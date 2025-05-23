package io.queberry.que.repository;

import io.queberry.que.entity.AuditLogs;
import io.queberry.que.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogsRepository extends JpaRepository<AuditLogs,String> {
}
