package io.queberry.que.mapper;

import io.queberry.que.branch.BranchDTO;
import io.queberry.que.branch.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface BranchMapper {

    Branch dtoToEntity(BranchDTO branchDTO);

    BranchDTO entityToDto(Branch branch);

    List<BranchDTO> mapList(List<Branch> branches);
}
