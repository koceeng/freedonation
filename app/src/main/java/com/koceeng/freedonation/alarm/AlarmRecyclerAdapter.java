package com.koceeng.freedonation.alarm;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.LayoutUtil;

public class AlarmRecyclerAdapter extends RecyclerView.Adapter<AlarmRecyclerAdapter.ViewHolder> {

    AlarmBottomSheet alarmBottomSheet;

    private SortedList<AlarmObject> alarmList;
    private SparseArrayCompat<AlarmObject> alarmIndex = new SparseArrayCompat<>();

    public AlarmRecyclerAdapter(AlarmBottomSheet alarmBottomSheet) {
        this.alarmBottomSheet = alarmBottomSheet;
        alarmList = new SortedList<>(AlarmObject.class, new SortedList.Callback<AlarmObject>() {
            @Override
            public int compare(AlarmObject o1, AlarmObject o2) {
                return 0;
            }

            @Override
            public void onChanged(int position, int count) {

            }

            @Override
            public boolean areContentsTheSame(AlarmObject oldItem, AlarmObject newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(AlarmObject item1, AlarmObject item2) {
                return false;
            }

            @Override
            public void onInserted(int position, int count) {

            }

            @Override
            public void onRemoved(int position, int count) {

            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {

            }
        });
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
            if (alarmObject.getId() == key) {
                alarmList.remove(alarmObject);
                return;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.setting_alarm, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AlarmObject alarmObject = alarmList.get(position);

        LayoutUtil.getInstance().setText(holder.textView, alarmObject.getDisplay());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmBottomSheet.removeAlarm(alarmList.get(holder.getAdapterPosition()));
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
