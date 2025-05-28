//package io.queberry.que.Repository;
//import io.queberry.que.Entity.Service;
//import io.queberry.que.Entity.Token;
//import io.queberry.que.Entity.TokenStatus;
//import jakarta.persistence.QueryHint;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.QueryHints;
//import org.springframework.data.repository.query.Param;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.Set;
//

///**
// * @author : Fahad Fazil
// * @since : 30/01/18
// */
//public interface TokenRepository extends JpaRepository<Token,String>{
//    Set<Token> findByIdIn(Set<String> ids);
//    @Query(value = "SELECT * FROM token WHERE id = :id", nativeQuery = true)
//    @QueryHints(value = {
//            @QueryHint(name = "org.hibernate.cacheable", value = "true"),
//            @QueryHint(name = "org.hibernate.cacheRegion", value = "TokenCache")
//    })
//    Optional<Token> findTokenById(@Param("id") String id);
//    Optional<Token> findByIdAndStatus( String id, TokenStatus status);
//    Set<Token> findByCreatedAtBetween(LocalDateTime from,LocalDateTime to);
//    Set<Token> findByCreatedAtBetweenAndServiceOrderByCreatedAtDesc(LocalDateTime from,LocalDateTime to,Service service);
//    //    Set<Token> findByCreatedAtBetweenAndBranchAndServiceOrderByCreatedAtDesc(LocalDateTime from,LocalDateTime to, String branchKey,Service service);
//    Set<Token> findByCreatedAtBetweenAndBranchAndServiceIdOrderByCreatedAtDesc(LocalDateTime from,LocalDateTime to, String branchKey,String service);
//    Optional<Token> findByServiceAndNumberAndStatusIn(Service service, Integer number, Set<TokenStatus> statuses);
//    Optional<Token> findByCreatedAtBetweenAndServiceAndNumberAndStatusIn(LocalDateTime from,LocalDateTime to,Service service, Integer number, Set<TokenStatus> status);
//    //    Optional<Token> findByCreatedAtBetweenAndServiceAndNumberAndBranchAndStatusIn(LocalDateTime from,LocalDateTime to,Service service,Integer number, String branchKey,Set<TokenStatus> status);
//    Optional<Token> findByCreatedAtBetweenAndServiceIdAndNumberAndBranchAndStatusIn(LocalDateTime from,LocalDateTime to,String service,Integer number, String branchKey,Set<TokenStatus> status);
//    Set<Token> findByCreatedAtBetweenAndBranchAndServiceInOrderByCreatedAtDesc(LocalDateTime from,LocalDateTime to, String branchKey,Set<Service> service);
//
//}
