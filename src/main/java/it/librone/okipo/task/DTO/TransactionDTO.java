package it.librone.okipo.task.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "address",
        "blockNumber",
        "timeStamp",
        "hash",
        "from",
        "to",
        "value",
        "gasUsed"
})
public class TransactionDTO{

    @JsonProperty("address")
    private String Address;

    @JsonProperty("blockNumber")
    private Long blockNumber;

    @JsonProperty("timeStamp")
    private Long timeStamp;

    @JsonProperty("hash")
    private String transactionHash;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("value")
    private Double value;

    @JsonProperty("gasUsed")
    private Double gas;


}
