package com.koceeng.freedonation.object;

import android.content.Context;
import android.view.View;
import android.widget.ViewFlipper;

import java.util.HashMap;

public class HomeMenuList {

    public enum Name { FEED, REPORT, SETTING }

    private Context context;
    private ViewFlipper viewFlipper;
    private HashMap<Name, HomeMenu> itemMap;
    private Name selected;

    public HomeMenuList(Context context, ViewFlipper viewFlipper) {
        this.context = context;
        this.viewFlipper = viewFlipper;
    }

    public void putItem(final Name name, HomeMenu item) {
        if (itemMap == null)
            itemMap = new HashMap<>();

        item.setViewFlipper(viewFlipper);
        item.getLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActive(name);
            }
        });
        itemMap.put(name, item);
    }

    public void setActive(Name name) {
        setActive(name, true);
    }

    public void setActive(Name name, boolean withAnimation) {
        if (name == null || name.equals(selected))
            return;

        if (itemMap.containsKey(name))
            itemMap.get(name).toggleActive(context, true, withAnimation);

        if (selected != null && itemMap.containsKey(selected))
            itemMap.get(selected).toggleActive(context, false, false);

        selected = name;
    }

//    public HomeMenu getActiveItem() {
//        if (itemMap == null || selected == null || itemMap.containsKey(selected))
//            return null;
//
//        return itemMap.get(selected);
//    }
}
