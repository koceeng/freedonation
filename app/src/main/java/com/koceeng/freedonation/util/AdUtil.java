package com.koceeng.freedonation.util;

import com.google.android.gms.ads.AdRequest;

public class AdUtil {

    public AdRequest getAdRequest() {
        return new AdRequest.Builder()
                // .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();
    }

    private static AdUtil adUtil;

    public static AdUtil getInstance() {
        if (adUtil == null)
            adUtil = new AdUtil();
        return adUtil;
    }
}
