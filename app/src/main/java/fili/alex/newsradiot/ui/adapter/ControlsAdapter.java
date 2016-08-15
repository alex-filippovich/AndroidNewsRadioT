package fili.alex.newsradiot.ui.adapter;


import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fili.alex.newsradiot.component.HeaderView;
import fili.alex.newsradiot.component.ItemClickListener;
import fili.alex.newsradiot.R;
import fili.alex.newsradiot.model.Control;
import fili.alex.newsradiot.model.ControlItem;
import fili.alex.newsradiot.model.Filter;
import fili.alex.newsradiot.model.FilterItem;
import fili.alex.newsradiot.model.HeaderItem;
import fili.alex.newsradiot.model.Sorting;
import fili.alex.newsradiot.model.SortingItem;

public class ControlsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ControlItem> controlItems = new ArrayList<>();
    @Filter
    private int activeFilter;
    @Sorting
    private int activeSorting;
    private ControlListener controlListener;

    public interface ControlListener {
        void updateControl(ControlItem item);
    }

    static class RadioControlHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public AppCompatRadioButton active;
        private ItemClickListener clickListener;

        public RadioControlHolder(View itemView) {
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
            case Control.SORTING:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sorting, parent, false);
                viewHolder = new RadioControlHolder(view);
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
                RadioControlHolder fh = (RadioControlHolder) holder;
                FilterItem filterItem = (FilterItem) controlItems.get(position);

                fh.name.setText(filterItem.getName());
                fh.active.setChecked(filterItem.getFilter() == activeFilter);

                fh.setItemClickListener((view, pos) -> {
                    FilterItem item = (FilterItem) controlItems.get(pos);
                    activeFilter = item.getFilter();
                    controlListener.updateControl(item);
                    notifyDataSetChanged();
                });
                break;
            case Control.SORTING:
                RadioControlHolder sh = (RadioControlHolder) holder;
                SortingItem sortingItem = (SortingItem) controlItems.get(position);
                sh.name.setText(sortingItem.getName());
                sh.active.setChecked(sortingItem.getSorting() == activeSorting);

                sh.setItemClickListener((view, pos) -> {
                    SortingItem item = (SortingItem) controlItems.get(pos);
                    activeSorting = item.getSorting();
                    controlListener.updateControl(item);
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

    public void addAll(List<ControlItem> items) {
        controlItems.clear();
        controlItems.addAll(items);
    }

    public void setSorting(@Sorting int sorting) {
        activeSorting = sorting;
    }

    public void setFilter(@Filter int filter) {
        activeFilter = filter;
    }


    public void setControlListener(ControlListener controlListener) {
        this.controlListener = controlListener;
    }
}
