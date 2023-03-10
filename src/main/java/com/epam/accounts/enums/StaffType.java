package com.epam.accounts.enums;

public enum StaffType {
    Indefinite("Indefinite"),
    Admin("Admin"),
    Consultant("Consultant"),
    HR("HR"),
    Director("Director"),
    Currency_Exchange_Manager("Currency Exchange Manager");

    private final String staffType;

    StaffType(String staffType) {
        this.staffType = staffType;
    }

    public static StaffType getFromString(String value) {
        for (StaffType t: StaffType.values()) {
            if (t.staffType.equals(value)) {
                return t;
            }
        }
        return null;
    }

    public String getValue() {
        return staffType;
    }
}
