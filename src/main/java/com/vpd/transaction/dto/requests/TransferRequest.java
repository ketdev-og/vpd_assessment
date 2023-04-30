package com.vpd.transaction.dto.requests;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TransferRequest {
    @NotNull
    Long recipientAccount;
    @NotNull
    Long amount;
    @NotNull
    String transactionType;
}
