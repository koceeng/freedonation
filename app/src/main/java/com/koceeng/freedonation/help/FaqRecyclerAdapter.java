package com.koceeng.freedonation.help;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.koceeng.freedonation.R;
import com.koceeng.freedonation.util.LayoutUtil;

import java.util.List;

public class FaqRecyclerAdapter extends RecyclerView.Adapter<FaqRecyclerAdapter.ViewHolder> {

    private final String TAG = "FaqRecyclerAdapter";

    private List<Faq> faqs;

    public FaqRecyclerAdapter(List<Faq> faqs) {
        this.faqs = faqs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.help_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Faq faq = faqs.get(position);

        LayoutUtil.getInstance().setText(holder.textViewQuestion, faq.getQuestion());
        LayoutUtil.getInstance().setText(holder.textViewAnswer, faq.getAnswer());

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutUtil.getInstance().toggleVisibility(holder.textViewAnswer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View parentView;
        public TextView textViewQuestion;
        public TextView textViewAnswer;

        public ViewHolder(View itemView) {
            super(itemView);

            parentView = itemView.findViewById(R.id.help_item_parent);
            textViewQuestion = (TextView) itemView.findViewById(R.id.help_item_text_question);
            textViewAnswer = (TextView) itemView.findViewById(R.id.help_item_text_answer);
        }
    }
}