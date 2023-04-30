package com.vpd.transaction.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionRequest {
        @NotNull
        String transactionType;
        @NotNull
        Long amount;
        @NotNull
        Long accountNumber;
}
