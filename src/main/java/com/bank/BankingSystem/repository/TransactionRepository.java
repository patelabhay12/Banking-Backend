package com.bank.BankingSystem.repository;

import com.bank.BankingSystem.domain.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {
    private final Map<String, List<Transaction>> txnByAccount = new HashMap<>();


    public void add(Transaction transaction) {
        List<Transaction> transactions = txnByAccount.computeIfAbsent(transaction.getAccountNumber(), K -> new ArrayList<Transaction>());
        transactions.add(transaction);
    }

    public List<Transaction> findAllTxn(String accountNumber) {
        return txnByAccount.computeIfAbsent(accountNumber, K -> new ArrayList<Transaction>());
    }
}
