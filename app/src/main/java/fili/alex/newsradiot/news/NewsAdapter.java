package fili.alex.newsradiot.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fili.alex.newsradiot.R;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<News> newsList = new ArrayList<>();
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView snippet;
        public TextView info;
        public ImageView pic;

        public NewsViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            snippet = (TextView) itemView.findViewById(R.id.snippet);
            info = (TextView) itemView.findViewById(R.id.info);
            pic = (ImageView) itemView.findViewById(R.id.pic);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_news, parent, false);

        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News news = newsList.get(position);

        ((NewsViewHolder) holder).title.setText(news.title);
        ((NewsViewHolder) holder).snippet.setText(news.snippet);
        ((NewsViewHolder) holder).info.setText(news.info());

        if (news.pic != null && !news.pic.isEmpty()) {
            ((NewsViewHolder) holder).pic.setVisibility(View.VISIBLE);
            Picasso.with(context).load(news.pic).into(((NewsViewHolder) holder).pic);
        } else {
            ((NewsViewHolder) holder).pic.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }


    public void addAll(List<News> list) {
        newsList.addAll(list);
        notifyDataSetChanged();
    }
}
