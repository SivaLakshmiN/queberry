package io.queberry.que.service;

import io.queberry.que.dto.SubTransactionDTO;
import io.queberry.que.dto.SubTransactionGroupDTO;
import io.queberry.que.dto.SubTransactionGroupReq;
import io.queberry.que.entity.SubTransaction;
import io.queberry.que.entity.SubTransactionGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface SubTransactionService {

    Page<SubTransactionDTO> getSubTransactionsByRegion(String regionId, Pageable pageable);
    Set<SubTransactionDTO> getAllSubTransactionsByRegion(String regionId);
    Page<SubTransactionDTO> getFilterSubTransaction(String region, String name, Pageable pageable);
    SubTransaction save(SubTransaction subTransaction);
    SubTransaction edit(String id, SubTransaction editSubTransaction);
    Page<SubTransactionGroupDTO> getSubTransactionGroupsByRegion(String region, Pageable pageable);
    Page<SubTransactionGroupDTO> filterSubTransactionGroupsByRegionAndName(String region, String name, Pageable pageable);
    SubTransactionGroup saveSubTransactionGroup(SubTransactionGroupReq stgReq);
    SubTransactionGroup editSubTransactionGroup(String id, SubTransactionGroupReq editReq);
    void assignSubTransactionGroupToServices(String subTransactionGroupId, Set  <String> serviceIds);


}
