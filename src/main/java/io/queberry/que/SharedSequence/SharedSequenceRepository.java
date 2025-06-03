package io.queberry.que.SharedSequence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedSequenceRepository extends JpaRepository<SharedSequence,String>{
}
