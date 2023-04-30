package com.vpd.transaction.dto.responses;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TransactionResponse {
    Long accountNumber;
    String accountName;
    UUID transactionId;
    String transactionType;
    Long amount;
    Long accountBalance;
}
