package io.queberry.que.Repository;

import io.queberry.que.Entity.Branch;
import io.queberry.que.Entity.Counter;
import io.queberry.que.Entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CounterRepository extends JpaRepository<Counter,String>{
    Counter findByName(String name);
    Page<Counter> findByBranchAndCodeContainingIgnoreCase(Branch b,String str, Pageable p);
    Set<Counter> findByActiveTrue(Sort sort);
    Set<Counter> findByBranchAndActiveTrue(Branch b, Sort sort);
    List<Counter> findByBranchIdAndInUse(String b, boolean t);
    Set<Counter> findByBranchAndInUseAndFirstIsInOrSecondIsInOrThirdIsInOrFourthIsIn(Branch b, boolean t, Set<Service> s1, Set<Service> s2, Set<Service> s3, Set<Service> s4);
    Set<Counter> findByBranchIdAndInUseAndFirst_IdOrSecond_IdOrThird_IdOrFourth_Id(String b, boolean t, String s1, String s2, String s3, String s4);
    @Override
    @RestResource(exported = false)
    void delete(Counter entity);
    List<Counter> findByIdIn(Set<String> ids);
    List<Counter> findByIdInAndBranch(Set<String> ids, Branch b);
    List<Counter> findByIdInAndBranchId(Set<String> ids, String b);
    List<Counter> findAllByBranch(Branch b);
    List<Counter> findAllByBranchId(String b);
    Page<Counter> findAllByBranch(Branch b, Pageable pageable);
    Page<Counter> findAllByBranchId(String b, Pageable pageable);
    Set<Counter> findAllByBranchIn(Set<Branch> b);
    Set<Counter> findAllByIdNotInAndBranchAndActiveIsTrueAndInUseFalse(Set<String> ids, Branch b, Sort sort);
    Set<Counter> findAllByIdNotInAndBranchIdAndActiveIsTrueAndInUseFalse(Set<String> ids, String b, Sort sort);
//    @Query("SELECT new io.queberry.que.counter.CounterDTO(" +
//            "c.id, c.name, c.displayName, c.description, c.code, c.active, c.inUse, " +
//            "c.colorCode, c.panelNumber, c.presentation, " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.first s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.second s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.third s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.fourth s)) " +
//            "FROM que_counter c " +
//            "WHERE c.id not in :ids And c.branch.id = :branch AND c.inUse = false AND c.active = true order by c.name")
//    Collection<CounterDTO> findByNotInIdAndBranchIdAndInUseAndActive(@Param("ids") Set<String> ids, @Param("branch") String branch);

//    @Query("SELECT new io.queberry.que.counter.CounterDTO(" +
//            "c.id, c.name, c.displayName, c.description, c.code, c.active, c.inUse, " +
//            "c.colorCode, c.panelNumber, c.presentation, " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.first s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.first s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.first s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.first s)) " +
    ////            "(SELECT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.second s), " +
    ////            "(SELECT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.third s), " +
    ////            "(SELECT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.fourth s)) " +
//            "FROM que_counter c " +
//            "WHERE c.id NOT IN (:ids) AND c.branch.id = :branch AND c.inUse = false AND c.active = true " +
//            "ORDER BY c.name")
//    List<CounterDTO> findByNotInIdAndBranchIdAndInUseAndActive(@Param("ids") Set<String> ids, @Param("branch") String branch);

//    @Query("SELECT new io.queberry.que.counter.CounterDTO(" +
//            "c.id, c.name, c.displayName, c.description, c.code, c.active, c.inUse, " +
//            "c.colorCode, c.panelNumber, c.presentation, " +
//            "(SELECT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.first s), " +
//            "(SELECT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.second s), " +
//            "(SELECT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.third s), " +
//            "(SELECT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.fourth s)) " +
//            "FROM que_counter c " +
//            "WHERE c.id NOT IN :ids " +
//            "AND c.branch.id = :branch " +
//            "AND c.inUse = false " +
//            "AND c.active = true " +
//            "ORDER BY c.name")
//    List<CounterDTO> findAvailableCounters(
//            @Param("ids") Set<String> ids,
//            @Param("branch") Long branchId);
//

    Set<Counter> findAllByActiveIsTrueAndBranchAndInUseFalse(Branch b,Sort sort);
    Set<Counter> findAllByActiveIsTrueAndBranchIdAndInUseFalse(String b,Sort sort);

//    @Query("SELECT new io.queberry.que.counter.CounterDTO(" +
//            "c.id, c.name, c.displayName, c.description, c.code, c.active, c.inUse, " +
//            "c.colorCode, c.panelNumber, c.presentation, " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.first s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.second s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.third s), " +
//            "(SELECT DISTINCT new io.queberry.que.service.ServiceDTO(s.id, s.name) FROM c.fourth s)) " +
//            "FROM que_counter c " +
//            "WHERE c.branch.id = :branch AND c.inUse = false AND c.active = true order by c.name")
//    Collection<CounterDTO> findByBranchIdAndInUseAndActive(@Param("branch") String branch);


    @Query(value = "SELECT * FROM que_counter WHERE id = :id", nativeQuery = true)
    Optional<Counter> findCounterById(@Param("id") String id);

    //    Set<Counter> findAllByActiveIsTrueAndInUseFalse(Sort sort);
    //    Set<Counter> findByIdNotInAndInUseIsFalseAndActiveIsTrue(Set<String> ids, Sort sort);
    //    Set<Counter> findByIdNotIn(Set<String> ids, Sort sort);
    //    Set<Counter> findByBranchIn(Set<Branch> b);
    //    Set<Counter> findAllByInUseIsTrue();
    //    Set<Counter> findAllByActiveIsFalse();
    //    Collection<? extends String> findByIdAndInUseIsFalseAndActiveIsTrue();
    //    Set<Counter> findByServices(@Param("service") Service service);
    //    Set<Counter> findByBranchAndFirstIsInOrSecondIsInOrThirdIsInOrFourthIsIn(Branch b, Set<Service> s1, Set<Service> s2, Set<Service> s3, Set<Service> s4);
    //    Set<Counter> findAllByIdNotInAndActiveIsTrueAndInUseFalse(Set<String> ids, Sort sort);
}

