package com.epam.accounts.entity;

import com.epam.accounts.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal sum;
    private Currency currency;
    private PaymentStatus status;
    private LocalDateTime createDate;
}
