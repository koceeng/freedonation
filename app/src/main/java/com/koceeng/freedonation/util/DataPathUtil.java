package com.koceeng.freedonation.util;

public class DataPathUtil {

    /*
    public DatabaseReference getIsPremiumAutoActive() {
        return FirebaseDatabase.getInstance().getReference("is-premium-auto-active");
    }
    */

    private static DataPathUtil dataPathUtil = null;

    public static DataPathUtil getInstance() {
        if (dataPathUtil == null)
            dataPathUtil = new DataPathUtil();
        return dataPathUtil;
    }
}
