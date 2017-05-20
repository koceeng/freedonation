package com.koceeng.freedonation.bank;

import com.koceeng.freedonation.base.FirebaseObject;

public class BankAccount extends FirebaseObject {

    private String bank;
    private String number;
    private String name;

    public BankAccount() {
    }

    public BankAccount(String bank, String number, String name) {
        this.bank = bank;
        this.number = number;
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
}
