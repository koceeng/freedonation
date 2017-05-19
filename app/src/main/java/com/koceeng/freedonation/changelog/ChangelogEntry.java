package com.koceeng.freedonation.changelog;

public class ChangelogEntry {
    public static final String KIND_FEATURE = "feature";
    public static final String KIND_BUGFIX = "bugfix";

    // these values are not used YET
    public static final String TYPE_NEW_FEATURE = "new_feature";
    public static final String TYPE_IMPROVEMENT = "improvement";
    public static final String TYPE_BUGFIX_CRITICAL = "critical";
    public static final String TYPE_BUGFIX_MODERATE = "moderate";
    public static final String TYPE_BUGFIX_MINOR = "minor";

    public String versionCode;
    public String versionName;
    public Boolean versionCritical;
    public String kind;
    public String type;
    public String note;

    public ChangelogEntry(String versionCode, String versionName, Boolean versionCritical, String kind, String type, String note) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.versionCritical = versionCritical;
        this.kind = kind;
        this.type = type;
        this.note = note;
    }

    public String getKindDisplay() {
        switch (kind) {
            case KIND_FEATURE:
                return "Features";
            case KIND_BUGFIX:
                return "Bug Fixes";
            default:
                return "";
        }
    }

    public String getNoteDisplay() {
        return note + ".";
    }
}
