package com.epam.accounts.enums;

public enum CardProvider {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    AMERICAN_EXPRESS("American Express");

    private final String cardProvider;

    CardProvider(String cardProvider) {
        this.cardProvider = cardProvider;
    }

    public static CardProvider getFromString(String value) {
        for (CardProvider t: CardProvider.values()) {
            if (t.cardProvider.equals(value)) {
                return t;
            }
        }
        return null;
    }
}
