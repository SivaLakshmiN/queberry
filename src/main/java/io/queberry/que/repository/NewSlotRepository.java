package io.queberry.que.repository;

import io.queberry.que.entity.Branch;
import io.queberry.que.entity.NewSlot;
import io.queberry.que.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface NewSlotRepository extends JpaRepository<NewSlot, String> {

    @Query("SELECT e FROM NewSlot e WHERE e.branch = :branch AND e.toDate >= :inputDate")
    List<NewSlot> findByBranchIsAndInputDateBetweenFromDateAndToDate(@Param("branch") Branch branch, @Param("inputDate") LocalDate inputDate);

    Set<NewSlot> findByBranchAndServiceInAndToDateGreaterThanEqual(String b, Set<Service> s, LocalDate edate);

}
