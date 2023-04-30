package com.vpd.transaction.dto.responses;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class RegisterResponse {
    String userEmail;
    String firstName;
    String lastName;
}
