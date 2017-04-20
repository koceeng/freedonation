package com.koceeng.freedonation.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataPathUtil {

    public DatabaseReference getIsActive() {
        return FirebaseDatabase.getInstance().getReference("is-active");
    }

    private static DataPathUtil dataPathUtil = null;

    public static DataPathUtil getInstance() {
        if (dataPathUtil == null)
            dataPathUtil = new DataPathUtil();
        return dataPathUtil;
    }
}
