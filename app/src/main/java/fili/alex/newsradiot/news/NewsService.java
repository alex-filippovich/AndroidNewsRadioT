package fili.alex.newsradiot.news;


import java.util.List;

import fili.alex.newsradiot.news.News;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface NewsService {
    @GET("/api/v1/news")
    Observable<List<News>> listNews(@Query("_") String date);
}
