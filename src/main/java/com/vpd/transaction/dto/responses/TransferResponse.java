package com.vpd.transaction.dto.responses;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TransferResponse {
    UUID transactionId;
    String recipientName;
    Long recipientAccount;
    Long transferredAmount;
    Long userAccountBalance;
    Long userAccountNumber;
    String userName;
}
