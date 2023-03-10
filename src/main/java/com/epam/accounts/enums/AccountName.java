package com.epam.accounts.enums;

public enum AccountName {
    CURRENT("Current"),
    DISTRIBUTION("Distribution"),
    CARD("Card");

    private final String accountName;

    AccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getValue() {
        return accountName;
    }

    public static AccountName getFromString(String value) {
        for (AccountName t: AccountName.values()) {
            if (t.accountName.equals(value)) {
                return t;
            }
        }
        return null;
    }
}
