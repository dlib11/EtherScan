package it.librone.okipo.task.Utility;

import it.librone.okipo.task.DTO.TransactionDTO;
import it.librone.okipo.task.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// NB se combini Lombok con Mapper occhio al file pom.xml!!!
@Mapper(componentModel = "spring")
public interface MapperTransactionDTO {

    @Mapping(source = "address.ethAddress", target = "Address")
    @Mapping(source = "blockNumber", target = "blockNumber")
    @Mapping(source = "timeStamp", target = "timeStamp")
    @Mapping(source = "transactionHash", target = "transactionHash")
    @Mapping(source = "from", target = "from")
    @Mapping(source = "to", target = "to")
    @Mapping(source = "value", target = "value")
    @Mapping(source = "gas", target = "gas")
    TransactionDTO toDTO(Transaction transaction);
}