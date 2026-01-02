package com.bank.BankingSystem.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private String id;
    private Type type;
    private String accountNumber;
    private Double amount;
    private LocalDateTime timeStamp;
    private String note;
}
