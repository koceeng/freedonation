package com.koceeng.freedonation.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataPathUtil {

    public DatabaseReference getIsActive() {
        return FirebaseDatabase.getInstance().getReference("is-active");
    }

    public DatabaseReference getContentLast(String lang) {
        return FirebaseDatabase.getInstance().getReference("/content/" + lang + "/last");
    }

    public DatabaseReference getContentById(String lang, String contentId) {
        return FirebaseDatabase.getInstance().getReference("/content/" + lang + "/data/" + contentId);
    }

    public DatabaseReference getFaq(String lang) {
        return FirebaseDatabase.getInstance().getReference("/faq/" + lang);
    }

    private static DataPathUtil dataPathUtil = null;

    public static DataPathUtil getInstance() {
        if (dataPathUtil == null)
            dataPathUtil = new DataPathUtil();
        return dataPathUtil;
    }
}
