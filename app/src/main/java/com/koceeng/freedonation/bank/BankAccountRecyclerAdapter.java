package com.koceeng.freedonation.bank;

import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.LayoutUtil;

import java.util.List;

public class BankAccountRecyclerAdapter extends RecyclerView.Adapter<BankAccountRecyclerAdapter.ViewHolder> {

    private final String TAG = "BankAccountRecyclerAdapter";

    private List<Pair<String, BankAccount>> bankAccountPairList;

    public BankAccountRecyclerAdapter(List<Pair<String, BankAccount>> bankAccountPairList) {
        this.bankAccountPairList = bankAccountPairList;
    }

    @Override
    public BankAccountRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_account_item, parent, false);
        return new BankAccountRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BankAccountRecyclerAdapter.ViewHolder holder, int position) {
        Pair<String, BankAccount> bankAccountPair = bankAccountPairList.get(position);
        BankAccount bankAccount = bankAccountPair.second;

        if (bankAccountPair.first != null && !bankAccountPair.first.isEmpty()) {
            LayoutUtil.getInstance().setText(holder.textViewGroup, bankAccountPair.first);
            LayoutUtil.getInstance().toggleVisibility(holder.textViewGroup, true);

        } else {
            LayoutUtil.getInstance().toggleVisibility(holder.textViewGroup, false);
        }

        LayoutUtil.getInstance().setText(holder.textViewBank, bankAccount.getBank());
        LayoutUtil.getInstance().setText(holder.textViewNumber, bankAccount.getNumber());
        LayoutUtil.getInstance().setText(holder.textViewName, bankAccount.getName());
    }

    @Override
    public int getItemCount() {
        return bankAccountPairList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewGroup;
        TextView textViewBank;
        TextView textViewNumber;
        TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewGroup = (TextView) itemView.findViewById(R.id.bank_account_item_text_group);
            this.textViewBank = (TextView) itemView.findViewById(R.id.bank_account_item_text_bank);
            this.textViewNumber = (TextView) itemView.findViewById(R.id.bank_account_item_text_number);
            this.textViewName = (TextView) itemView.findViewById(R.id.bank_account_item_text_name);
        }
    }
}