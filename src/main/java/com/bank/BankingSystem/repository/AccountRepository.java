package com.bank.BankingSystem.repository;

import com.bank.BankingSystem.domain.Account;
import com.bank.BankingSystem.domain.Customer;

import java.util.*;

public class AccountRepository {
    private final Map<String, Account> accountsByNumber = new HashMap<>();


    public void save(Account account) {
        accountsByNumber.put(account.getAccountNumber(), account);
    }

    public List<Account> findAll() {
        if (accountsByNumber.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(accountsByNumber.values());
    }

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.ofNullable(accountsByNumber.get(accountNumber));
    }

    public List<Account> findByCustomerId(String id) {
        List<Account> result = new ArrayList<>();
        for (Account a : accountsByNumber.values()) {
            if (a.getCustomerId().equals(id)) {
                result.add(a);
            }
        }
        result.sort(Comparator.comparing(Account::getAccountNumber));
        return result;
    }
}
