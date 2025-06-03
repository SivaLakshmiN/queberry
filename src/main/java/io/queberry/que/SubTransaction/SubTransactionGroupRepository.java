package io.queberry.que.SubTransaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubTransactionGroupRepository extends JpaRepository<SubTransactionGroup,String> {
    Page<SubTransactionGroup> findByRegion(String region,
                                           Pageable pageable);

    Page<SubTransactionGroup> findByRegionAndNameContainingIgnoreCase(String region, String s,
                                                                      Pageable pageable);
}
