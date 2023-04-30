package com.vpd.transaction.repositories;

import com.vpd.transaction.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepo extends JpaRepository<Transactions, UUID> {
}
