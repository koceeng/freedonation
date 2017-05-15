package com.koceeng.freedonation.alarm;

import android.content.Context;

import com.koceeng.freedonation.util.LanguageUtil;

public class AlarmObject {

    private Integer id;
    private Integer hourOfDay;
    private Integer minute;
    private Integer pendingIntentRequestCode;

    public AlarmObject(int hourOfDay, int minute) {
        this.hourOfDay = hourOfDay;
        this.minute = minute;
    }

    public int compare(Context context, AlarmObject compared) {
        if (compared == null)
            return -1;

        int result = getId().compareTo(compared.getId());
        if (result != 0) {
            result = getDisplay(context).compareTo(compared.getDisplay(context));
        }

        return result;
    }

    public boolean areContentsTheSame(Context context, AlarmObject compared) {
        if (compared == null)
            return false;

        if ((getDisplay(context) == null ? "" : getDisplay(context)).equals(compared.getDisplay(context) == null ? "" : compared.getDisplay(context))) {
            return false;
        } else if ((getPendingIntentRequestCode() == null ? -1 : getPendingIntentRequestCode()) == (compared.getPendingIntentRequestCode() == null ? -1 : getPendingIntentRequestCode())) {
            return false;
        }
        return true;
    }

    public boolean areItemsTheSame(AlarmObject compared) {
        return getId().equals(compared.getId());
    }

    public String getDisplay(Context context) {
        if (getHourOfDay() != null && getMinute() != null) {
            return String.format(LanguageUtil.getInstance().getCurrentLocale(context), "%02d", getHourOfDay())
                    + ":" + String.format(LanguageUtil.getInstance().getCurrentLocale(context), "%02d", getMinute());
        } else {
            return null;
        }
    }

    public Integer getId() {
        return (id != null) ? id : getPendingIntentRequestCode();
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
