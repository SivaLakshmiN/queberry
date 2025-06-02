package io.queberry.que.serviceTest;

import io.queberry.que.dto.AddServiceToServiceGroup;
import io.queberry.que.dto.ServiceDTO;
import io.queberry.que.dto.ServiceGroupDTO;
import io.queberry.que.dto.ServiceGroupRequest;
import io.queberry.que.entity.ServiceGroup;
import io.queberry.que.exception.DataNotFoundException;
import io.queberry.que.repository.ServiceGroupRepository;
import io.queberry.que.repository.ServiceRepository;
import io.queberry.que.service.impl.ServiceGroupServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ServiceGroupServiceImplTest {

    @Mock
    private ServiceGroupRepository serviceGroupRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceGroupServiceImpl serviceGroupService;

    @Test
    void testAddServicesToGroup_success() {
        String groupId = "group123";
        String serviceId1 = "service1";
        String serviceId2 = "service2";
        AddServiceToServiceGroup request = new AddServiceToServiceGroup();
        request.setServices(Set.of(serviceId1, serviceId2));

        ServiceGroup serviceGroup = Mockito.spy(new ServiceGroup());
//        serviceGroup.setId(groupId);
        serviceGroup.setServices(new HashSet<>());

        ServiceDTO svc1 = new ServiceDTO();
        svc1.setId(serviceId1);
        ServiceDTO svc2 = new ServiceDTO();
        svc2.setId(serviceId2);

        Mockito.when(serviceGroupRepository.findById(groupId)).thenReturn(Optional.of(serviceGroup));
//        Mockito.when(serviceRepository.findById(serviceId1)).thenReturn(Optional.of(svc1));
//        Mockito.when(serviceRepository.findById(serviceId2)).thenReturn(Optional.of(svc2));
        Mockito.when(serviceGroupRepository.save(any(ServiceGroup.class))).thenReturn(serviceGroup);

        ServiceGroup result = serviceGroupService.addServicesToGroup(groupId, request);

        assertThat(result).isNotNull();
        assertThat(result.getServices()).containsExactlyInAnyOrder(serviceId1, serviceId2);
        Mockito.verify(serviceGroup).clearServices();
        Mockito.verify(serviceGroup).setServices(Set.of(serviceId1, serviceId2));
        Mockito.verify(serviceGroupRepository).save(serviceGroup);
    }
    @Test
    void testGetAllServiceGroups() {
        ServiceGroup group1 = new ServiceGroup();
//        group1.setId("1");
        group1.setName("Group A");

        ServiceGroup group2 = new ServiceGroup();
//        group2.setId("2");
        group2.setName("Group B");

        Page<ServiceGroup> mockPage = new PageImpl<>(List.of(group1, group2));
        Mockito.when(serviceGroupRepository.findAll()).thenReturn((List<ServiceGroup>) mockPage);
        Page<ServiceGroup> result = serviceGroupService.getAllServiceGroups();
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting(ServiceGroup::getName)
                .containsExactlyInAnyOrder("Group A", "Group B");
        Mockito.verify(serviceGroupRepository).findAll();
    }
    @Test
    void testSaveServiceGroup_success() {
        ServiceGroup input = new ServiceGroup();
        input.setName("Support");
        input.setDescription("Customer support group");
        input.setDisplayName("Support Team");
//        input.setNames(Set.of("en:Support", "fr:Soutien"));
        input.setServices(Set.of("service1", "service2"));

        ServiceGroup saved = new ServiceGroup();
//        saved.setId("123");
        saved.setName(input.getName());
        saved.setDescription(input.getDescription());
        saved.setDisplayName(input.getDisplayName());
        saved.setNames(input.getNames());
        saved.setServices(input.getServices());

        Mockito.when(serviceGroupRepository.save(any(ServiceGroup.class))).thenReturn(saved);

        ServiceGroup result = serviceGroupService.save(input);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Support");
        Mockito.verify(serviceGroupRepository).save(any(ServiceGroup.class));
    }
    @Test
    void testEditServiceGroup_success() {
        String groupId = "group123";
        ServiceGroupRequest request = new ServiceGroupRequest();
        request.setDisplayName("Updated Display");
//        request.setNames(Set.of("en:Updated", "fr:Mis à jour"));
        request.setServices(Set.of("svc1", "svc2"));
        ServiceGroup existing = new ServiceGroup();
//        existing.setId(groupId);
        io.queberry.que.entity.Service svc1 = new io.queberry.que.entity.Service();
//        svc1.setId("svc1");

        io.queberry.que.entity.Service svc2 = new io.queberry.que.entity.Service();
//        svc2.setId("svc2");

        Set<io.queberry.que.entity.Service> serviceSet = Set.of(svc1, svc2);
        Mockito.when(serviceGroupRepository.findById(groupId)).thenReturn(Optional.of(existing));
//        Mockito.when(serviceRepository.findByIdIn(request.getServices())).thenReturn(serviceSet);
        Mockito.when(serviceGroupRepository.save(any(ServiceGroup.class))).thenAnswer(inv -> inv.getArgument(0));
        ServiceGroup result = serviceGroupService.edit(groupId, request);
        assertThat(result.getDisplayName()).isEqualTo("Updated Display");
//        assertThat(result.getNames()).contains("en:Updated", "fr:Mis à jour");
        assertThat(result.getServices()).hasSize(2);
        Mockito.verify(serviceGroupRepository).save(existing);
    }

    @Test
    void testEditServiceGroup_notFound() {
        String invalidId = "missing-id";

        Mockito.when(serviceGroupRepository.findById(invalidId)).thenReturn(Optional.empty());

        ServiceGroupRequest request = new ServiceGroupRequest();
        request.setDisplayName("Anything");

        assertThrows(DataNotFoundException.class, () -> {
            serviceGroupService.edit(invalidId, request);
        });

        Mockito.verify(serviceGroupRepository, Mockito.never()).save(any());
    }

}
