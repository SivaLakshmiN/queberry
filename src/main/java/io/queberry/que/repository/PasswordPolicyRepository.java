package io.queberry.que.repository;

import io.queberry.que.entity.PasswordPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, String> {
}
