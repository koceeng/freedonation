package com.koceeng.freedonation.alarm;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.DebugUtil;
import com.koceeng.freedonation.util.LayoutUtil;

import java.util.List;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder> {

    private final String TAG = "AlarmRecyclerAdapter";

    Context context;
    AlarmBottomSheet alarmBottomSheet;
    AlarmHelper alarmHelper;

    private SortedList<AlarmObject> alarmList;
    private SparseArrayCompat<AlarmObject> alarmIndex = new SparseArrayCompat<>();

    public AlarmRecyclerAdapter(Context contextIn, AlarmBottomSheet alarmBottomSheet, AlarmHelper alarmHelper) {
        this.context = contextIn;
        this.alarmBottomSheet = alarmBottomSheet;
        this.alarmHelper = alarmHelper;
        alarmList = new SortedList<>(AlarmObject.class, new SortedList.Callback<AlarmObject>() {
            @Override
            public int compare(AlarmObject o1, AlarmObject o2) {
                return o1.compare(context, o2);
            }

            @Override
            public boolean areContentsTheSame(AlarmObject oldItem, AlarmObject newItem) {
                return oldItem.areContentsTheSame(context, newItem);
            }

            @Override
            public boolean areItemsTheSame(AlarmObject item1, AlarmObject item2) {
                return item1.areItemsTheSame(item2);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });

        List<AlarmObject> alarmObjects = alarmHelper.getAllData();
        for (AlarmObject alarmObject : alarmObjects) {
            DebugUtil.getInstance().v(TAG, "AlarmRecyclerAdapter: " + alarmObject.getHourOfDay());
            putData(alarmObject);
        }
    }

    public void putData(AlarmObject alarmObject) {
        AlarmObject existing = alarmIndex.get(alarmObject.getId());
        if (existing == null) {
            alarmList.add(alarmObject);
        } else {
            alarmList.updateItemAt(alarmList.indexOf(existing), alarmObject);
        }
        alarmIndex.put(alarmObject.getId(), alarmObject);
    }

    public void removeData(Integer key) {
        alarmIndex.remove(key);

        for (int i = 0; i < alarmList.size(); i++) {
            AlarmObject alarmObject = alarmList.get(i);
            if (alarmObject.getId().equals(key)) {
                alarmList.remove(alarmObject);
                return;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_alarm_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AlarmObject alarmObject = alarmList.get(position);

        LayoutUtil.getInstance().setText(holder.textView, alarmObject.getDisplay(context));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (holder.getAdapterPosition() >= alarmList.size())
                        return;

                    AlarmObject alarmObjectLocal = alarmList.get(holder.getAdapterPosition());
                    removeData(alarmObjectLocal.getId());
                    alarmHelper.removeAlarmData(alarmObjectLocal);

                } catch (Exception e) {
                    DebugUtil.getInstance().e(TAG, "onBindViewHolder button click: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textView;
        public View button;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.setting_alarm_item_text);
            button = itemView.findViewById(R.id.setting_alarm_item_button);
        }
    }
}
