package com.bank.BankingSystem;

import com.bank.BankingSystem.domain.Account;
import com.bank.BankingSystem.domain.Transaction;
import com.bank.BankingSystem.service.BankService;
import com.bank.BankingSystem.service.impl.BankServiceImpl;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class BankingSystemApplication {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        BankService bankService = new BankServiceImpl();

        boolean isRunning = true;
        System.out.println("Welcome to console Bank");

        while (isRunning) {
            System.out.println("""
                     1) Open Account
                     2) Deposit
                     3) Withdraw
                     4) Transfer
                     5) Account Statement
                     6) List Accounts
                     7) Search Account by Customer Name
                     8) Exit\s
                    \s""");

            System.out.print("CHOOSE : ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> openAccount(scanner, bankService);
                case "2" -> deposit(scanner, bankService);
                case "3" -> withdraw(scanner, bankService);
                case "4" -> transfer(scanner, bankService);
                case "5" -> accountStatement(scanner, bankService);
                case "6" -> listAccounts(scanner, bankService);
                case "7" -> searchAccountsByCustomerName(scanner, bankService);
                default -> isRunning = false;

            }
        }
    }

    private static void openAccount(Scanner scanner, BankService bankService) {
        System.out.println("Customer name: ");
        String name = scanner.nextLine().trim();
        System.out.println("Customer email: ");
        String email = scanner.nextLine().trim();
        System.out.println("Account Type (SAVINGS/CURRENT): ");
        String type = scanner.nextLine().trim();
        System.out.println("Initial deposit (optional, blank for 0): ");
        double initialAmount = Double.parseDouble(scanner.nextLine().trim());
        String accountNumber = bankService.OpenAccount(name, email, type);
        if (initialAmount > 0) {
            bankService.deposit(accountNumber, initialAmount, "Initial Deposit");
        }
        System.out.println("Account opened: " + accountNumber);
    }

    private static void deposit(Scanner scanner, BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = scanner.nextLine().trim();
        System.out.println("Amount : ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.deposit(accountNumber, amount, "Deposit");
        System.out.println("Deposited");

    }

    private static void withdraw(Scanner scanner, BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = scanner.nextLine().trim();
        System.out.println("Amount : ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.withdraw(accountNumber, amount, "Withdraw");
        System.out.println("Debited");
    }

    private static void transfer(Scanner scanner, BankService bankService) {
        System.out.println("From Account: ");
        String fromAccountNumber = scanner.nextLine().trim();
        System.out.println("To Account: ");
        String toAccountNumber = scanner.nextLine().trim();
        System.out.println("Amount : ");
        double amount = Double.parseDouble(scanner.nextLine().trim());
        bankService.transfer(fromAccountNumber, toAccountNumber, amount, "Transfer");
        System.out.println("Transfer");
    }

    private static void accountStatement(Scanner scanner, BankService bankService) {
        System.out.println("Account number: ");
        String accountNumber = scanner.nextLine().trim();

        List<Transaction> transactions = bankService.accountStatement(accountNumber);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        transactions.forEach(t -> {
            String formatted = t.getTimeStamp().format(formatter);
            System.out.println(t.getAccountNumber() + " | " + t.getType() + " | " + t.getAmount() + " | " + t.getNote() + " | " + formatted);
        });
    }

    private static void listAccounts(Scanner scanner, BankService bankService) {
        List<Account> accountList = bankService.listAccounts();
        accountList.forEach(a -> {
            System.out.println(a.getAccountNumber() + " | " + a.getType() + " | " + a.getBalance());
        });
    }

    private static void searchAccountsByCustomerName(Scanner scanner, BankService bankService) {
        System.out.println("Customer Name Contains: ");
        String q = scanner.nextLine().trim();
        List<Account> accounts = bankService.searchAccountsByCustomerName(q);
        accounts.forEach(account ->
                System.out.println(account.getAccountNumber() + " | " + account.getType() + " | " + account.getBalance())
        );
    }

}
