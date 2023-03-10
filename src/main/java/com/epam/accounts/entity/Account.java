package com.epam.accounts.entity;

import com.epam.accounts.enums.AccountName;
import com.epam.accounts.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Account {
    private Long id;
    private String number;
    private AccountName name;
    private LocalDateTime createDate;
    private Card card;
    private Status status;
    private BigDecimal balance;
    private Currency currency;
    private List<Payment> incomePayments;
    private List<Payment> outcomePayments;
}
