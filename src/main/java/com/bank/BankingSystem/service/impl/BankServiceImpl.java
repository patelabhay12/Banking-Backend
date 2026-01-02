package com.bank.BankingSystem.service.impl;

import com.bank.BankingSystem.CustomErrors.AccountNotFoundException;
import com.bank.BankingSystem.CustomErrors.InsufficientFundException;
import com.bank.BankingSystem.domain.Account;
import com.bank.BankingSystem.domain.Customer;
import com.bank.BankingSystem.domain.Transaction;
import com.bank.BankingSystem.domain.Type;
import com.bank.BankingSystem.repository.AccountRepository;
import com.bank.BankingSystem.repository.CustomerRepository;
import com.bank.BankingSystem.repository.TransactionRepository;
import com.bank.BankingSystem.service.BankService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


public class BankServiceImpl implements BankService {


    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();

    @Override
    public String OpenAccount(String name, String email, String accountType) {

        String customerId = UUID.randomUUID().toString();

        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setEmail(email);
        customer.setName(name);

        customerRepository.save(customer);

        final var accountNumber = getAccountNumber();
        Account account = new Account(accountNumber, customerId, 0D, accountType);
        accountRepository.save(account);

        return accountNumber;
    }

    @Override
    public void deposit(String accountNumber, Double amount, String message) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setNote(message);
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setType(Type.DEPOSIT);
        transaction.setTimeStamp(LocalDateTime.now());
        transactionRepository.add(transaction);
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream().sorted(Comparator.comparing(Account::getAccountNumber)).collect(Collectors.toList());
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String message) {

        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
        if (amount > account.getBalance()) {
            throw new InsufficientFundException("Insufficient Balance");
        }
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setNote(message);
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setType(Type.DEPOSIT);
        transaction.setTimeStamp(LocalDateTime.now());
        transactionRepository.add(transaction);
    }

    @Override
    public List<Transaction> accountStatement(String accountNumber) {
        return transactionRepository.findAllTxn(accountNumber);
    }

    @Override
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount, String transfer) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new RuntimeException("Can't transfer to your own account");
        }

        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber).orElseThrow(() -> new AccountNotFoundException("From Account not found: " + fromAccountNumber));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber).orElseThrow(() -> new AccountNotFoundException("From Account not found: " + toAccountNumber));

        if (fromAccount.getBalance() < amount) {
            throw new InsufficientFundException("Insufficient Balance");
        }
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        Transaction transactionForFrom = new Transaction();
        transactionForFrom.setId(UUID.randomUUID().toString());
        transactionForFrom.setNote(transfer);
        transactionForFrom.setAccountNumber(fromAccountNumber);
        transactionForFrom.setAmount(amount);
        transactionForFrom.setType(Type.TRANSFER_OUT);
        transactionForFrom.setTimeStamp(LocalDateTime.now());
        transactionRepository.add(transactionForFrom);

        Transaction transactionForTo = new Transaction();
        transactionForTo.setId(UUID.randomUUID().toString());
        transactionForTo.setNote(transfer);
        transactionForTo.setAccountNumber(toAccountNumber);
        transactionForTo.setAmount(amount);
        transactionForTo.setType(Type.TRANSFER_IN);
        transactionForTo.setTimeStamp(LocalDateTime.now());
        transactionRepository.add(transactionForTo);
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String q) {
        String query = (q == null) ? "" : q.toLowerCase();
//        List<Account> accounts = new ArrayList<>();
//        for (Customer c : customerRepository.findAll()) {
//            if (c.getName().toLowerCase().contains(query)) {
//                accounts.addAll(accountRepository.findByCustomerId(c.getId()));
//            }
//        }
        return customerRepository.findAll().stream().filter(c -> c.getName().contains(query)).flatMap(c -> accountRepository.findByCustomerId(c.getId()).stream()).sorted(Comparator.comparing(Account::getAccountNumber)).collect(Collectors.toList());
    }

    private String getAccountNumber() {
        int count = accountRepository.findAll().size();
        int temp = (count == 0) ? 1 : count + 1;
        return String.format("AC%06d", temp);
    }
}
