package com.epam.accounts.enums;

public enum CardName {
    CREDIT_CARD("Credit"),
    DEBIT_CARD("Debit");

    private final String creditCardName;

    CardName(String creditCardName) {
        this.creditCardName = creditCardName;
    }

    public String getValue() {
        return creditCardName;
    }

    public static CardName getFromString(String value) {
        for (CardName t: CardName.values()) {
            if (t.creditCardName.equals(value)) {
                return t;
            }
        }
        return null;
    }
}
