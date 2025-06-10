package io.queberry.que.employee;

import io.queberry.que.appointment.Appointment;
import io.queberry.que.passwordManagement.ForgotPasswordDTO;
import io.queberry.que.passwordManagement.PasswordResetDTO;
import io.queberry.que.auditLogs.AuditLogs;
import io.queberry.que.exception.QueueException;
import io.queberry.que.auditLogs.AuditLogsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final AuditLogsRepository auditLogsRepository;

    @PutMapping("/employees/password/resets")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDTO dto, HttpServletRequest request) {
        log.info("resetPassword called with dto={}, requester={}", dto, request.getRemoteUser());
        return ResponseEntity.ok(employeeService.resetPassword(dto, request));
    }

    @PutMapping("/employees/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable String id, HttpServletRequest request) {
        log.info("deactivate called with id={}, requester={}", id, request.getRemoteUser());
        try {
            Employee updated = employeeService.deactivateEmployee(id, request);
            return ResponseEntity.ok(updated);
        } catch (QueueException e) {
            log.error("Error in deactivate for id={}: {}", id, e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/employees/active")
    public ResponseEntity<?> getActiveEmployeeCount() {
        log.info("getActiveEmployeeCount called");
        return ResponseEntity.ok(employeeService.getActiveCount());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id) {
        log.info("getEmployee called with id={}", id);
        try {
            EmployeeRequest employeeRequest = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employeeRequest);
        } catch (RuntimeException e) {
            log.error("Error fetching employee id={}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/employees/filterByUsername")
    public ResponseEntity<?> fetchUserName(@RequestParam("userName") String username, Pageable pageable) {
        log.info("fetchUserName called with username={}, pageable={}", username, pageable);
        return ResponseEntity.ok(employeeService.filterEmployeesByUsername(username, pageable));
    }

    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeData employeeData) {
        log.info("createEmployee called with employeeData={}", employeeData);
        try {
            employeeService.createEmployee(employeeData);
            return ResponseEntity.ok("Employee created successfully.");
        } catch (Exception e) {
            log.error("Error creating employee: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody EmployeeData employeeData) {
        log.info("updateEmployee called with id={}, employeeData={}", id, employeeData);
        try {
            employeeService.updateEmployee(id, employeeData);
            return ResponseEntity.ok("Employee updated successfully.");
        } catch (Exception e) {
            log.error("Error updating employee id={}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/employees")
    public ResponseEntity<?> getAllEmployees() {
        log.info("getAllEmployees called");
        List<EmployeeRequest> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/employees/password/reset")
    public ResponseEntity resetUserPassword(
            @RequestBody PasswordResetResource resetResource,
            @RequestParam("username") String username,
            HttpServletRequest request) {

        log.info("resetUserPassword called for username={}", username);
        Employee employee = employeeService.resetUserPassword(username, resetResource.getPassword());

        AuditLogs auditLogs = new AuditLogs();
        auditLogs.setEntityName("User");
        auditLogs.setEntityId(employee.getId());
        auditLogs.setEntityField("Password");
        auditLogs.setNewData("Reset password");
        auditLogs.setCreatedBy(username);
        auditLogsRepository.save(auditLogs);
        return ResponseEntity.ok().build();
    }

    @Data
    public static class PasswordResetResource {
        private String password;
    }

    @PutMapping("/employees/{id}/unlock")
    public ResponseEntity<?> unlock(@PathVariable("id") String id) {
        log.info("unlock called with id={}", id);
        Employee employee = employeeService.unlockEmployee(id);
        if (employee == null) {
            log.warn("Unlock failed - user id={} does not exist", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID doesn't exist!");
        }
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/employees/{id}/assign")
    public ResponseEntity<?> assignCounter(@PathVariable String id, @RequestParam("counterId") String counter) {
        log.info("assignCounter called with id={}, counterId={}", id, counter);
        try {
            Employee employee = employeeService.assignCounter(id, counter);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            log.error("Error assigning counter for id={}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
    }

    @PutMapping("/employees/password/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotDTO) {
        log.info("forgotPassword called for username={}", forgotDTO.getUsername());
        String result = employeeService.forgotPassword(forgotDTO);
        if (result.equals("User not found.") || result.contains("New password")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/employees/{id}/update/{username}")
    public ResponseEntity<?> updateSuperAdmin(@PathVariable String id, @PathVariable String username) {
        log.info("updateSuperAdmin called with id={}, username={}", id, username);
        try {
            Employee employee = employeeService.updateUsername(id, username);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            log.error("Error updating super admin id={}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/employees/{id}/detach")
    public ResponseEntity<?> detach(@PathVariable String id) {
        log.info("detach called with id={}", id);
        try {
            Employee employee = employeeService.detachCounter(id);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            log.error("Error detaching counter for id={}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request) {
        log.info("delete called with id={}, performedBy={}", id, request.getUserPrincipal().getName());
        String result = employeeService.deleteEmployee(id, request.getUserPrincipal().getName());
        if (result.contains("Cannot delete")) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(result);
        } else if (result.contains("doesn't exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/employees/active/counterAgents")
    public ResponseEntity<?> getActiveCounterAgents(HttpServletRequest request) {
        log.info("getActiveCounterAgents called with tenantId={}", request.getHeader("X-TenantID"));
        return ResponseEntity.ok(employeeService.getActiveCounterAgents(request.getHeader("X-TenantID")));
    }

    @GetMapping("/employees/test")
    public void getExchangeRate1() throws IOException, InterruptedException {
        log.info("getExchangeRate1 called - test curl");
        String cmd = "curl --location 'http://192.168.131.149:30004/api/reporting/report/live' \\\n" +
                "--header 'x-tenantid: queberry'";
        log.info("command: {}", cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        log.info("process:{}", process);
    }

    @PutMapping("/employees/dashboard")
    public ResponseEntity<EmpDashboardDtls> empDashboard(@RequestBody EmpDashboardRequest request) {
        log.info("empDashboard called with request={}", request);
        EmpDashboardDtls result = employeeService.getEmployeeDashboard(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/employees/appointmentList")
    public ResponseEntity<?> getAppointmentList(@RequestBody EmpDashboardRequest services, Pageable pageable) {
        log.info("getAppointmentList called with services={}, pageable={}", services, pageable);
        Page<Appointment> appointments = employeeService.getAppointmentList(services, pageable);
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/employees/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable("id") String id, @RequestParam(defaultValue = "admin") String performedBy) {
        log.info("activate called with id={}, performedBy={}", id, performedBy);
        try {
            Employee updated = employeeService.activateEmployee(id, performedBy);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            log.error("Error activating employee id={}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}