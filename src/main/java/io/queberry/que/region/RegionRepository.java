package io.queberry.que.region;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RegionRepository extends JpaRepository<Region,String> {

    List<Region> findByIdIn(Set<String> ids);
    Optional<Region> findByName(String name);
    Page<Region> findByNameContainingIgnoreCase(String s, Pageable p);
}
