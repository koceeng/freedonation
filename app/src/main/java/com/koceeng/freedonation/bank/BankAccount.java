package com.koceeng.freedonation.bank;

import com.google.firebase.database.PropertyName;
import com.koceeng.freedonation.base.FirebaseObject;

public class BankAccount extends FirebaseObject {

    @PropertyName("1st-line")
    public String firstLine;
    @PropertyName("2nd-line")
    public String secondLine;
    @PropertyName("3rd-line")
    public String thirdLine;

    public BankAccount() {
    }

    public String getAllLines() {
        return firstLine + "\n" + secondLine + "\n" + thirdLine;
    }

    public String getFirstLine() {
        return firstLine;
    }

    public String getSecondLine() {
        return secondLine;
    }

    public String getThirdLine() {
        return thirdLine;
    }
}
