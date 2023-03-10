package com.epam.accounts.entity;

import com.epam.accounts.enums.CardProvider;
import com.epam.accounts.enums.CardName;
import com.epam.accounts.enums.Status;

import java.time.LocalDateTime;

public class Card {
    private Long id;
    private String number;
    private CardName name;
    private LocalDateTime createDate;
    private LocalDateTime expirationDate;
    private CardProvider provider;
    private boolean isChipped;
    private Status status;
}
