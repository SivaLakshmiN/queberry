package io.queberry.que.serviceTest;

import io.queberry.que.entity.Service;
import io.queberry.que.enums.InputType;
import io.queberry.que.service.ServiceRepository;
import io.queberry.que.subTransaction.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SubTransactionServiceImplTest {

    @Mock
    private SubTransactionRepository subTransactionRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private SubTransactionGroupRepository subTransactionGroupRepository;

    @InjectMocks
    private SubTransactionServiceImpl subTransactionService;

    @Test
    void testGetSubTransactionsByRegion() {
        String regionId = "north";
        Pageable pageable = PageRequest.of(0, 10);
        SubTransaction transaction = new SubTransaction();
//        transaction.setId(1L);
        transaction.setName("Sample Txn");
        transaction.setRegion(regionId);

        Page<SubTransaction> page = new PageImpl<>(Collections.singletonList(transaction));
        when(subTransactionRepository.findByRegion(regionId, pageable)).thenReturn(page);

        Page<SubTransactionDTO> result = subTransactionService.getSubTransactionsByRegion(regionId, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Sample Txn");
    }
    @Test
    void testGetAllSubTransactionsByRegion() {
        String regionId = "east";
        SubTransaction transaction1 = new SubTransaction();
//        transaction1.setId(1L);
        transaction1.setName("Alpha");
        transaction1.setRegion(regionId);

        SubTransaction transaction2 = new SubTransaction();
//        transaction2.setId(2L);
        transaction2.setName("Beta");
        transaction2.setRegion(regionId);

        Set<SubTransaction> mockSet = new HashSet<>();
        mockSet.add(transaction1);
        mockSet.add(transaction2);

        when(subTransactionRepository.findByRegion(regionId, Sort.by(Sort.Order.asc("name"))))
                .thenReturn(mockSet);

        Set<SubTransactionDTO> result = subTransactionService.getAllSubTransactionsByRegion(regionId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        Set<String> names = result.stream().map(SubTransactionDTO::getName).collect(Collectors.toSet());
        assertThat(names).containsExactlyInAnyOrder("Alpha", "Beta");
    }
    @Test
    void testGetFilterSubTransaction() {
        String region = "west";
        String name = "bill";
        Pageable pageable = PageRequest.of(0, 5);

        SubTransaction tx = new SubTransaction();
//        tx.setId(100L);
        tx.setName("Billing Inquiry");
        tx.setRegion(region);

        Page<SubTransaction> page = new PageImpl<>(Collections.singletonList(tx), pageable, 1);

        when(subTransactionRepository.findByRegionAndNameContainingIgnoreCase(region, name, pageable))
                .thenReturn(page);

        Page<SubTransactionDTO> result = subTransactionService.getFilterSubTransaction(region, name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Billing Inquiry");
    }
    @Test
    void testSave() {
        SubTransaction subTransaction = new SubTransaction();
//        subTransaction.setId(1L);
        subTransaction.setName("Test SubTx");

        when(subTransactionRepository.save(subTransaction)).thenReturn(subTransaction);

        SubTransaction result = subTransactionService.save(subTransaction);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test SubTx");
    }
    @Test
    void testEdit_SubTransactionExists_ShouldUpdateAndReturn() {
        String id = "123";
        SubTransaction existing = new SubTransaction();
//        existing.setId(123L);
        existing.setName("Old Name");

        SubTransaction updateRequest = new SubTransaction();
        updateRequest.setName("New Name");
        updateRequest.setDisplayName("Display");
        updateRequest.setInput(true);
        updateRequest.setInputType(InputType.DROPDOWN);
        updateRequest.setSelectionValues("A,B,C");
        updateRequest.setActive(true);
        updateRequest.setRegion("East");
        updateRequest.setSecondaryName("Secondary");

        when(subTransactionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(subTransactionRepository.save(existing)).thenReturn(existing);

        SubTransaction result = subTransactionService.edit(id, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getDisplayName()).isEqualTo("Display");
        assertThat(result.getInputType()).isEqualTo("Text");
        assertThat(result.getRegion()).isEqualTo("East");
        assertThat(result.getSecondaryName()).isEqualTo("Secondary");
    }
    @Test
    void testGetSubTransactionGroupsByRegion() {
        String region = "east";
        Pageable pageable = PageRequest.of(0, 10);
        SubTransactionGroup group = new SubTransactionGroup();
//        group.setId(1L);
        group.setName("Group A");
        group.setRegion(region);

        Page<SubTransactionGroup> mockPage = new PageImpl<>(Collections.singletonList(group));
        when(subTransactionGroupRepository.findByRegion(region, pageable)).thenReturn(mockPage);

        Page<SubTransactionGroupDTO> result = subTransactionService.getSubTransactionGroupsByRegion(region, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Group A");
    }

    @Test
    void testFilterSubTransactionGroupsByRegionAndName() {
        String region = "north";
        String name = "finance";
        Pageable pageable = PageRequest.of(0, 10);
        SubTransactionGroup group = new SubTransactionGroup();
//        group.setId(2L);
        group.setName("Finance Ops");
        group.setRegion(region);

        Page<SubTransactionGroup> mockPage = new PageImpl<>(Collections.singletonList(group));
        when(subTransactionGroupRepository.findByRegionAndNameContainingIgnoreCase(region, name, pageable))
                .thenReturn(mockPage);

        Page<SubTransactionGroupDTO> result = subTransactionService.filterSubTransactionGroupsByRegionAndName(region, name, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).containsIgnoringCase("finance");
    }
    @Test
    void testSaveSubTransactionGroup() {
        SubTransactionGroupReq req = new SubTransactionGroupReq();
        req.setName("Group A");
        req.setDisplayName("Group A Display");
        req.setSecondaryName("G-A");
        req.setRegion("South");
        req.setActive(true);
//        req.setSubTransactions(List.of(1L, 2L));

        List<SubTransaction> mockSubTxs = Arrays.asList(new SubTransaction(), new SubTransaction());
        when(subTransactionRepository.findAllById(req.getSubTransactions())).thenReturn(mockSubTxs);

        SubTransactionGroup savedGroup = new SubTransactionGroup();
//        savedGroup.setId(1L);
        savedGroup.setName(req.getName());

        when(subTransactionGroupRepository.save(any(SubTransactionGroup.class))).thenReturn(savedGroup);

        SubTransactionGroup result = subTransactionService.saveSubTransactionGroup(req);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Group A");
    }

    @Test
    void testEditSubTransactionGroup() {
        String id = "group-1";

        SubTransactionGroupReq req = new SubTransactionGroupReq();
        req.setName("Edited Name");
        req.setDisplayName("Edited Display");
        req.setSecondaryName("E-N");
        req.setRegion("East");
        req.setActive(false);
//        req.setSubTransactions(Set.of(3L, 4L));

        SubTransactionGroup existing = new SubTransactionGroup();
//        existing.setId(1L);
        existing.setName("Old Name");

        when(subTransactionGroupRepository.findById(id)).thenReturn(Optional.of(existing));
        when(subTransactionRepository.findAllById(req.getSubTransactions())).thenReturn(Collections.emptyList());
        when(subTransactionGroupRepository.save(any(SubTransactionGroup.class))).thenReturn(existing);

        SubTransactionGroup result = subTransactionService.editSubTransactionGroup(id, req);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Edited Name");
    }

    @Test
    void testAssignSubTransactionGroupToServices() {
        String groupId = "group-1";
        Set<String> newServiceIds = Set.of("s1", "s2");

        SubTransactionGroup group = new SubTransactionGroup();
//        group.setId(1L);
        group.setName("Group One");

        when(subTransactionGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(serviceRepository.findBySubServiceGroup(String.valueOf(group))).thenReturn(Set.of("s1", "s3"));

        Service service1 = new Service();
        service1.setId("s1");

        Service service2 = new Service();
        service2.setId("s2");

        Service service3 = new Service();
        service3.setId("s3");

        when(serviceRepository.findById("s1")).thenReturn(Optional.of(service1));
        when(serviceRepository.findById("s2")).thenReturn(Optional.of(service2));
        when(serviceRepository.findById("s3")).thenReturn(Optional.of(service3));

        subTransactionService.assignSubTransactionGroupToServices(groupId, newServiceIds);

        verify(serviceRepository).save(service2); // newly assigned
        verify(serviceRepository).save(service3); // removed
        verify(serviceRepository, times(2)).save(any(Service.class));
    }
}
