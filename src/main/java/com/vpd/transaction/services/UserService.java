package com.vpd.transaction.services;


import com.vpd.transaction.dto.requests.CreateAccountRequest;
import com.vpd.transaction.dto.requests.TransactionRequest;
import com.vpd.transaction.dto.requests.TransferRequest;
import com.vpd.transaction.dto.responses.CreateAccountResponse;
import com.vpd.transaction.dto.responses.TransactionResponse;
import com.vpd.transaction.dto.responses.TransferResponse;
import com.vpd.transaction.enums.AccountTypes;
import com.vpd.transaction.enums.TransactionType;
import com.vpd.transaction.exceptions.NotFoundException;
import com.vpd.transaction.models.Account;
import com.vpd.transaction.models.Transactions;
import com.vpd.transaction.models.User;
import com.vpd.transaction.repositories.AccountRepo;
import com.vpd.transaction.repositories.TransactionRepo;
import com.vpd.transaction.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
        ;
        return new User(user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getAccount());
    }


    public CreateAccountResponse createUserAccount(CreateAccountRequest request, Principal principal) {
        User user = userRepo.findByEmail(principal.getName());
        Account acc = Account.builder()
                .accountBalance(0L)
                .accountNumber(request.getMobileNumber())
                .accountType(AccountTypes.valueOf(request.getAccType()))
                .build();
        user.setAccount(acc);
        User updatedUser = userRepo.save(user);

        return CreateAccountResponse.builder()
                .accountName(String.format("%s %s", updatedUser.getFirstName(), updatedUser.getLastName()))
                .accountNumber(updatedUser.getAccount().getAccountNumber())
                .build();
    }

    public TransactionResponse creditAccount(TransactionRequest request) {
        return getTransactionResponse(request);

    }

    public TransactionResponse debitAccount(TransactionRequest request) {
        return getTransactionResponse(request);

    }



    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public TransferResponse transferToAccount(TransferRequest request, Principal principal) {
        Account recipientAccount = accountRepo.findAccountByAccountNumber(
                request.getRecipientAccount()
        );

       User user = userRepo.findByEmail(principal.getName());

       TransactionRequest debitRequest = TransactionRequest.builder()
               .accountNumber(user.getAccount().getAccountNumber())
               .transactionType(TransactionType.DEBIT.name())
               .amount(request.getAmount())
               .build();

       TransactionRequest creditRequest = TransactionRequest.builder()
                .accountNumber(recipientAccount.getAccountNumber())
                .transactionType(TransactionType.CREDIT.name())
                .amount(request.getAmount())
                .build();

        TransactionResponse debitResponse = this.debitAccount(debitRequest);
        TransactionResponse creditResponse = this.creditAccount(creditRequest);

        return TransferResponse.builder()
                .recipientAccount(creditResponse.getAccountNumber())
                .transactionId(creditResponse.getTransactionId())
                .transferredAmount(creditResponse.getAmount())
                .userName(debitResponse.getAccountName())
                .userAccountNumber(debitResponse.getAccountNumber())
                .userAccountBalance(debitResponse.getAccountBalance())
                .build();
    }

    @Transactional(rollbackFor = { SQLException.class },
            propagation = Propagation.REQUIRES_NEW)
    public TransactionResponse getTransactionResponse(TransactionRequest request) {
        Account account = accountRepo.findAccountByAccountNumber(request.getAccountNumber());

        Transactions transaction = Transactions.builder()
                .transactionType(TransactionType.valueOf(request.getTransactionType()))
                .Amount(request.getAmount())
                .account(account)
                .build();


        switch (TransactionType.valueOf(request.getTransactionType())){
            case CREDIT -> {
                transaction.getAccount().setAccountBalance(
                        transaction.getAccount().getAccountBalance() + request.getAmount()

                );
                break;
            }
            case DEBIT -> {
                transaction.getAccount().setAccountBalance(
                        transaction.getAccount().getAccountBalance() - request.getAmount()
                );
            }
        }

        Transactions userTransaction = transactionRepo.save(transaction);
        return TransactionResponse.builder()
                .accountBalance(userTransaction.getAccount().getAccountBalance())
                .accountName(userTransaction.getAccount().getUser().getFirstName() + " " +
                        userTransaction.getAccount().getUser().getLastName()
                )
                .transactionId(userTransaction.getId())
                .amount(userTransaction.getAmount())
                .transactionType(userTransaction.getTransactionType().name())
                .accountNumber(userTransaction.getAccount().getAccountNumber())
                .build();
    }
}