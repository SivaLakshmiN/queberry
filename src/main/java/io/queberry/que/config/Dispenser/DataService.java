package io.queberry.que.config.Dispenser;

import io.queberry.que.assistance.AssistanceRepository;
import io.queberry.que.config.Tenant.TenantContext;
import io.queberry.que.counter.CounterRepository;
import io.queberry.que.employee.Employee;
import io.queberry.que.employee.EmployeeRepository;
import io.queberry.que.employee.EmployeeSessionRepository;
import io.queberry.que.service.ServiceRepository;
import io.queberry.que.session.SessionRepository;
import io.queberry.que.token.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DataService {

    @Autowired
    private AssistanceRepository assistanceRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CounterRepository counterRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeSessionRepository employeeSessionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public void deleteAllData() {
        try {
            // Delete all sessions first (child table)
            sessionRepository.deleteAll();

            // Delete all assistance records (parent table)
            assistanceRepository.deleteAll();
            log.info("deleted assistance");
            tokenRepository.deleteAll();
            log.info("token deleted");
//            counterRepository.deleteAll();
            jdbcTemplate.execute("delete from que_counter_first");
            log.info("deleted first counter ser");
            jdbcTemplate.execute("delete from que_counter_second");
            log.info("deleted second counter ser");
            jdbcTemplate.execute("delete from que_counter_third");
            log.info("deleted third counter ser");
            jdbcTemplate.execute("delete from que_counter_fourth");
            log.info("deleted fourth counter ser");
            employeeSessionRepository.deleteAll();
            log.info("emp sessio deleted");
            jdbcTemplate.execute("delete from que_employee_services");
            log.info("deleted emp services");
            jdbcTemplate.execute("delete from que_employee_second");
            log.info("deleted second emp ser");
            jdbcTemplate.execute("delete from que_employee_third");
            log.info("deleted third  emp ser");
            jdbcTemplate.execute("delete from que_employee_fourth");
            log.info("deleted fourth  emp ser");
//            employeeRepository.deleteAll();
            log.info("deleted employee");
            jdbcTemplate.execute("delete from branch_services");
            log.info("branh services");
            jdbcTemplate.execute("delete from device_services");
            log.info("device services");
            jdbcTemplate.execute("delete from service_group_services");
            log.info("service group  services");
            serviceRepository.deleteAll();
            log.info("deleted service");
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }
    @Transactional
    public void deleteAssistanceReport() {
        try {
            TenantContext.setCurrentTenant("queberry");
            log.info("Deleting the records of Assistance Report bookings");
            jdbcTemplate.execute("delete from que_assistance_report_booking_ids");
            log.info("Deleting the records of Assistance Report");
            jdbcTemplate.execute("delete from que_assistance_report");
            log.info("Deleting sucessfully records from assistance report");
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }

    @Transactional
    public void updateEmpData() {
        try {
            List<Employee> employeeSet = employeeRepository.findAll();
            employeeSet.forEach(employee -> {
                log.info("empid:{}", employee.getId());
                employee.setForceAutoCall(false);
                employeeRepository.save(employee);
            });
            log.info("all employees updated");
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createIndex() {
        try {
            jdbcTemplate.execute("CREATE INDEX idx_pairedBy_type ON device (paired_by, type)");
            log.info("device index created");
            jdbcTemplate.execute("CREATE CLUSTERED INDEX idx_token_createdAt ON token (created_at DESC)");
            jdbcTemplate.execute("CREATE INDEX idx_token_branch_service ON token (created_at DESC, branch, service_id)");
            jdbcTemplate.execute("CREATE INDEX idx_token_createdAt_service_number_branch_status ON token (created_at DESC, service_id, number, branch, status)");
            log.info("token indexes created");

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createTIndex() {
        try {
            jdbcTemplate.execute("CREATE CLUSTERED INDEX idx_token_createdAt ON token (created_at DESC)");
            jdbcTemplate.execute("CREATE INDEX idx_token_branch_service ON token (created_at DESC, branch, service_id)");
            jdbcTemplate.execute("CREATE INDEX idx_token_createdAt_service_number_branch_status ON token (created_at DESC, service_id, number, branch, status)");
            log.info("token indexes created");

        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createQTIndex() {
        try {
            jdbcTemplate.execute("CREATE INDEX idx_qt_branch ON queued_token (branch)");
            jdbcTemplate.execute("CREATE INDEX idx_qt_token ON queued_token (token_id)");
            jdbcTemplate.execute("CREATE INDEX idx_qt_branch_service_priority_createdat ON queued_token (branch, service_id, priority, created_at)");
            log.info("queued token index created");
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createQCIndex() {
        try {
            jdbcTemplate.execute("CREATE UNIQUE INDEX idx_qc_counter ON QUEUED_COUNTER(counter_id);");
            jdbcTemplate.execute("CREATE INDEX idx_qc_branch_createdAt ON QUEUED_COUNTER(branch, created_at);");
            jdbcTemplate.execute("CREATE INDEX idx_qc_employee ON QUEUED_COUNTER(employee);");
            log.info("queued counter index created");
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createCTIndex() {
        try {
            jdbcTemplate.execute("CREATE UNIQUE INDEX idx_ct_token ON COUNTER_TOKEN(token_id);");
            jdbcTemplate.execute("CREATE INDEX idx_ct_counter_createdAt ON COUNTER_TOKEN(counter_id, created_at);");
            jdbcTemplate.execute("CREATE INDEX idx_ct_employee_createdAt ON COUNTER_TOKEN(employee_id,created_at);");
            log.info("counter token index created");
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void createSerIndex() {
        try {
            jdbcTemplate.execute("ALTER TABLE que_service DROP CONSTRAINT UK_puk2o9wjoup8sui6a0n8x9v75");
            jdbcTemplate.execute("ALTER TABLE que_service DROP CONSTRAINT UK_na19x9cm1sye1ri5usy2o0qwt");
            log.info("service token index dropped");
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting data: " + e.getMessage());
        }
    }
}


