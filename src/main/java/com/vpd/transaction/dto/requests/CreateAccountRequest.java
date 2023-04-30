package com.vpd.transaction.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
public class CreateAccountRequest {
    @NotNull
    Long mobileNumber;
    @NotNull
    String accType;
}
