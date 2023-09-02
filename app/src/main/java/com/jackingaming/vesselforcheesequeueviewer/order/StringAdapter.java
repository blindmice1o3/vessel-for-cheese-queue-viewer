package com.jackingaming.vesselforcheesequeueviewer.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StringAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = StringAdapter.class.getSimpleName();

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    private List<String> customizations;
    private ItemClickListener listener;

    public StringAdapter(List<String> customizations, ItemClickListener listener) {
        this.customizations = customizations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                android.R.layout.simple_list_item_1, parent, false);
        return new CustomizationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CustomizationViewHolder viewHolder = (CustomizationViewHolder) holder;
        String nameCustomization = customizations.get(position);

        viewHolder.getTvName().setText(nameCustomization);
    }

    @Override
    public int getItemCount() {
        return customizations.size();
    }

    public class CustomizationViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView tvName;

        public CustomizationViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onClick(view, getAdapterPosition());
            }
        }

        public TextView getTvName() {
            return tvName;
        }

        public void setTvName(TextView tvName) {
            this.tvName = tvName;
        }
    }
}
