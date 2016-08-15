package fili.alex.newsradiot.net;


import java.util.List;

import fili.alex.newsradiot.model.NewsObject;
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

    public Observable<List<NewsObject>> getNewsList(long date) {
        return mClient.listNews(String.valueOf(date));
    }

    public Observable<NewsObject> getNews(String slug) {
        return mClient.getNews(slug);
    }
}
