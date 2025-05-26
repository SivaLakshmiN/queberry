package io.queberry.que.Repository;

import io.queberry.que.Entity.PasswordPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, String> {
}
