package fili.alex.newsradiot.controls;


import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fili.alex.newsradiot.HeaderView;
import fili.alex.newsradiot.ItemClickListener;
import fili.alex.newsradiot.R;

public class ControlsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ControlItem> controlItems;
    private int activeFilters;
    @Sorting
    private int activeSorting;
    Context context;

    public ControlsAdapter(Context context, List<ControlItem> controlItems, int filters, @Sorting int activeSorting) {
        this.context = context;
        this.controlItems = controlItems;
        this.activeFilters = filters;
        this.activeSorting = activeSorting;
    }

    static class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public AppCompatCheckBox check;
        private ItemClickListener clickListener;

        public FilterHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            check = (AppCompatCheckBox) itemView.findViewById(R.id.check);
            itemView.setOnClickListener(this);
            check.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener == null) return;
            clickListener.click(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }


    static class SortingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public AppCompatRadioButton active;
        private ItemClickListener clickListener;

        public SortingHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            active = (AppCompatRadioButton) itemView.findViewById(R.id.active);
            itemView.setOnClickListener(this);
            active.setOnClickListener(this);
        }

        public void onClick(View v) {
            if (clickListener == null) return;
            clickListener.click(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener clickListener) {
            this.clickListener = clickListener;
        }
    }


    static class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case Control.HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
                viewHolder = new HeaderHolder(headerView);
                break;
            case Control.FILTER:
                View filterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
                viewHolder = new FilterHolder(filterView);
                break;
            case Control.SORTING:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sorting, parent, false);
                viewHolder = new SortingHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Control.HEADER:
                HeaderHolder hh = (HeaderHolder) holder;
                HeaderItem headerItem = (HeaderItem) controlItems.get(position);
                ((HeaderView) hh.itemView).setShowDivider(position != 0);
                ((HeaderView) hh.itemView).setText(headerItem.getName());
                break;
            case Control.FILTER:
                FilterHolder fh = (FilterHolder) holder;
                FilterItem filterItem = (FilterItem) controlItems.get(position);
                fh.name.setText(filterItem.getName());
                fh.check.setChecked(isActive(filterItem.getFilter()));

                fh.setItemClickListener((view, pos) -> {
                    if (view.getId() != R.id.check) {
                        AppCompatCheckBox check = (AppCompatCheckBox) view.findViewById(R.id.check);
                        check.setChecked(!check.isChecked());
                    }
                    FilterItem item = (FilterItem) controlItems.get(pos);
                    setActive(item.getFilter());
                });

                break;
            case Control.SORTING:
                SortingHolder sh = (SortingHolder) holder;
                SortingItem sortingItem = (SortingItem) controlItems.get(position);
                sh.name.setText(sortingItem.getName());
                sh.active.setChecked(sortingItem.getSorting() == activeSorting);

                sh.setItemClickListener((view, pos) -> {
                    activeSorting = ((SortingItem) controlItems.get(pos)).getSorting();
                    notifyDataSetChanged();
                });
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return getControlType(position);
    }

    @Override
    public int getItemCount() {
        return controlItems.size();
    }

    public int getControlType(int position) {
        return controlItems.get(position).getType();
    }


    private boolean isActive(@Filter int filter) {
        return (activeFilters & (1 << filter)) != 0;
    }

    private void setActive(@Filter int filter) {
        if (isActive(filter)) {
            activeFilters |= (1 << filter);
        } else {
            activeFilters ^= (1 << filter);
        }
    }
}
