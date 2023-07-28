package com.epam.accounts.enums;

public enum Department {
    TECHNICAL_SUPPORT("Technical Support"),
    CUSTOMER_SERVICE("Customer Service"),
    HR("HR"),
    ADMINISTRATION("Administration"),
    ACCOUNTING("Accounting");

    private final String department;

    Department(String department) {
        this.department = department;
    }

    public static Department getFromString(String value) {
        for (Department t: Department.values()) {
            if (t.department.equals(value)) {
                return t;
            }
        }
        return null;
    }
}
