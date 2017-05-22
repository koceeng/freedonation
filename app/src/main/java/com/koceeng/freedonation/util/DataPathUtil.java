package com.koceeng.freedonation.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataPathUtil {

    private static DataPathUtil dataPathUtil = null;

    public static DataPathUtil getInstance() {
        if (dataPathUtil == null)
            dataPathUtil = new DataPathUtil();
        return dataPathUtil;
    }

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

    public DatabaseReference getChangelogUpdateCode() {
        return FirebaseDatabase.getInstance().getReference("changelog/updatecode");
    }

    public DatabaseReference getChangelog() {
        return FirebaseDatabase.getInstance().getReference("changelog");
    }

    public DatabaseReference getBankAccount() {
        return FirebaseDatabase.getInstance().getReference("bank-account-number");
    }

    public DatabaseReference getReportLink() {
        return FirebaseDatabase.getInstance().getReference("report-link");
    }
}
