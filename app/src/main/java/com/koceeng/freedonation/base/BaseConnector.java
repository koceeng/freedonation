package com.koceeng.freedonation.base;

import com.koceeng.freedonation.util.DebugUtil;

public class BaseConnector {

    public String TAG = "BaseConnector";

    public void setTag(String TAG) {
        this.TAG = TAG;
    }

    public void init() {
        DebugUtil.getInstance().v(TAG, "init");
    }

    public void init(String userId) {
        DebugUtil.getInstance().v(TAG, "init with userId: " + userId);
    }
}
