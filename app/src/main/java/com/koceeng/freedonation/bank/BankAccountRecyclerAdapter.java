package com.koceeng.freedonation.bank;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.LayoutUtil;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class BankAccountRecyclerAdapter extends RecyclerView.Adapter<BankAccountRecyclerAdapter.ViewHolder> {

    private final String TAG = "BankAccountRecAdp";

    private Context context;
    private List<Pair<String, BankAccount>> bankAccountPairList;

    public BankAccountRecyclerAdapter(Context context, List<Pair<String, BankAccount>> bankAccountPairList) {
        this.context = context;
        this.bankAccountPairList = bankAccountPairList;
    }

    @Override
    public BankAccountRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_account_item, parent, false);
        return new BankAccountRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BankAccountRecyclerAdapter.ViewHolder holder, int position) {
        Pair<String, BankAccount> bankAccountPair = bankAccountPairList.get(position);
        BankAccount bankAccount = bankAccountPair.second;

        if (bankAccountPair.first != null && !bankAccountPair.first.isEmpty()) {
            LayoutUtil.getInstance().setText(holder.textViewGroup, bankAccountPair.first);
            LayoutUtil.getInstance().toggleVisibility(holder.textViewGroup, true);
            LayoutUtil.getInstance().toggleVisibility(holder.divider, false);

        } else {
            LayoutUtil.getInstance().toggleVisibility(holder.textViewGroup, false);
        }

        LayoutUtil.getInstance().setText(holder.textViewBank, bankAccount.getFirstLine());
        LayoutUtil.getInstance().setText(holder.textViewNumber, bankAccount.getSecondLine());
        LayoutUtil.getInstance().setText(holder.textViewName, bankAccount.getThirdLine());

        holder.buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bankAccountPairList.size() <= holder.getAdapterPosition())
                    return;

                BankAccount bankAccountLocal = bankAccountPairList.get(holder.getAdapterPosition()).second;
                if (bankAccountLocal == null)
                    return;

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(ClipData.newPlainText(context.getString(R.string.bank_account_copy_tag), bankAccountLocal.getAllLines()));
                Toast.makeText(context, context.getString(R.string.bank_account_copied), Toast.LENGTH_SHORT).show();
            }
        });
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
        View divider;
        View buttonCopy;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textViewGroup = (TextView) itemView.findViewById(R.id.bank_account_item_text_group);
            this.textViewBank = (TextView) itemView.findViewById(R.id.bank_account_item_text_bank);
            this.textViewNumber = (TextView) itemView.findViewById(R.id.bank_account_item_text_number);
            this.textViewName = (TextView) itemView.findViewById(R.id.bank_account_item_text_name);
            this.divider = itemView.findViewById(R.id.bank_account_item_layout_divider);
            this.buttonCopy = itemView.findViewById(R.id.bank_account_item_button_copy);
        }
    }
}