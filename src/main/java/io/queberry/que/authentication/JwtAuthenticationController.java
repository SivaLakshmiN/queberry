package io.queberry.que.authentication;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.queberry.que.assistance.Assistance;
import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.authentication.JwtResponse;
import io.queberry.que.config.Break.BreakSession;
import io.queberry.que.config.Break.BreakSessionRepository;
import io.queberry.que.config.EncryptionUtil;
import io.queberry.que.config.JwtTokenUtil;
import io.queberry.que.config.Websocket.WebSocketOperations;
import io.queberry.que.counter.CounterRepository;
import io.queberry.que.customer.Customer;
import io.queberry.que.customer.CustomerRepository;
import io.queberry.que.employee.*;
import io.queberry.que.counter.Counter;
import io.jsonwebtoken.impl.DefaultClaims;
import io.queberry.que.enums.Status;
import io.queberry.que.failed.FailedLogin;
import io.queberry.que.failed.FailedLoginRepository;
import io.queberry.que.passwordManagement.PasswordPolicy;
import io.queberry.que.passwordManagement.PasswordPolicyRepository;
import io.queberry.que.role.RoleRepository;
import io.queberry.que.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Profile("prod")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api")
public class JwtAuthenticationController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeSessionRepository employeeSessionRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AssistanceRepository assistanceRepository;

    private final WebSocketOperations messagingTemplate;

    @Autowired
    private BreakSessionRepository breakSessionRepository;
    @Autowired
    private RoleRepository rolesRepository;
    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private PasswordPolicyRepository passwordPolicyRepository;

    @Autowired
    private FailedLoginRepository failedLoginRepository;

    public JwtAuthenticationController(WebSocketOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//	private static final byte[] KEY = "queberry123".getBytes(StandardCharsets.UTF_8); // Replace with a secure key


    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody String payload) {
        try {
            String decryptedData = EncryptionUtil.decrypt(payload);
            ObjectMapper mapper = new ObjectMapper();
            Authenticate authenticationRequest = mapper.readValue(decryptedData, Authenticate.class);

            log.info("Login request: {}", authenticationRequest.getUsername());

            Employee emp = employeeRepository.findByUsername(authenticationRequest.getUsername());
            LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

            if (emp == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "Username doesn't exist"
                ));
            }

            if (!emp.isActive() || emp.isLocked()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "status", "error",
                        "message", "Your account is locked/inactive. Please contact your admin"
                ));
            }

            if (!authenticate(authenticationRequest.getPassword(), emp)) {
                if (emp.getRoles().contains(rolesRepository.findByName("PRODUCT_ADMIN"))) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                            "status", "error",
                            "message", "Incorrect password"
                    ));
                }

                PasswordPolicy policy = passwordPolicyRepository.findAll().stream().findFirst().orElse(null);
                if (policy != null) {
                    int failedCount = failedLoginRepository.countFailedAttempts(emp.getId(), start);
                    int remaining = policy.getFailedAttempts() - failedCount;

                    failedLoginRepository.save(new FailedLogin(emp.getId(), LocalDateTime.now()));

                    if (remaining > 1) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                                "status", "error",
                                "message", "Incorrect password. " + (remaining - 1) + " attempt(s) left."
                        ));
                    } else {
                        emp.setLocked(true);
                        employeeRepository.save(emp);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                                "status", "error",
                                "message", "Your account is locked due to multiple failed attempts"
                        ));
                    }
                }
            }

            // At this point, authentication passed
            if (messagingTemplate != null) {
                try {
                    messagingTemplate.send("/notifications/employee/" + emp.getUsername(), "logout");
                } catch (Exception ex) {
                    log.warn("WebSocket logout message failed for {}", emp.getUsername(), ex);
                }
            }

            EmployeeInfo empData = setEmpResponse(emp);

            // Handle COUNTER_AGENT logic
            if (emp.getRoles().contains(rolesRepository.findByName("COUNTER_AGENT"))) {
                Set<Assistance> assists = assistanceRepository
                        .findByCreatedAtBetweenAndSessionsEmployeeAndSessionsStatus(start, end, emp.getId(), Status.ATTENDING,
                                Sort.by(Sort.Order.desc("CreatedAt")));

                if (!assists.isEmpty()) {
                    assists.stream().findFirst().ifPresent(empData::setAssistance);
                } else if (emp.getLoggedCounter() != null) {
                    counterRepository.findCounterById(emp.getLoggedCounter())
                            .ifPresent(counter -> empData.employee.setCounter(String.valueOf(counter)));
                }
            }

            String message = emp.getLoggedTime() == null
                    ? "Login successful"
                    : "Previous session was terminated. You are now logged in.";

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", message,
                    "data", empData
            ));

        } catch (Exception e) {
            log.error("Login exception", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Something went wrong. Please try again."
            ));
        }
    }



//    @PostMapping("/login")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody String payload) {
//        try {
//            String decryptedData = EncryptionUtil.decrypt(payload);
//            ObjectMapper mapper = new ObjectMapper();
//            Authenticate authenticationRequest = mapper.readValue(decryptedData, Authenticate.class);
//            log.info("{}", authenticationRequest);
//
//            Employee emp = employeeRepository.findByUsername(authenticationRequest.getUsername());
//            log.info("emp details");
//            LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//            LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//
//            if (emp == null) {
//                return ResponseEntity.ok(Map.of(
//                        "status", "error",
//                        "message", "Username doesn't exist"
//                ));
//            } else {
//                if (!emp.isActive() || emp.isLocked()) {
//                    return ResponseEntity.ok(Map.of(
//                            "status", "error",
//                            "message", "Your account is locked/inactive. Please contact your admin"
//                    ));
//                }
//
//                Set<Assistance> assist;
//                if (authenticate(authenticationRequest.getPassword(), emp)) {
//                    if (emp.getRoles().contains(rolesRepository.findByName("COUNTER_AGENT"))) {
//                        assist = assistanceRepository.findByCreatedAtBetweenAndSessionsEmployeeAndSessionsStatus(
//                                start, end, emp.getId(), Status.ATTENDING, Sort.by(Sort.Order.desc("CreatedAt"))
//                        );
//                        if (assist.size() > 0) {
//                            messagingTemplate.send("/notifications/employee/" + emp.getUsername(), "logout");
//                            log.info("running token");
//                            EmployeeInfo empData = setEmpResponse(emp);
//                            assist.stream().findFirst().ifPresent(empData::setAssistance);
//                            return ResponseEntity.ok(Map.of(
//                                    "status", "success",
//                                    "message", "Login successful",
//                                    "data", empData
//                            ));
//                        }
//                        if (emp.getLoggedCounter() != null) {
//                            log.info("logged counter");
//                            Counter counter = counterRepository.findCounterById(emp.getLoggedCounter()).get();
//                            log.info("received counter");
//                            messagingTemplate.send("/notifications/employee/" + emp.getUsername(), "logout");
//                            EmployeeInfo empData = setEmpResponse(emp);
//                            empData.employee.setCounter(String.valueOf(counter));
//                            log.info("emp data:{}", empData);
//                            return ResponseEntity.ok(Map.of(
//                                    "status", "success",
//                                    "message", "Login successful",
//                                    "data", empData
//                            ));
//                        }
//                    }
//
//                    if (emp.getLoggedTime() == null) {
//                        log.info("clear logout");
//                        EmployeeInfo empData = setEmpResponse(emp);
//                        return ResponseEntity.ok(Map.of(
//                                "status", "success",
//                                "message", "Login successful",
//                                "data", empData
//                        ));
//                    } else {
//                        log.info("not logged out properly");
//                        EmployeeInfo empData = setEmpResponse(emp);
//                        return ResponseEntity.ok(Map.of(
//                                "status", "warning",
//                                "message", "An active session exists",
//                                "data", empData
//                        ));
//                    }
//                } else {
//                    if (emp.getRoles().contains(rolesRepository.findByName("PRODUCT_ADMIN"))) {
//                        return ResponseEntity.ok(Map.of(
//                                "status", "error",
//                                "message", "Incorrect password"
//                        ));
//                    } else {
//                        PasswordPolicy passwordPolicy = passwordPolicyRepository.findAll().stream().findFirst().orElse(null);
//                        int count;
//                        if (passwordPolicy != null) {
//                            count = passwordPolicy.getFailedAttempts() - failedLoginRepository.countFailedAttempts(emp.getId(), start);
//                            FailedLogin failedLogin = new FailedLogin(emp.getId(), LocalDateTime.now());
//                            failedLoginRepository.save(failedLogin);
//                            if (count > 1) {
//                                return ResponseEntity.ok(Map.of(
//                                        "status", "error",
//                                        "message", "Incorrect Password only " + (count - 1) + " left"
//                                ));
//                            } else {
//                                emp.setLocked(true);
//                                employeeRepository.save(emp);
//                                return ResponseEntity.ok(Map.of(
//                                        "status", "error",
//                                        "message", "Your account is locked, Please contact your admin"
//                                ));
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return ResponseEntity.ok(Map.of(
//                "status", "error",
//                "message", "Internal server error. Please try again later."
//        ));
//    }


    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody String payload) {
        try {
            // Decrypt incoming payload
            String decryptedData = EncryptionUtil.decrypt(payload);
            ObjectMapper mapper = new ObjectMapper();
            Authenticate authenticationRequest = mapper.readValue(decryptedData, Authenticate.class);

            log.info("Verify request received for username: {}", authenticationRequest.getUsername());

            // Fetch employee by username
            Employee employee = employeeRepository.findByUsername(authenticationRequest.getUsername());
            if (employee == null) {
                log.warn("Username not found: {}", authenticationRequest.getUsername());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "Username doesn't exist"
                ));
            }

            // Check authentication
            if (authenticate(authenticationRequest.getPassword(), employee)) {
                EmployeeInfo empData = setEmpResponse(employee);

                // Attempt to send WebSocket logout message (fail silently if not initialized)
                try {
                    if (messagingTemplate != null) {
                        messagingTemplate.send("/notifications/employee/" + employee.getUsername(), "logout");
                    }
                } catch (Exception ex) {
                    log.warn("WebSocket notification failed for user: {}", employee.getUsername(), ex);
                }

                log.info("Authentication success for {}", employee.getUsername());
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Authentication successful",
                        "data", empData
                ));
            } else {
                log.warn("Incorrect password for user: {}", employee.getUsername());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "status", "error",
                        "message", "Incorrect password"
                ));
            }

        } catch (Exception e) {
            log.error("Exception during /verify", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Something went wrong. Please try again later"
            ));
        }
    }




    @PostMapping("/greeter/login")
    public ResponseEntity<?> createAuthenticationGToken(@RequestBody Authenticate authenticationRequest){

        try {
//			String decryptedData = EncryptionUtil.decrypt(payload);
//			ObjectMapper mapper = new ObjectMapper();
//			Authenticate authenticationRequest = mapper.readValue(decryptedData, Authenticate.class);
//			log.info("{}",authenticationRequest);
            Employee emp = employeeRepository.findByUsername(authenticationRequest.getUsername());
            log.info("emp details");

            if(emp == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username doesn't exit");
            } else {
                if (!emp.isActive()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Your account is inactive. Please contact your admin");
                }
                Set<Assistance> assist;
                if(authenticate(authenticationRequest.getPassword(), emp)) {
                    LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
                    LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
                    if(emp.getRoles().contains(rolesRepository.findByName("COUNTER_AGENT"))){
                        assist = assistanceRepository.findByCreatedAtBetweenAndSessionsEmployeeAndSessionsStatus(start,end,emp.getId(), Status.ATTENDING, Sort.by(Sort.Order.desc("CreatedAt")));
                        if(assist.size() > 0){
                            messagingTemplate.send("/notifications/employee/" + emp.getUsername(), "logout");
                            log.info("running token");
                            EmployeeInfo empData = setEmpResponse(emp);
                            assist.stream().findFirst().ifPresent(empData::setAssistance);
                            return ResponseEntity.status(HttpStatus.OK).body(empData);
                        }
                    }
                    if(emp.getLoggedTime() == null) {
                        log.info("clear logout");
                        EmployeeInfo empData = setEmpResponse(emp);
                        return ResponseEntity.status(HttpStatus.OK).body(empData);
                    }
                    else {
                        log.info("not logged out properly");
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("An active session exists");
                    }
                }else{
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Password");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/greeter/verify")
    public ResponseEntity<?> gVerify(@RequestBody Authenticate authenticationRequest){
//	public ResponseEntity<?> verify(@RequestBody Authenticate authenticationRequest){
        try {
//			String decryptedData = EncryptionUtil.decrypt(payload);
//			ObjectMapper mapper = new ObjectMapper();
//			Authenticate authenticationRequest = mapper.readValue(decryptedData, Authenticate.class);
//			Optional<Employee> emp = employeeRepository.findEmployeeByUsername(authenticationRequest.getUsername());
            Employee employee = employeeRepository.findByUsername(authenticationRequest.getUsername());

            if(employee == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username doesn't exit");
            try {
                if (authenticate(authenticationRequest.getPassword(), employee)) {
                    EmployeeInfo empData = setEmpResponse(employee);
                    messagingTemplate.send("/notifications/employee/" + employee.getUsername(), "logout");
                    return ResponseEntity.status(HttpStatus.OK).body(empData);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect Password");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.status(HttpStatus.OK).body("Please try later");
//			}
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body("Please try later");
    }

    private EmployeeInfo setEmpResponse(Employee emp){
        EmployeeInfo empData = new EmployeeInfo();
        // to close any active sessions
//		Set<EmployeeSessions> es = employeeSessionRepository.findByEmployeeAndLogoutTimeIsNull(emp);
//		Set<EmployeeSessions> es = employeeSessionRepository.findByLogoutTimeIsNullAndEmployeeId(emp.getId());
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        Set<EmployeeSessions> es = employeeSessionRepository.findByLoginTimeBetweenAndEmployeeIdAndLogoutTimeIsNull(startOfDay, endOfDay, emp.getId());
        if (es.size() > 0) {
            es.forEach(e -> {
                e.setLogoutTime(LocalDateTime.now());
                employeeSessionRepository.save(e);
            });
        }

        EmployeeSessions employeeSessions = new EmployeeSessions(emp, LocalDateTime.now());
        employeeSessionRepository.save(employeeSessions);

        emp.setLoggedTime(LocalDateTime.now());
//		emp.setLoggedCounter(null);
        log.info(emp.getRegion());
        log.info(String.valueOf(emp.getBranches().size()));
        employeeRepository.save(emp);
        EmployeeDTO employeeDTO = new EmployeeDTO(emp);
        empData.setEmployee(employeeDTO);
        final String token = jwtTokenUtil.generateToken(emp, employeeSessions);
        empData.setToken(token);
        return empData;
    }


//    @PostMapping ("/logout")
//    @Transactional
//    public ResponseEntity<?> logout(@RequestBody Authenticate request){
//
//        log.info("Processing logout for username: {}", request.getUsername());
//
//        Employee emp = employeeRepository.findByUsername(request.getUsername());
//
//        if (emp == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username doesn't exist");
//        }
//        // Log username
//        log.info("Employee found: {}", emp.getUsername());
//
//        // Define time range for today
//        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
//        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
//
//        // Fetch and update employee sessions
//        Set<EmployeeSessions> sessions = employeeSessionRepository.findByLoginTimeBetweenAndEmployeeIdAndLogoutTimeIsNull(
//                startOfDay, endOfDay, emp.getId()
//        );
//
//        sessions.forEach(session -> session.setLogoutTime(LocalDateTime.now()));
//        employeeSessionRepository.saveAll(sessions);
//
//        // Handle logged time and break sessions
//        if (emp.getLoggedTime() != null) {
//            Set<BreakSession> breakSessions = breakSessionRepository.findByEmployeeIdAndStartTimeBetweenAndEndTimeIsNull(
//                    emp.getId(), emp.getLoggedTime(), LocalDateTime.now()
//            );
//
//            breakSessions.forEach(breakSession -> breakSession.setEndTime(LocalDateTime.now()));
//            breakSessionRepository.saveAll(breakSessions);
//
//            // Reset employee logged data
//            emp.setLoggedTime(null);
//            emp.setLoggedCounter(null);
//            employeeRepository.save(emp);
//        }
//
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

    @PostMapping("/logout")
    @Transactional
    public ResponseEntity<?> logout(@RequestBody Authenticate request) {

        log.info("Processing logout for username: {}", request.getUsername());

        Employee emp = employeeRepository.findByUsername(request.getUsername());

        if (emp == null) {
            log.warn("Logout failed: username not found -> {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username doesn't exist");
        }

        log.info("Employee found: {}", emp.getUsername());

        // Define today's date range
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);

        // Close active employee sessions
        Set<EmployeeSessions> sessions = employeeSessionRepository
                .findByLoginTimeBetweenAndEmployeeIdAndLogoutTimeIsNull(startOfDay, endOfDay, emp.getId());

        if (!sessions.isEmpty()) {
            sessions.forEach(session -> session.setLogoutTime(LocalDateTime.now()));
            employeeSessionRepository.saveAll(sessions);
            log.info("Closed {} active session(s) for {}", sessions.size(), emp.getUsername());
        }
        if (emp.getLoggedTime() != null) {
            Set<BreakSession> breakSessions = breakSessionRepository
                    .findByEmployeeIdAndStartTimeBetweenAndEndTimeIsNull(emp.getId(), emp.getLoggedTime(), LocalDateTime.now());

            if (!breakSessions.isEmpty()) {
                breakSessions.forEach(brk -> brk.setEndTime(LocalDateTime.now()));
                breakSessionRepository.saveAll(breakSessions);
                log.info("Closed {} break session(s) for {}", breakSessions.size(), emp.getUsername());
            }
            emp.setLoggedTime(null);
            emp.setLoggedCounter(null);
            employeeRepository.save(emp);
            log.info("Reset logged time and counter for employee: {}", emp.getUsername());
        } else {
            log.info("No loggedTime set for employee: {}, skipping session reset.", emp.getUsername());
        }

        return ResponseEntity.ok("Logout successful");
    }


    @RequestMapping(value = "/refreshtoken", method = RequestMethod.GET)
    public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws Exception {
        // From the HttpRequest get the claims
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
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


    // for customer login

    @PostMapping(value = "/customerLogin")
    public ResponseEntity<?> customerAuthToken(@RequestBody Authenticate authenticationRequest){

        CustomerInfo custData = new CustomerInfo();
        Customer customerInfo = null;
        customerInfo = customerRepository.findByUsername(authenticationRequest.getUsername());

        if(customerInfo == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer doesn't exist");
//		if(!customer.isPresent())
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer doesn't exist");
//		customerInfo = customer.get();
        if(new BCryptPasswordEncoder().matches(authenticationRequest.getPassword(), Objects.requireNonNull(customerInfo).getPassword())) {
            custData.setCustomer(customerInfo);
            Map<String, Object> claims = new HashMap<>();
            final String token = jwtTokenUtil.doGenerateRefreshToken(claims,customerInfo.getUsername());
            custData.setToken(token);
            return ResponseEntity.status(HttpStatus.OK).body(custData);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

//	@PostMapping("/customerSignup")
//	private ResponseEntity getCustomerInfo(@RequestBody CustomerRequest customerRequest ){
//
//		boolean found = false;
//		SmsConfiguration smsConfiguration = smsConfigurationRepository.findAll().get(0);
//		try {
//			Optional<Customer> customer = customerRepository.findByMobile(customerRequest.getMobile());
//			found = true;
//		}
//		catch (NullPointerException e) {
//			log.info("null pointer");
//		}
//
//		finally {
//			if(found)
//				return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
//			else {
//				Customer cust = new Customer();
//				String password = new BCryptPasswordEncoder().encode(customerRequest.getPassword());
//				cust.setPassword(password);
//
//				String otp = Customer.getRandom6DigitNumber().toString();
//				cust.setSmsotp(otp);
//				cust.setFullName(customerRequest.getFullName());
//				cust.setUsername(customerRequest.getUsername());
//				cust.setMobile(customerRequest.getMobile());
//
//				if (smsConfiguration.isEnabled()) {
//					try {
//						smsEngine.send(customerRequest.getMobile(), "Dear Customer, use OTP " + otp + " to verify your mobile number.", smsConfiguration);
//					} catch (Exception eq) {
//						log.error("Error while sending OTP to customer!!");
//						eq.printStackTrace();
//					}
//				}
//
//				cust = customerRepository.save(cust);
//				Map<String, Object> claims = new HashMap<>();
//				final String token = jwtTokenUtil.doGenerateRefreshToken(claims,customerRequest.getUsername());
//				CustomerInfo cinfo = new CustomerInfo();
//				cinfo.setCustomer(cust);
//				cinfo.setToken(token);
//				return ResponseEntity.ok().body(cinfo);
//			}
//		}
//
//
//
    ////		if(customer.isPresent())
    ////			return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
//	}

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
    @NoArgsConstructor
    public static class Authenticate{
        private String username;
        private String password;

        @JsonCreator
        public Authenticate(
                @JsonProperty("username") String username,
                @JsonProperty("password") String password
        ) {
            this.username = username;
            this.password = password;
        }
    }

    @Data
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerRequest{
        private String username;
        private String password;
        private String fullName;
        private String mobile;
    }

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    public static class LogoutReq{
        private String username;
    }

    @Data
    public static class EmployeeInfo{
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