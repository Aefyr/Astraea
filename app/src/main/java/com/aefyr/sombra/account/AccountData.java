package com.aefyr.sombra.account;

/**
 * Created by Aefyr on 10.09.2017.
 */

public class AccountData {
    public static final int FIRST_NAME = 0;
    public static final int MIDDLE_NAME = 1;
    public static final int LAST_NAME = 2;

    private String firstName;
    private String middleName;
    private String lastName;

    private String email;

    void setName(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getName(int namePart) {
        switch (namePart) {
            case FIRST_NAME:
                return firstName;
            case MIDDLE_NAME:
                return middleName;
            case LAST_NAME:
                return lastName;
            default:
                throw new IllegalArgumentException("namePart parameter value must match one of the following AccountData class's constants: FIRST_NAME, MIDDLE_NAME, LAST_NAME");
        }
    }

    void setMail(String email) {
        this.email = email;
    }

    public String email() {
        return email;
    }
}
