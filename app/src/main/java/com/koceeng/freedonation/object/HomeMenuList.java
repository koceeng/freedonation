package com.koceeng.freedonation.object;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ViewFlipper;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.IntentUtil;

import java.util.HashMap;

public class HomeMenuList {

    private final String TAG = "HomeMenuList";

    public enum Name { FEED, REPORT, SETTING, OTHER_APP, SHARE, HELP }

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

        switch (name) {
            case OTHER_APP:
                item.getLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        IntentUtil.getInstance().goToMarket(context, true);
                    }
                });
                break;

            case SHARE:
                item.getLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share_subject));;
                            intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text));
                            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_choose_app)));
                        } catch(Exception e) {
                            DebugUtil.getInstance().e(TAG, e.toString());
                        }
                    }
                });
                break;

            default:
                item.setViewFlipper(viewFlipper);
                item.getLayout().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setActive(name);
                    }
                });
                break;
        }
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
