package io.queberry.que.authentication;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.assistance.Assistance;
import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.config.Break.BreakSession;
import io.jsonwebtoken.impl.DefaultClaims;
import io.queberry.que.config.Break.BreakSessionRepository;
import io.queberry.que.config.EncryptionUtil;
import io.queberry.que.config.JwtTokenUtil;
import io.queberry.que.config.Sms.SmsConfigurationRepository;
import io.queberry.que.config.Tenant.TenantContext;
import io.queberry.que.config.Websocket.WebSocketOperations;
import io.queberry.que.customer.Customer;
import io.queberry.que.customer.CustomerRepository;
import io.queberry.que.employee.*;
import io.queberry.que.enums.Status;
import io.queberry.que.role.RoleRepository;
//import io.queberry.que.sms.SmsEngine;
import io.queberry.que.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Profile("dev")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api")
public class JwtAuthenticationControllerDev {

    private AuthenticationManager authenticationManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeSessionRepository employeeSessionRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

//    @Autowired
//    private SmsEngine smsEngine;

    @Autowired
    private SmsConfigurationRepository smsConfigurationRepository;

    @Autowired
    private AssistanceRepository assistanceRepository;
    @Autowired
    private RoleRepository rolesRepository;
    @Autowired
    private WebSocketOperations messagingTemplate;
    @Autowired
    private BreakSessionRepository breakSessionRepository;

    @PostMapping(value = "/login")
//	public ResponseEntity<?> createAuthenticationToken(@RequestBody Authenticate authenticationRequest) throws Exception {
    public ResponseEntity<?> createAuthenticationToken(@RequestBody String payload) {
        try {
            String decryptedData = EncryptionUtil.decrypt(payload);
            ObjectMapper mapper = new ObjectMapper();
            JwtAuthenticationController.Authenticate authenticationRequest = mapper.readValue(decryptedData, JwtAuthenticationController.Authenticate.class);
            log.info("{}", authenticationRequest);
            Employee empinfo = employeeRepository.findByUsername(authenticationRequest.getUsername());
            log.info("emp details");

            if (empinfo == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username doesn't exit");
            } else {
                TenantContext.setCurrentTenant(empinfo.getTenant());
                log.info("tenant before emp details");
                Employee emp = employeeRepository.findByUsername(authenticationRequest.getUsername());
                log.info("{}", emp.getId());
                log.info("tenant emp details");

                if (!emp.isActive()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your account is inactive. Please contact your admin");
                }
                Set<Assistance> assist;
                if (authenticate(authenticationRequest.getPassword(), emp)) {
                    LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
                    if (emp.getRoles().contains(rolesRepository.findByName("COUNTER_AGENT"))) {
                        log.info("counter agent");
                        assist = assistanceRepository.findByCreatedAtBetweenAndSessionsEmployeeAndSessionsStatus(start, end, emp.getId(), Status.ATTENDING, Sort.by(Sort.Order.desc("CreatedAt")));
                        if (assist.size() > 0) {
                            messagingTemplate.send("/notifications/employee/" + emp.getUsername(), "logout");
                            log.info("running token");
                            EmployeeInfo empData = setEmpResponse(emp);
                            assist.stream().findFirst().ifPresent(empData::setAssistance);
                            return ResponseEntity.status(HttpStatus.OK).body(empData);
                        }
                    }
                    if (emp.getLoggedTime() == null) {
                        log.info("clear logout");
                        EmployeeInfo empData = setEmpResponse(emp);
                        log.info("return empdata");
                        return ResponseEntity.status(HttpStatus.OK).body(empData);
                    } else {
                        log.info("not logged out properly");
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("An active session exists");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Password");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping ("/logout")
    @Transactional
    public ResponseEntity<?> logout(@RequestBody JwtAuthenticationController.Authenticate request){

        log.info("Processing login for username: {}", request.getUsername());

//		Optional<Employee> employee = employeeRepository.findEmployeeByUsername(request.getUsername());
        Employee emp = employeeRepository.findByUsername(request.getUsername());

//		if (!employee.isPresent()) {
        if (emp == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username doesn't exist");
        }

//		Employee emp = employee.get();
        // Log username
        log.info("Employee found: {}", emp.getUsername());

        // Define time range for today
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // Fetch and update employee sessions
        Set<EmployeeSessions> sessions = employeeSessionRepository.findByLoginTimeBetweenAndEmployeeIdAndLogoutTimeIsNull(
                startOfDay, endOfDay, emp.getId()
        );

        sessions.forEach(session -> session.setLogoutTime(LocalDateTime.now()));
        employeeSessionRepository.saveAll(sessions);

        // Handle logged time and break sessions
        if (emp.getLoggedTime() != null) {
            Set<BreakSession> breakSessions = breakSessionRepository.findByEmployeeIdAndStartTimeBetweenAndEndTimeIsNull(
                    emp.getId(), emp.getLoggedTime(), LocalDateTime.now()
            );

            breakSessions.forEach(breakSession -> breakSession.setEndTime(LocalDateTime.now()));
            breakSessionRepository.saveAll(breakSessions);

            // Reset employee logged data
            emp.setLoggedTime(null);
            emp.setLoggedCounter(null);
            employeeRepository.save(emp);
        }

        return ResponseEntity.ok(HttpStatus.OK);
    }

//	@GetMapping("/logout")
//    public ResponseEntity logout(HttpServletRequest request){
//		String token = request.getHeader("Authorization").substring(7);
////		log.info("in logout"+ token);
//        String username = jwtTokenUtil.getUsernameFromToken(token);
////		log.info("username"+ username);
//		Employee empInfo = employeeRepository.findByUsername(username);
//		TenantContext.setCurrentTenant(empInfo.getTenant());
//		Employee emp = employeeRepository.findByUsername(username);
////		log.info("e info"+emp.getId());
//		Set<EmployeeSessions> es = employeeSessionRepository.findByEmployeeAndLogoutTimeIsNull(emp);
    ////		log.info("info"+ es.size());
//		es.forEach(e -> {
//			e.setLogoutTime(LocalDateTime.now());
//			employeeSessionRepository.save(e);
//		});
//		return ResponseEntity.ok(HttpStatus.OK);
//	}

    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");
//        log.info("claims info {}", claims);
        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

    private boolean authenticate(String password, Employee emp) throws Exception {
//		log.info("in authenticate");
        try {
//			Employee e = employeeRepository.findByUsername(username);
            if(new BCryptPasswordEncoder().matches(password,emp.getPassword())){
                return true;
            }else{
                return false;
            }
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        }
    }

    private EmployeeInfo setEmpResponse(Employee emp){
        EmployeeInfo empData = new EmployeeInfo();
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        Set<EmployeeSessions> es = employeeSessionRepository.findByLoginTimeBetweenAndEmployeeIdAndLogoutTimeIsNull(startOfDay, endOfDay, emp.getId());
        if (es.size() > 0) {
            es.forEach(e -> {
                e.setLogoutTime(LocalDateTime.now());
                employeeSessionRepository.save(e);
            });
        }
        log.info("id:{}", emp.getId());
        log.info("tennat:{}", TenantContext.getCurrentTenant());
        EmployeeSessions employeeSessions = new EmployeeSessions(emp, LocalDateTime.now());
        employeeSessionRepository.save(employeeSessions);

//		emp.setLoggedTime(LocalDateTime.now());
//		emp.setLoggedCounter(null);
//		employeeRepository.save(emp);
        EmployeeDTO employeeDTO = new EmployeeDTO(emp);
        empData.setEmployee(employeeDTO);
        final String token = jwtTokenUtil.generateToken(emp, employeeSessions);
        empData.setToken(token);
        log.info("before return");
        employeeRepository.save(emp);
        return empData;
    }


    // for customer login

//	@PostMapping(value = "/customerLogin")
//	public ResponseEntity<?> customerAuthToken(@RequestBody JwtAuthenticationController.Authenticate authenticationRequest){
//
//		CustomerInfo custData = new CustomerInfo();
//		Customer customerInfo = null;
//		Optional<Customer> customer = customerRepository.findByUsername(authenticationRequest.getUsername());
//
//		if(!customer.isPresent())
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer doesn't exist");
//		customerInfo = customer.get();
//		if(new BCryptPasswordEncoder().matches(authenticationRequest.getPassword(), Objects.requireNonNull(customerInfo).getPassword())) {
//			custData.setCustomer(customerInfo);
//			Map<String, Object> claims = new HashMap<>();
//			final String token = jwtTokenUtil.doGenerateRefreshToken(claims,customerInfo.getUsername());
//			custData.setToken(token);
//			return ResponseEntity.status(HttpStatus.OK).body(custData);
//		}else {
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//		}
//	}
//
//	@PostMapping("/customerSignup")
//	private ResponseEntity<?> getCustomerInfo(@RequestBody Customer customerRequest ){
//		Customer customer = null;
//		Optional<Customer> cust = customerRepository.findByUsername(customerRequest.getUsername());
//		String password = new BCryptPasswordEncoder().encode(customerRequest.getPassword());
//		customerRequest.setPassword(password);
//
//		if(!cust.isPresent()){
//
//			String otp = Customer.getRandom6DigitNumber().toString();
//			customerRequest.setSmsotp(otp);
//
//			SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().get(0);
//			if (smsConfiguration.isEnabled()) {
//				try {
//					smsEngine.send(customerRequest.getMobile(), "Dear Customer, use OTP " + otp + " to verify your mobile number.", smsConfiguration);
//				} catch (Exception e) {
//					log.error("Error while sending OTP to customer!!");
//					e.printStackTrace();
//				}
//			}
//
//			customer = customerRepository.save(customerRequest);
//			Map<String, Object> claims = new HashMap<>();
//			final String token = jwtTokenUtil.doGenerateRefreshToken(claims,customerRequest.getUsername());
//			CustomerInfo cinfo = new CustomerInfo();
//			cinfo.setCustomer(customer);
//			cinfo.setToken(token);
//			return ResponseEntity.ok().body(cinfo);
//		}else{
//			return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
//		}
//	}
//
//	@PostMapping("/customerOtpVerification")
//	private ResponseEntity<?> CustOtpVer(@RequestBody Customer customerRequest ){
//		Optional<Customer> cust = customerRepository.findByUsername(customerRequest.getUsername());
//		if (!cust.isPresent()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer doesn't exist");
//		}
//		else {
//			Customer customer = cust.get();
//			if (customerRequest.getSmsotp().equals(customer.getSmsotp())) {
//				customer.setOtpVerified(true);
//				customer.setOtpVerifiedDt(LocalDateTime.now());
//				customerRepository.save(customer);
//				Map<String, Object> claims = new HashMap<>();
//				final String token = jwtTokenUtil.doGenerateRefreshToken(claims,customerRequest.getUsername());
//				CustomerInfo cinfo = new CustomerInfo();
//				cinfo.setCustomer(customer);
//				cinfo.setToken(token);
//				return ResponseEntity.ok().body(cinfo);
//			}
//			else {
//				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please enter valid OTP!!");
//			}
//		}
//	}
//
//	@PostMapping("/customerExists")
//	private ResponseEntity<?> CustExists(@RequestBody Customer customerRequest ){
//		Optional<Customer> customer = customerRepository.findByUsername(customerRequest.getUsername());
//		if (!customer.isPresent()) {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer doesn't exist");
//		}
//		else
//		{
//			return ResponseEntity.status(HttpStatus.OK).body(customer.get());
//		}
//	}



    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Authenticate{
        private String username;
        private String password;
    }

    @Data
    public class EmployeeInfo{
        private EmployeeDTO employee;
        private String token;
        private Assistance assistance;
        private Token tokenRef;
    }

    @Data
    public class CustomerInfo{
        private Customer customer;
        private String token;
    }

}
