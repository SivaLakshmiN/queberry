package io.queberry.que.serviceTest;

import io.queberry.que.dto.ServiceResource;
import io.queberry.que.region.Region;
import io.queberry.que.region.RegionRepository;
import io.queberry.que.service.*;
import io.queberry.que.sharedSequence.SharedSequence;
import io.queberry.que.sharedSequence.SharedSequenceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceImplTest {

    @InjectMocks
    private ServiceService serviceService;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private SharedSequenceRepository sharedSequenceRepository;

    @Mock
    private ServiceEngine serviceEngine;

    private Service service;

    @BeforeEach
    void setup() {
        service = new Service();
        service.setId("s1");
        service.setName("Test Service");
        service.setActive(false);
    }

    @Test
    void testGetAllActiveServices() {
        Set<Service> services = Set.of(service);
        Mockito.when(serviceRepository.findByActiveTrue(Mockito.any())).thenReturn(services);

        Set<Service> result = serviceService.getAllActiveServices(true);

        assertThat(result).isNotEmpty().contains(service);
    }

    @Test
    void testGetRegionActiveServices() {
        Set<ServiceRegionResponse> responses = Set.of(new ServiceRegionResponse());
        Mockito.when(serviceRepository.findByIdAndActiveTrue(Mockito.eq("region1"), Mockito.any()))
                .thenReturn(responses);

        Set<ServiceRegionResponse> result = serviceService.getRegionActiveServices("region1");

        assertThat(result).hasSize(1);
    }

    @Test
    void testActivateService() {
        service.setActive(false);
        Mockito.when(serviceRepository.findById("s1")).thenReturn(Optional.of(service));
        Mockito.when(serviceRepository.save(Mockito.any())).thenReturn(service);

        Service result = serviceService.activate("s1");

        assertThat(result.isActive()).isTrue();
    }

    @Test
    void testDeactivateService() {
        service.setActive(true);
        Mockito.when(serviceRepository.findById("s1")).thenReturn(Optional.of(service));

        Service result = serviceService.deactivate("s1");

        Mockito.verify(serviceRepository).save(Mockito.any());
        assertThat(result).isNull();  // Because method returns null
    }

    @Test
    void testUpdateServiceSuccess() {
        ServiceResource resource = new ServiceResource();
        resource.setDisplayName("Updated Name");
        resource.setDescription("Updated Desc");
        resource.setActive(true);
        resource.setSubServiceGroup(Set.of("group1"));

        Region region = new Region();
        region.setName("name");
        resource.setRegion(region);

        SharedSequence seq = new SharedSequence();
        seq.setName("name");
        resource.setSharedSequence(seq);

        Mockito.when(serviceRepository.findById("s1")).thenReturn(Optional.of(service));
        Mockito.when(regionRepository.findById("r1")).thenReturn(Optional.of(new Region()));
        Mockito.when(sharedSequenceRepository.findById("ss1")).thenReturn(Optional.of(new SharedSequence()));
        Mockito.when(serviceRepository.save(Mockito.any())).thenAnswer(invocation -> invocation.getArgument(0));

        Service updated = serviceService.updateService("s1", resource);

        assertThat(updated.getDisplayName()).isEqualTo("Updated Name");
        assertThat(updated.getSubServiceGroup()).contains("group1");
    }

    @Test
    void testUpdateServiceThrowsWhenServiceNotFound() {
        Mockito.when(serviceRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceService.updateService("invalid", new ServiceResource()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Service not found");
    }

    @Test
    void testSubServices() {
        Mockito.when(serviceRepository.findById("s1")).thenReturn(Optional.of(service));
        Mockito.when(serviceEngine.manageSubServices(Mockito.any(), Mockito.any()))
                .thenReturn(service);

        Service result = serviceService.subServices("s1", Set.of("sub1", "sub2"));

        assertThat(result).isNotNull();
        Mockito.verify(serviceEngine).manageSubServices(service, Set.of("sub1", "sub2"));
    }

    @Test
    void testSubServicesThrowsIfServiceNotFound() {
        Mockito.when(serviceRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceService.subServices("invalid", Set.of()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Service not found");
    }
}

