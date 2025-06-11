package io.queberry.que.queue;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QueueCounterRepository extends JpaRepository<QueueCounter,String>{
    List<QueueCounter> findByCounterId(String id);
    List<QueueCounter> findAll(Sort sort);
    List<QueueCounter> findAllByBranch(String branch,Sort sort);
    Optional<QueueCounter> findByEmployee(String s);



}
