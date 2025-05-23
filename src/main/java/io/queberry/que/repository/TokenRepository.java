package io.queberry.que.repository;

import io.queberry.que.entity.SubTransactionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<SubTransactionGroup,String> {
}
