package io.queberry.que.repository;

import io.queberry.que.entity.SharedSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedSequenceRepository extends JpaRepository<SharedSequence,String>{
}
