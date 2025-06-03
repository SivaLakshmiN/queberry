package io.queberry.que.mapper;

import io.queberry.que.SubTransaction.SubTransactionDTO;
import io.queberry.que.SubTransaction.SubTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface SubTransactionMapper {

    SubTransaction dtoToEntity(SubTransactionDTO subTransactionDTO);
    SubTransactionDTO entityToDto(SubTransaction subTransaction);
    List<SubTransactionDTO> mapList(List<SubTransaction> subTransactions);
}
