package fili.alex.newsradiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import fili.alex.newsradiot.news.NewsAdapter;
import fili.alex.newsradiot.news.NewsClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    NewsClient client;

    NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView newsView = new RecyclerView(this);
        newsAdapter = new NewsAdapter(this);
        newsView.setHasFixedSize(true);
        newsView.setLayoutManager(new LinearLayoutManager(this));
        newsView.setAdapter(newsAdapter);

        client = new NewsClient();

    }


    @Override
    protected void onResume() {
        super.onResume();

        client.getNews(System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    newsAdapter.addAll(news);
                    System.out.println(news);
                }, throwable -> {
                    Toast.makeText(this, throwable.toString(), Toast.LENGTH_LONG).show();
                }, () -> {
                    Toast.makeText(this, "Completed", Toast.LENGTH_LONG).show();
                });
    }
}
