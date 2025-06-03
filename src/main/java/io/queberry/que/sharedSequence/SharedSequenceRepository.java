package io.queberry.que.sharedSequence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedSequenceRepository extends JpaRepository<SharedSequence,String>{
}
