package io.queberry.que.employee;

import io.queberry.que.passwordManagement.ForgotPasswordDTO;
import io.queberry.que.passwordManagement.PasswordResetDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {
    String resetPassword(PasswordResetDTO dto, HttpServletRequest request);
    Employee deactivateEmployee(String id, HttpServletRequest request);
    int getActiveCount();
    Employee activateEmployee(String id, String performedBy);
    EmployeeRequest createEmployee(EmployeeData employeeData);
    EmployeeRequest updateEmployee(String id, EmployeeData employeeData);
    Page<EmployeeRequest> filterEmployeesByUsername(String username, Pageable pageable);
    List<EmployeeRequest> getAllEmployees();
    EmployeeRequest getEmployeeById(String id);
    Employee resetUserPassword(String username, String newPassword);
    Employee unlockEmployee(String id);
    void updateMasterUser(Employee employee);
    Employee assignCounter(String employeeId, String counter);
    String forgotPassword(ForgotPasswordDTO forgotDTO);
    Employee updateUsername(String employeeId, String newUsername);
    Employee detachCounter(String employeeId);
    String deleteEmployee(String employeeId, String loggedInUsername);
    List<Employee> getActiveCounterAgents(String branchKey);
    EmpDashboardDtls getEmployeeDashboard(EmpDashboardRequest request);
    //Page<Appointment> getAppointmentList(EmpDashboardRequest services, Pageable pageable);

}
