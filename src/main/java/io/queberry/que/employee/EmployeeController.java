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
        return ResponseEntity.ok(employeeService.resetPassword(dto, request));
    }

    @PutMapping("/employees/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable String id, HttpServletRequest request) {
        try {
            Employee updated = employeeService.deactivateEmployee(id, request);
            return ResponseEntity.ok(updated);
        } catch (QueueException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/employees/active")
    public ResponseEntity<?> getActiveEmployeeCount() {

        return ResponseEntity.ok(employeeService.getActiveCount());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<?> getEmployee(@PathVariable String id) {
        try {
            EmployeeRequest employeeRequest = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(employeeRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/employees/filterByUsername")
    public ResponseEntity<?> fetchUserName(@RequestParam("userName") String username,
                                           Pageable pageable) {
        return ResponseEntity.ok(employeeService.filterEmployeesByUsername(username, pageable));
    }

    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeData employeeData) {
        try {
            employeeService.createEmployee(employeeData);
            return ResponseEntity.ok("Employee created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable String id, @RequestBody EmployeeData employeeData) {
        try {
            employeeService.updateEmployee(id, employeeData);
            return ResponseEntity.ok("Employee updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/employees")
    public ResponseEntity<?> getAllEmployees() {
        List<EmployeeRequest> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PutMapping("/employees/password/reset")
    public ResponseEntity resetUserPassword(
            @RequestBody PasswordResetResource resetResource,
            @RequestParam("username") String username,
            HttpServletRequest request) {

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
        Employee employee = employeeService.unlockEmployee(id);
        if (employee == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User ID doesn't exist!");
        }
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/employees/{id}/assign")
    public ResponseEntity<?> assignCounter(@PathVariable String id, @RequestParam("counterId") String counter) {
        try {
            Employee employee = employeeService.assignCounter(id, counter);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(e.getMessage());
        }
    }

    @PutMapping("/employees/password/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotDTO) {
        String result = employeeService.forgotPassword(forgotDTO);
        if (result.equals("User not found.") || result.contains("New password")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/employees/{id}/update/{username}")
    public ResponseEntity<?> updateSuperAdmin(@PathVariable String id, @PathVariable String username) {
        try {
            Employee employee = employeeService.updateUsername(id, username);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/employees/{id}/detach")
    public ResponseEntity<?> detach(@PathVariable String id) {
        try {
            Employee employee = employeeService.detachCounter(id);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, HttpServletRequest request) {
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
        return ResponseEntity.ok(employeeService.getActiveCounterAgents(request.getHeader("X-TenantID")));
    }

    @GetMapping("/employees/test")
    public void getExchangeRate1() throws IOException, InterruptedException {
        log.info("test curl");

        String cmd = "curl --location 'http://192.168.131.149:30004/api/reporting/report/live' \\\n" +
                "--header 'x-tenantid: queberry'";

        log.info("command: {}", cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        log.info("process:{}", process);
    }

    @PutMapping("/employees/dashboard")
    public ResponseEntity<EmpDashboardDtls> empDashboard(@RequestBody EmpDashboardRequest request) {
        EmpDashboardDtls result = employeeService.getEmployeeDashboard(request);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/employees/appointmentList")
    public ResponseEntity<?> getAppointmentList(@RequestBody EmpDashboardRequest services, Pageable pageable) {
    Page<Appointment> appointments = employeeService.getAppointmentList(services, pageable);
    return ResponseEntity.ok(appointments);
}

    @PutMapping("/employees/{id}/activate")
    public ResponseEntity<?> activate(@PathVariable("id") String id, @RequestParam(defaultValue = "admin") String performedBy) {
        try {
            Employee updated = employeeService.activateEmployee(id, performedBy);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

