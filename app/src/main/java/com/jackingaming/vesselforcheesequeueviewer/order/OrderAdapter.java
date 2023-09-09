package com.jackingaming.vesselforcheesequeueviewer.order;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.vesselforcheesequeueviewer.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = OrderAdapter.class.getSimpleName();

    public interface ItemClickListener {
        void onClick(View view, int position);

        void onOrderHandedOff(int position);
    }

    private List<Order> orders;
    private ItemClickListener listener;

    public OrderAdapter(List<Order> orders, ItemClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderViewHolder viewHolder = (OrderViewHolder) holder;
        int positionSelected = position;
        Order order = orders.get(position);

        viewHolder.getTvTimestamp().setText(order.getCreatedOn().toString());

        RecyclerView rvMenuItemInfos = viewHolder.getRvMenuItemInfos();
        MenuItemInfoAdapter adapter = new MenuItemInfoAdapter(order.getMenuItemInfos(), new MenuItemInfoAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i(TAG, "MenuItemInfoAdapter.onClick()");

                MenuItemInfo menuItemInfo = order.getMenuItemInfos().get(position);
                Toast.makeText(view.getContext(), menuItemInfo.getId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCheckBoxClick() {
                Log.i(TAG, "MenuItemInfoAdapter.onCheckBoxClick()");

                int numberOfHandedOff = 0;
                for (MenuItemInfo menuItemInfo : order.getMenuItemInfos()) {
                    if (menuItemInfo.isHandedOff()) {
                        numberOfHandedOff++;
                    }
                }

                if (numberOfHandedOff == order.getMenuItemInfos().size()) {
                    listener.onOrderHandedOff(positionSelected);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rvMenuItemInfos.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvMenuItemInfos.getContext(), linearLayoutManager.getOrientation());
        rvMenuItemInfos.setAdapter(adapter);
        rvMenuItemInfos.setLayoutManager(linearLayoutManager);
        rvMenuItemInfos.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView tvTimestamp;
        private RecyclerView rvMenuItemInfos;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            rvMenuItemInfos = itemView.findViewById(R.id.rv_menu_item_infos);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onClick(view, getAdapterPosition());
            }
        }

        public TextView getTvTimestamp() {
            return tvTimestamp;
        }

        public void setTvTimestamp(TextView tvTimestamp) {
            this.tvTimestamp = tvTimestamp;
        }

        public RecyclerView getRvMenuItemInfos() {
            return rvMenuItemInfos;
        }

        public void setRvMenuItemInfos(RecyclerView rvMenuItemInfos) {
            this.rvMenuItemInfos = rvMenuItemInfos;
        }
    }
}
