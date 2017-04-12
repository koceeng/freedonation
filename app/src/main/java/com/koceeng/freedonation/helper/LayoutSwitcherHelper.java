package com.koceeng.freedonation.helper;

import android.view.View;

import com.koceeng.freedonation.util.LayoutUtil;

import java.util.HashMap;
import java.util.Map;

public class LayoutSwitcherHelper {

    public enum LayoutType { LOADING, MAIN, NO_RESULT }

    private LayoutType activeLayout;
    private HashMap<LayoutType, View> layoutList;

    public LayoutSwitcherHelper() {
    }

    public LayoutSwitcherHelper(View layoutMain, View layoutLoading, View layoutNoResult) {
        addToLayoutList(LayoutType.MAIN, layoutMain);
        addToLayoutList(LayoutType.LOADING, layoutLoading);
        addToLayoutList(LayoutType.NO_RESULT, layoutNoResult);
    }

    public void addToLayoutList(LayoutType layoutType, View layout) {
        if (layoutList == null)
            layoutList = new HashMap<>();

        layoutList.put(layoutType, layout);
    }

    public void showLayout(LayoutType layoutType) {

        for (Map.Entry<LayoutType, View> layout : layoutList.entrySet()) {
            LayoutUtil.getInstance().setVisibility(layout.getValue(),
                    layout.getKey() == layoutType ? View.VISIBLE : View.GONE);
        }

        activeLayout = layoutType;
    }

    public LayoutType getActiveLayout() {
        return activeLayout;
    }
}
