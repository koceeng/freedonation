package com.koceeng.freedonation.object;

import android.content.Context;
import android.view.View;

import java.util.HashMap;

public class HomeMenuList {

    public enum Name { FEED, SETTING }

    private Context context;
    private HashMap<Name, HomeMenu> itemMap;
    private Name selected;

    public HomeMenuList(Context context) {
        this.context = context;
    }

    public void putItem(final Name name, HomeMenu item) {
        if (itemMap == null)
            itemMap = new HashMap<>();

        item.getLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setActive(name);
            }
        });
        itemMap.put(name, item);
    }

    public void setActive(Name name) {
        if (name != null && itemMap.containsKey(name))
            itemMap.get(name).toggleActive(context, true);

        if (selected != null && itemMap.containsKey(selected))
            itemMap.get(selected).toggleActive(context, false);

        selected = name;
    }

//    public HomeMenu getActiveItem() {
//        if (itemMap == null || selected == null || itemMap.containsKey(selected))
//            return null;
//
//        return itemMap.get(selected);
//    }
}
