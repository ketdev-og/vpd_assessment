package com.vpd.transaction.Service;


import com.vpd.transaction.dto.requests.CreateAccountRequest;
import com.vpd.transaction.dto.requests.TransactionRequest;
import com.vpd.transaction.dto.responses.CreateAccountResponse;
import com.vpd.transaction.dto.responses.TransactionResponse;
import com.vpd.transaction.enums.AccountTypes;
import com.vpd.transaction.enums.TransactionType;
import com.vpd.transaction.models.Account;
import com.vpd.transaction.models.Transactions;
import com.vpd.transaction.models.User;
import com.vpd.transaction.repositories.AccountRepo;
import com.vpd.transaction.repositories.TransactionRepo;
import com.vpd.transaction.repositories.UserRepo;
import com.vpd.transaction.services.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock(strictness = Mock.Strictness.LENIENT)
    private UserRepo userRepo;
    @Mock
    private AccountRepo accountRepo;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private TransactionRepo transactionRepo;
    @Mock
    Principal principal;

    @InjectMocks
    private UserService userService;

    private User user;
    private Account account;
    private Transactions creditTransactions, debitTransactions;
    private CreateAccountRequest accountRequest;

    private TransactionRequest transactionRequest;


    @BeforeEach()
    public void setup() {
        user = User.builder()
                .id(UUID.randomUUID())
                .firstName("ketdev")
                .lastName("boyos")
                .email("chylau13@gmail.com")
                .password("123456")
                .account(account)
                .build();


        account = Account.builder()
                .user(user)
                .accountType(AccountTypes.TIER_THREE)
                .accountNumber(12345678L)
                .id(UUID.randomUUID())
                .accountBalance(5000L)
                .transactions(Set.of(Transactions.builder()
                                .account(account)
                                .transactionType(TransactionType.CREDIT)
                                .Amount(100L)
                                .build(),
                        Transactions.builder()
                                .account(account)
                                .transactionType(TransactionType.DEBIT)
                                .Amount(200L)
                                .build()
                ))
                .build();

        creditTransactions = Transactions.builder()
                .account(account)
                .transactionType(TransactionType.CREDIT)
                .Amount(100L)
                .build();


        debitTransactions = Transactions.builder()
                .account(account)
                .transactionType(TransactionType.DEBIT)
                .Amount(200L)
                .build();


        accountRequest = CreateAccountRequest.builder()
                .mobileNumber(account.getAccountNumber())
                .accType(account.getAccountType().toString())
                .build();

    }

    @DisplayName("unit test to create account")
    @Test
    @WithMockUser(username = "chylau13@gmail.com")
    public void whenAccountCreated_returnAccountNotNull_And_AccountBelongstoUser() {
        given(accountRepo.findAccountByAccountNumber(account.getAccountNumber()))
                .willReturn(account);
        given(userRepo.findByEmail(principal.getName())).willReturn(user);
        when(userRepo.save(user)).thenReturn(user);

        CreateAccountResponse createAccountResponse = userService.createUserAccount(accountRequest, principal);
        Account userAccount = accountRepo.findAccountByAccountNumber(createAccountResponse.getAccountNumber());

        assertThat(createAccountResponse).isNotNull();
        assertThat(userAccount).isNotNull();
        assertThat(userAccount.getUser()).isInstanceOf(User.class);
        assertThat(userAccount.getUser()).isNotNull();

    }

    @DisplayName("increment on account balance")
    @Test
    public void whenAccountCredited_accountBalance_shouldIncrease() {
        given(userRepo.findByEmail(principal.getName())).willReturn(user);

        given(accountRepo.findAccountByAccountNumber(account.getAccountNumber()))
                .willReturn(account);
        given(transactionRepo.save(creditTransactions)).willReturn(creditTransactions);



        TransactionResponse transactionResponse = userService.creditAccount(
                TransactionRequest.builder()
                        .amount(creditTransactions.getAmount())
                        .transactionType(creditTransactions.getTransactionType().toString())
                        .accountNumber(creditTransactions.getAccount().getAccountNumber())
                        .build());


        Assertions.assertEquals(transactionResponse.getTransactionType(),
                String.valueOf(creditTransactions.getTransactionType()));
        assertThat(transactionResponse).isNotNull();
        assertThat(transactionResponse.getAccountBalance())
                .isGreaterThan(account.getAccountBalance()-creditTransactions.getAmount());

    }

    @DisplayName("decrement on account balance")
    @Test
    public void whenAccountDebited_accountBalance_shouldIncrease() {
        given(userRepo.findByEmail(principal.getName())).willReturn(user);

        given(accountRepo.findAccountByAccountNumber(account.getAccountNumber()))
                .willReturn(account);
        given(transactionRepo.save(debitTransactions)).willReturn(debitTransactions);



        TransactionResponse transactionResponse = userService.debitAccount(
                TransactionRequest.builder()
                        .amount(debitTransactions.getAmount())
                        .transactionType(debitTransactions.getTransactionType().toString())
                        .accountNumber(debitTransactions.getAccount().getAccountNumber())
                .build());


        Assertions.assertEquals(transactionResponse.getTransactionType(),
                String.valueOf(debitTransactions.getTransactionType()));
        assertThat(transactionResponse).isNotNull();
        assertThat(transactionResponse.getAccountBalance())
                .isLessThan(account.getAccountBalance()+debitTransactions.getAmount());

    }

}
