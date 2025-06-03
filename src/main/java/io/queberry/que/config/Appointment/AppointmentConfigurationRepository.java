package io.queberry.que.config.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentConfigurationRepository extends JpaRepository<AppointmentConfiguration,String> {
    AppointmentConfiguration findByBranchIsNull();
    Optional<AppointmentConfiguration> findByBranch(String b);

//    Set<AppointmentConfiguration> findByBranchRegionAndVirtualModeAndBranchActiveTrue(Region region, boolean v);
//    Set<AppointmentConfiguration> findByBranchRegionAndF2fModeAndBranchActiveTrue(Region region, boolean v);
}
