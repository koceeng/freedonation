package com.koceeng.freedonation.object;

import java.util.Map;

public class VersionData {

    public String current;
    public Map<String, Boolean> supported;

    public VersionData() {
    }

    public VersionData(String current, Map<String, Boolean> supported) {
        this.current = current;
        this.supported = supported;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Map<String, Boolean> getSupported() {
        return supported;
    }

    public void setSupported(Map<String, Boolean> supported) {
        this.supported = supported;
    }
}
