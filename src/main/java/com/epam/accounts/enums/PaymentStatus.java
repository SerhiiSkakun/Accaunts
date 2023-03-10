package com.epam.accounts.enums;

public enum PaymentStatus {
    PREPARED("prepared"),
    SENT("sent");

    private String paymentStatus;

    PaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public static PaymentStatus getFromString(String value) {
        for (PaymentStatus t: PaymentStatus.values()) {
            if (t.paymentStatus.equals(value)) {
                return t;
            }
        }
        return null;
    }

    public String getValue() {
        return paymentStatus;
    }
}
