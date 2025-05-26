package io.queberry.que.repository;

import io.queberry.que.entity.Region;
import io.queberry.que.entity.Service;
import io.queberry.que.entity.SharedSequence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ServiceRepository extends JpaRepository<Service,String> {

    Set<Service> findByActiveTrue(Sort sort);
    Set<Service> findByIdAndActiveTrue(String id,Sort sort);

    Service findByName(String name);
//    Service findBySubServices(Service service);

//    @Override
//    @RestResource(exported = false)
//    void delete(Service entity);

    //    Service findBySubServices_Id(String id);
    List<String> findByIdIn(Set<String> ids);
    List<Service> findByIdInAndRegion(Set<String> ids, Region regionId);
    List<Service> findAllByRegion(Region region);

    Page<Service> findByRegion(Region region, Pageable pageable);
    Optional<Service> findByNameLike(String name);

    Page<Service> findByRegionAndNameContainingIgnoreCase(Region r, String s, Pageable p);

    Set<String> findBySubServiceGroup(String subTransactionGroup);
    Set<Service> findByActiveTrueAndVirtualServiceTrue();
    @Query("SELECT e FROM que_service e WHERE e.active = true AND (e.virtualService = false OR e.virtualService IS NULL)")
    Set<Service> findByActiveTrueAndVirtualServiceFalseOrVirtualServiceNull();
    Set<String> findBySharedSequence(SharedSequence sharedSequence);

}
