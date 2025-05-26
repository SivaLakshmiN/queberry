package io.queberry.que.Repository;

import io.queberry.que.Entity.AuditLogs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogsRepository extends JpaRepository<AuditLogs,String> {

}
