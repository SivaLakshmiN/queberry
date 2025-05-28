//package io.queberry.que.Repository;
//
//import io.queberry.que.Entity.Assistance;
//import io.queberry.que.Entity.Service;
//import io.queberry.que.Entity.Status;
//import io.queberry.que.Entity.Type;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;

//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//
//public interface AssistanceRepository extends JpaRepository<Assistance,String> {
//    Set<Assistance> findByStatus(Status status);
//    Set<Assistance> findByStatusAndType(Status status, Type type);
//    Set<Assistance> findByBranchAndStatusAndType(String b, Status status, Type type);
//    Set<Assistance> findByBranchAndServiceIdAndStatus(String branch, String service, Status status, Sort sort);
//    Set<Assistance> findByBranchAndServiceIdAndMobileAndStatusAndCreatedAtBetween(String branch, String service, String mobile, Status status,LocalDateTime from, LocalDateTime to, Sort sort);
//
//
//    //    @QueryHints(value = {
////            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
////            @QueryHint(name = "org.hibernate.cacheRegion", value = "TokenRefQueryCache")
////    })
//    @Query(value = "SELECT e FROM que_assistance e WHERE e.tokenRef = :tokenref")
//    Assistance findByTokenRef(@Param("tokenref") String tokenref);
//
//    //    @QueryHints(value = {
////            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
////            @QueryHint(name = "org.hibernate.cacheRegion", value = "AssistanceIdCache")
////    })
//    @Query(value = "SELECT e FROM que_assistance e WHERE e.id = :id")
//    Optional<Assistance> findAssistanceById(@Param("id") String id);
//    Set<Assistance> findByStatusAndBranch(Status status, String branchKey);
//    Set<Assistance> findByStatusInAndBranch(Set<Status> status, String branchKey);
//    Set<Assistance> findByStatusAndSessionsCounterId(Status status, String counter);
//
//    Page<Assistance> findByStatusAndSessionsCounterIdAndCreatedAtBetween(Status status, String counter, LocalDateTime from, LocalDateTime to,Pageable pageable);
//    Page<Assistance> findByStatusAndCreatedAtBetween(Status status, LocalDateTime from, LocalDateTime to, Pageable pageable);
//    Page<Assistance> findByCreatedAtBetweenAndBranchAndStatus(LocalDateTime from, LocalDateTime to, String branchKey, Status status,  Pageable pageable);
//    Page<Assistance> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
//    @Query("SELECT a FROM que_assistance a JOIN a.sessions s WHERE a.createdAt BETWEEN :from AND :to AND s.employee = :employee AND (:branch IS NULL OR a.branch = :branch)")
//    Set<Assistance> findByCreatedAtBetweenAndSessionsEmployeeAndOptionalBranch(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to,
//            @Param("employee") String employee,
//            @Param("branch") String branch);
//    Set<Assistance> findByCreatedAtBetweenAndSessionsEmployeeAndSessionsServiceIdIn(LocalDateTime start, LocalDateTime end, String employee, Set<String> sList);
//    Set<Assistance> findByCreatedAtBetweenAndSessionsEmployeeAndSessionsServiceId(LocalDateTime start, LocalDateTime end, String employee, String service);
//    Page<Assistance> findByCreatedAtBetweenAndSessionsEmployee(LocalDateTime start, LocalDateTime end, String employee,Pageable pageable);
//
//    Set<Assistance> findByBranchAndServiceIdAndStatus(String b,String service, Status status);
//    Set<Assistance> findByServiceInAndStatus(Set<Service> service, Status status);
//    Set<Assistance> findByCreatedAtBetweenAndStatusAndBranchAndServiceIdIn(LocalDateTime start, LocalDateTime end, Status status,  String branchKey, Set<String> service);
//    Set<Assistance> findByStatusAndCreatedAtBetweenAndSessionsTransferredToServiceIdInAndBranch(Status status, LocalDateTime start, LocalDateTime end, Set<String> service, String branchKey);
//    Set<Assistance> findByStatusAndCreatedAtBetweenAndSessionsTransferredToCounterIdAndBranch(Status status, LocalDateTime start, LocalDateTime end, String counter, String branch);
//    Set<Assistance> findByStatusAndCreatedAtBetweenAndSessionsTransferredToUserIdAndBranch(Status status, LocalDateTime start, LocalDateTime end, String employee, String branch);
//    Set<Assistance> findByCreatedAtBetweenAndStatusAndServiceIdIn(LocalDateTime start, LocalDateTime end, Status status, Set<String> service);
//    //Reporting
//    Set<Assistance> findByCreatedAtBetweenAndBranch(LocalDateTime start,LocalDateTime end, String branchKey, Sort sort);
//    List<Assistance> findByCreatedAtBetweenAndBranchAndStatusIn(LocalDateTime start,LocalDateTime end, String branchKey, Set<Status> s);
//
//    @Query(value = "SELECT * FROM que_assistance a WHERE a.created_at BETWEEN :from AND :to " +
//            "AND (COALESCE(:branch, NULL) IS NULL OR a.branch IN (:branch))", nativeQuery = true)
//    Set<Assistance> findByCreatedAtBetweenAndOptionalBranch(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to,
//            @Param("branch") List<String> branch);
//
//    @Query("SELECT a FROM que_assistance a JOIN a.sessions s WHERE a.createdAt BETWEEN :from AND :to AND s.service.id = :serviceId AND (COALESCE(:branch, NULL) IS NULL OR a.branch IN :branch)")
//    Set<Assistance> findByCreatedAtBetweenAndSessionsServiceIdAndOptionalBranch(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to,
//            @Param("serviceId") String serviceId,
//            @Param("branch") List<String> branch);
//
//    @Query("SELECT a FROM que_assistance a WHERE a.createdAt BETWEEN :from AND :to AND a.service.id = :serviceId AND (COALESCE(:branch, NULL) IS NULL OR a.branch IN :branch)")
//    Set<Assistance> findByCreatedAtBetweenAndServiceIdAndOptionalBranch(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to,
//            @Param("serviceId") String serviceId,
//            @Param("branch") List<String> branch);
//
//    @Query("SELECT a FROM que_assistance a JOIN a.sessions s WHERE a.createdAt BETWEEN :from AND :to AND s.subService.id = :subserviceId AND (COALESCE(:branch, NULL) IS NULL OR a.branch = :branch)")
//    Set<Assistance> findByCreatedAtBetweenAndSessionsSubTransactionIdAndOptionalBranch(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to,
//            @Param("subserviceId") String subserviceId,
//            @Param("branch") String branch);
//
//    @Query("SELECT a FROM que_assistance a JOIN a.sessions s WHERE a.createdAt BETWEEN :from AND :to AND s.counter.id = :counterId AND (COALESCE(:branch, NULL) IS NULL OR a.branch = :branch)")
//    Set<Assistance> findByCreatedAtBetweenAndSessionsCounterIdAndOptionalBranch(
//            @Param("from") LocalDateTime from,
//            @Param("to") LocalDateTime to,
//            @Param("counterId") String counterId,
//            @Param("branch") String branch);
//
//    Set<Assistance> findByBranchAndStatusAndServiceIdAndCreatedAtBetween(String b, Status status, String service, LocalDateTime from, LocalDateTime to);
//    Set<Assistance> findByStatusAndServiceAndCreatedAtBetween(Status status, Service service, LocalDateTime from, LocalDateTime to);
//    Set<Assistance> findByStatusAndServiceIdAndCreatedAtBetween(Status status, String service, LocalDateTime from, LocalDateTime to);
//    Set<Assistance> findByServiceAndCreatedAtBetween(Service service, LocalDateTime from, LocalDateTime to, Sort sort);
//    Set<Assistance> findByServiceIdAndCreatedAtBetween(String service, LocalDateTime from, LocalDateTime to, Sort sort);
//    Set<Assistance> findByBranchAndServiceIdAndCreatedAtBetween(String b,String service, LocalDateTime from, LocalDateTime to, Sort sort);
//    Set<Assistance> findByCreatedAtBetweenAndSessionsEmployeeAndSessionsStatus(LocalDateTime start, LocalDateTime end, String employee, Status s, Sort sort);
//    Optional<Assistance> findByCreatedAtBetweenAndSessionsOngoingAndSessionsEmployeeAndBranch(LocalDateTime start, LocalDateTime end,boolean t, String e, String b);
//    Set<Assistance> findByBranchAndStatusNotInAndCreatedAtBetween(String branchKey, Set<Status> status, LocalDateTime from, LocalDateTime to);
//    Set<Assistance> findByCreatedAtBetweenAndStatusInAndServiceId(LocalDateTime from, LocalDateTime to, Set<Status> status, String branchKey);
//}
//
