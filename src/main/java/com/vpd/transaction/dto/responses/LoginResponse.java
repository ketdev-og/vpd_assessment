package com.vpd.transaction.dto.responses;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String authEmail;
    private String authToken;
}
