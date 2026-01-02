package com.bank.BankingSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String accountNumber;
    private String customerId;
    private Double balance;
    private String type;
}
