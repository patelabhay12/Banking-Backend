package com.bank.BankingSystem.service;

import com.bank.BankingSystem.domain.Account;
import com.bank.BankingSystem.domain.Transaction;

import java.util.List;

public interface BankService {

    String OpenAccount(String name, String email, String accountType);

    void deposit(String accountNumber, Double amount, String message);

    List<Account> listAccounts();

    void withdraw(String accountNumber, Double amount, String message);

    List<Transaction> accountStatement(String accountNumber);

    void transfer(String fromAccountNumber, String toAccountNumber, double amount, String transfer);

    List<Account> searchAccountsByCustomerName(String q);
}
