package fili.alex.newsradiot.net;


import java.util.List;

import fili.alex.newsradiot.model.NewsObject;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface NewsService {
    @GET("/api/v1/news")
    Observable<List<NewsObject>> listNews(@Query("_") String date);

    @GET("/api/v1/news/slug/{slug}")
    Observable<NewsObject> getNews(@Path("slug") String slug);
}
