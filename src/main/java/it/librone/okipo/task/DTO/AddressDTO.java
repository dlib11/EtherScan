package it.librone.okipo.task.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AddressDTO {
    @NotEmpty
    @Pattern(regexp = "^0x[a-fA-F0-9]{40}$")
    @JsonProperty("address")
    private String Address;
}
