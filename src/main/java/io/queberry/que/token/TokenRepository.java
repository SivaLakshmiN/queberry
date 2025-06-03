package io.queberry.que.token;

import io.queberry.que.subTransaction.SubTransactionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<SubTransactionGroup,String> {
}
