package io.queberry.que.Token;

import io.queberry.que.SubTransaction.SubTransactionGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<SubTransactionGroup,String> {
}
