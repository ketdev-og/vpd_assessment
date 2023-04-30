package com.vpd.transaction.dto.requests;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterRequest {

    @Email
    @NotNull
    String email;

    @NotNull
    String password;

    String firstName;

    String lastName;


}
