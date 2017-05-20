package com.koceeng.freedonation.bank;

import com.google.firebase.database.PropertyName;
import com.koceeng.freedonation.base.FirebaseObject;

import java.util.List;

public class BankAccountGroup extends FirebaseObject {

    @PropertyName("group-name")
    public String groupName;
    @PropertyName("group-content")
    public List<BankAccount> groupContent;

    public BankAccountGroup() {
    }

    public BankAccountGroup(String groupName, List<BankAccount> groupContent) {
        this.groupName = groupName;
        this.groupContent = groupContent;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<BankAccount> getGroupContent() {
        return groupContent;
    }
}
