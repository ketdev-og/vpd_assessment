package com.vpd.transaction.controllers;

import com.vpd.transaction.dto.requests.CreateAccountRequest;
import com.vpd.transaction.dto.requests.TransactionRequest;
import com.vpd.transaction.dto.requests.TransferRequest;
import com.vpd.transaction.dto.responses.CreateAccountResponse;
import com.vpd.transaction.dto.responses.Response;
import com.vpd.transaction.dto.responses.TransactionResponse;
import com.vpd.transaction.dto.responses.TransferResponse;
import com.vpd.transaction.enums.TransactionType;
import com.vpd.transaction.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/account/create")
    public ResponseEntity<Object> createAccount(@Valid @RequestBody CreateAccountRequest request, Principal principal) {

        try {
            CreateAccountResponse response = userService.createUserAccount(request, principal);
            return Response.generateResponse(
                    "account created successfully",
                    HttpStatus.OK,
                    response
            );
        } catch (DataAccessException e) {
            return Response.generateResponse(
                    e.getCause().getCause().getMessage(),
                    HttpStatus.MULTI_STATUS,
                    null
            );
        }

    }

    @PostMapping("/account/transact")
    public ResponseEntity<Object> fundAccount(@Valid @RequestBody TransactionRequest transactionRequest) {
        TransactionResponse response = null;
        try {
            switch (TransactionType.valueOf(transactionRequest.getTransactionType())){
                case CREDIT -> {
                    response = userService.creditAccount(transactionRequest);
                    break;
                }
                case DEBIT -> {
                    response = userService.debitAccount(transactionRequest);
                    break;
                }
            }

            return Response.generateResponse(
                    "account credited successfully",
                    HttpStatus.OK,
                    response
            );
        } catch (DataAccessException e) {
            return Response.generateResponse(
                    e.getCause().getCause().getMessage(),
                    HttpStatus.MULTI_STATUS,
                    null
            );
        }
    }

    @PostMapping("/account/transfer")
    public ResponseEntity<Object> transferToOtherAccount(
            @Valid
            @RequestBody
            TransferRequest transferRequest,
            Principal principal) {
        try {
            TransferResponse response = userService.transferToAccount(transferRequest, principal);
            return Response.generateResponse(
                    "account credited successfully",
                    HttpStatus.OK,
                    response
            );
        }catch (DataAccessException e){
            return Response.generateResponse(
                    e.getCause().getCause().getMessage(),
                    HttpStatus.MULTI_STATUS,
                    null
            );
        }
    }

}
