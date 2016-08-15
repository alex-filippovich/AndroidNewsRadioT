package fili.alex.newsradiot.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import fili.alex.newsradiot.component.NewsCellView;
import fili.alex.newsradiot.model.NewsObject;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsObject> newsList = new ArrayList<>();
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        public NewsCellView view;

        public NewsViewHolder(View itemView) {
            super(itemView);
            view = (NewsCellView) itemView;
        }
    }
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int width = parent.getMeasuredWidth();
        int height = parent.getMeasuredHeight();
        Log.d("NewsAdapter", "w = " + width + ", h=" + height);
        NewsCellView view = new NewsCellView(context, width);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, int position) {
        holder.view.setNews(newsList.get(position), true);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void addAll(List<NewsObject> list) {
        newsList.clear();
        newsList.addAll(list);
        notifyDataSetChanged();
    }
}
