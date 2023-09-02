package com.jackingaming.vesselforcheesequeueviewer.meal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.vesselforcheesequeueviewer.R;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    private List<Meal> localData;
    private ItemClickListener itemClickListener;

    public MealAdapter(List<Meal> localData, ItemClickListener itemClickListener) {
        this.localData = localData;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meal selectedItem = localData.get(position);
        String id = Long.toString(selectedItem.getId());
        String name = selectedItem.getName();
        String size = Integer.toString(selectedItem.getSize());
        String drink = selectedItem.getDrink();

        holder.getTvId().setText(id);
        holder.getTvName().setText(name);
        holder.getTvSize().setText(size);
        holder.getTvDrink().setText(drink);
    }

    @Override
    public int getItemCount() {
        return localData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView tvId, tvName, tvSize, tvDrink;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSize = itemView.findViewById(R.id.tv_size);
            tvDrink = itemView.findViewById(R.id.tv_drink);

            // Define click listener for the ViewHolder's View
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClick(view, getAdapterPosition());
            }
        }

        public TextView getTvId() {
            return tvId;
        }

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvSize() {
            return tvSize;
        }

        public TextView getTvDrink() {
            return tvDrink;
        }
    }

}
