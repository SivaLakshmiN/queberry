package io.queberry.que.repository;

import io.queberry.que.entity.Assistance;
import io.queberry.que.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface AssistanceRepository extends JpaRepository<Assistance,String> {

    List<Assistance> findByCreatedAtBetweenAndBranchAndStatusIn(LocalDateTime start,LocalDateTime end, String branchKey, Set<Status> s);

}
