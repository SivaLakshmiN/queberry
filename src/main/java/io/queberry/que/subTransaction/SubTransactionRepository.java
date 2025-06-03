package io.queberry.que.subTransaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SubTransactionRepository extends JpaRepository<SubTransaction, String> {
    Page<SubTransaction> findByRegion(String region,
                                      Pageable pageable);
    Set<SubTransaction> findByRegion(String region, Sort sort);

    Page<SubTransaction> findByRegionAndNameContainingIgnoreCase(String region, String s, Pageable p);

}
