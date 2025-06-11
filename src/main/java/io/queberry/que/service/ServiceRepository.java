package io.queberry.que.service;

import io.queberry.que.region.Region;
import io.queberry.que.sharedSequence.SharedSequence;
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
public interface ServiceRepository extends JpaRepository<Service, String> {

    Set<Service> findByActiveTrue(Sort sort);

    @Query("SELECT new io.queberry.que.service.ServiceRegionResponse(s.id, s.name) " +
            "FROM que_service s " +
            "WHERE s.active = true AND s.region = :regionId")
    Set<ServiceRegionResponse> findByIdAndActiveTrue(String regionId, Sort sort);

    Service findByName(String name);
//    Service findBySubServices(Service service);

//    @Override
//    @RestResource(exported = false)
//    void delete(Service entity);

    //    Service findBySubServices_Id(String id);
    List<String> findByIdIn(Set<String> ids);

    List<Service> findByIdInAndRegion(Set<String> ids, String regionId);

    List<Service> findAllByRegion(String region);

    Page<Service> findByRegion(Region region, Pageable pageable);

    Optional<Service> findByNameLike(String name);

    Page<Service> findByRegionAndNameContainingIgnoreCase(Region r, String s, Pageable p);

    Set<String> findBySubServiceGroup(String subTransactionGroup);

    Set<Service> findByActiveTrueAndVirtualServiceTrue();

    @Query("SELECT e FROM que_service e WHERE e.active = true AND (e.virtualService = false OR e.virtualService IS NULL)")
    Set<Service> findByActiveTrueAndVirtualServiceFalseOrVirtualServiceNull();

    Set<String> findBySharedSequence(SharedSequence sharedSequence);

    @Query("SELECT new io.queberry.que.service.ServiceDTO(s.id, s.name,s.displayName,s.sharedSequence) FROM que_service s")
    List<ServiceDTO> findAllService();

}
