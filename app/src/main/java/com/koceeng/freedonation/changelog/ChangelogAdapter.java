package com.koceeng.freedonation.changelog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.LayoutUtil;

import java.util.List;

public class ChangelogAdapter extends RecyclerView.Adapter<ChangelogAdapter.ViewHolder> {

    String lastVersionName = null;
    String lastKind = null;
    private List<ChangelogEntry> changelistEntries;

    public ChangelogAdapter(List<ChangelogEntry> changelistEntries) {
        this.changelistEntries = changelistEntries;
    }

    @Override
    public ChangelogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.changelog_item, parent, false);
        return new ChangelogAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChangelogAdapter.ViewHolder holder, int position) {
        ChangelogEntry changelistEntry = changelistEntries.get(position);

        Boolean showVersionName = lastVersionName == null || !lastVersionName.equals(changelistEntry.versionName);
        if (showVersionName)
            holder.textViewVersionName.setText(changelistEntry.versionName);
        LayoutUtil.getInstance().toggleVisibility(holder.textViewVersionName, showVersionName);

        Boolean showKind = lastKind == null || !lastKind.equals(changelistEntry.kind) || showVersionName;
        if (showKind)
            holder.textViewKind.setText(changelistEntry.getKindDisplay());
        LayoutUtil.getInstance().toggleVisibility(holder.textViewKind, showKind);

        holder.textViewNote.setText(changelistEntry.getNoteDisplay());

        lastVersionName = changelistEntry.versionName;
        lastKind = changelistEntry.kind;
    }

    @Override
    public int getItemCount() {
        return changelistEntries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewVersionName;
        TextView textViewKind;
        TextView textViewNote;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewVersionName = (TextView) itemView.findViewById(R.id.changelog_item_text_version_name);
            this.textViewKind = (TextView) itemView.findViewById(R.id.changelog_item_text_kind);
            this.textViewNote = (TextView) itemView.findViewById(R.id.changelog_item_text_note);
        }
    }
}
