package io.queberry.que.config.Break;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BreakConfigurationRepository extends JpaRepository<BreakConfiguration,String> {
}
