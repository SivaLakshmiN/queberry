package io.queberry.que.Repository;

import io.queberry.que.Entity.PasswordManagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PasswordManagementRepository extends JpaRepository<PasswordManagement, String> {
    PasswordManagement findByUsernameIgnoreCase(String username);
    Optional<PasswordManagement> findFirstByUsernameIgnoreCaseOrderByIdDesc(String username);
    Optional<PasswordManagement> findTop1ByUsernameIgnoreCaseOrderByIdDesc(String username);

}
