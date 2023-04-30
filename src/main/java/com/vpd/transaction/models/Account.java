package com.vpd.transaction.models;

import com.vpd.transaction.enums.AccountTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36,
            updatable = false,
            nullable = false
    )
    private UUID id;

    @Column(unique = true)
    private Long accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountTypes accountType;


    private Long accountBalance;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account", fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy="account")
    private Set<Transactions> transactions;
}
