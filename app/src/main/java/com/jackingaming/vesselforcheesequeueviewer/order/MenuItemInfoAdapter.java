package com.jackingaming.vesselforcheesequeueviewer.order;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jackingaming.vesselforcheesequeueviewer.R;

import java.util.List;

public class MenuItemInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = MenuItemInfoAdapter.class.getSimpleName();

    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    private List<MenuItemInfo> menuItemInfos;
    private ItemClickListener listener;

    public MenuItemInfoAdapter(List<MenuItemInfo> menuItemInfos, ItemClickListener listener) {
        this.menuItemInfos = menuItemInfos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_menu_item_info, parent, false);
        return new MenuItemInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MenuItemInfoViewHolder viewHolder = (MenuItemInfoViewHolder) holder;
        MenuItemInfo menuItemInfo = menuItemInfos.get(position);

        viewHolder.getCbHandedOff().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(compoundButton.getContext(), "onCheckedChanged()", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.getTvId().setText(menuItemInfo.getId());
        viewHolder.getTvSize().setText(menuItemInfo.getSize());

        RecyclerView rvCustomizations = viewHolder.getRvCustomizations();
        StringAdapter adapter = new StringAdapter(menuItemInfo.getMenuItemCustomizations(), new StringAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Log.i(TAG, "StringAdapter onClick()");

                String nameCustomization = menuItemInfo.getMenuItemCustomizations().get(position);
                Toast.makeText(view.getContext(), nameCustomization, Toast.LENGTH_SHORT).show();
            }
        });
        rvCustomizations.setAdapter(adapter);
        rvCustomizations.setLayoutManager(new LinearLayoutManager(rvCustomizations.getContext()));
    }

    @Override
    public int getItemCount() {
        return menuItemInfos.size();
    }

    public class MenuItemInfoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private CheckBox cbHandedOff;
        private TextView tvId;
        private TextView tvSize;
        private RecyclerView rvCustomizations;

        public MenuItemInfoViewHolder(@NonNull View itemView) {
            super(itemView);

            cbHandedOff = itemView.findViewById(R.id.cb_handed_off);
            tvId = itemView.findViewById(R.id.tv_id);
            tvSize = itemView.findViewById(R.id.tv_size);
            rvCustomizations = itemView.findViewById(R.id.rv_customizations);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null) {
                listener.onClick(view, getAdapterPosition());
            }
        }

        public CheckBox getCbHandedOff() {
            return cbHandedOff;
        }

        public void setCbHandedOff(CheckBox cbHandedOff) {
            this.cbHandedOff = cbHandedOff;
        }

        public TextView getTvId() {
            return tvId;
        }

        public void setTvId(TextView tvId) {
            this.tvId = tvId;
        }

        public TextView getTvSize() {
            return tvSize;
        }

        public void setTvSize(TextView tvSize) {
            this.tvSize = tvSize;
        }

        public RecyclerView getRvCustomizations() {
            return rvCustomizations;
        }

        public void setRvCustomizations(RecyclerView rvCustomizations) {
            this.rvCustomizations = rvCustomizations;
        }
    }
}
