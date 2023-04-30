package com.vpd.transaction.dto.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateAccountResponse {
    String accountName;
    Long accountNumber;
}
