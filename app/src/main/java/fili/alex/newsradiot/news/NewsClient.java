package fili.alex.newsradiot.news;


import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class NewsClient {
    private NewsService mClient;

    public NewsClient() {
        mClient = new Retrofit.Builder()
                .baseUrl("https://news.radio-t.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(NewsService.class);
    }

    public Observable<List<News>> getNews(long date) {
        return mClient.listNews(String.valueOf(date));
    }
}
