package com.koceeng.freedonation.alarm;

public class AlarmObject {

    private Integer id;
    private Integer hourOfDay;
    private Integer minute;
    private Integer pendingIntentRequestCode;

    public AlarmObject(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public String getDisplay() {
        if (getHourOfDay() != null && getMinute() != null) {
            return getHourOfDay() + ":" + getMinute();
        } else {
            return null;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(Integer hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public Integer getPendingIntentRequestCode() {
        return pendingIntentRequestCode;
    }

    public void setPendingIntentRequestCode(Integer pendingIntentRequestCode) {
        this.pendingIntentRequestCode = pendingIntentRequestCode;
    }
}
