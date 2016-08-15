package fili.alex.newsradiot.data;


import android.text.TextUtils;

import java.util.List;

import fili.alex.newsradiot.model.NewsObject;
import fili.alex.newsradiot.net.NewsClient;
import fili.alex.newsradiot.App;
import rx.Observable;

public class NewsDataRepository {
    private final MemoryStore store;
    private final NewsDbHelper helper;
    private final NewsClient client;
    private volatile boolean first;

    private NewsDataRepository() {
        store = new MemoryStore();
        helper = new NewsDbHelper(App.appContext);
        client = new NewsClient();
    }

    private static NewsDataRepository instance;

    public static NewsDataRepository getInstance() {
        NewsDataRepository localRepository = instance;

        if (localRepository == null) {
            synchronized (NewsDataRepository.class) {
                localRepository = instance;
                if (localRepository == null) {
                    localRepository = instance = new NewsDataRepository();
                }
            }
        }

        return localRepository;
    }

    public Observable<List<NewsObject>> newsList() {
        if (store.size() > 0) {
            first = true;
            return store.getAll();
        }

        return client.getNewsList(System.currentTimeMillis())
                .doOnNext(newsObjects -> {
                    helper.openForRead().addAll(newsObjects);
                    helper.close();
                })
                .doOnCompleted(() -> {
                    first = true;
                })
                .doOnNext(store::putAll);
    }

    public Observable<List<NewsObject>> recentNewsList() {
        if (first) {
            return client
                    .getNewsList(System.currentTimeMillis())
                    .flatMapIterable(newsObjects -> newsObjects)
                    .filter(newsObject -> !store.isCached(newsObject.id))
                    .toList()
                    .doOnNext(newsObjects -> {
                        helper.openForRead().addAll(newsObjects);
                        helper.close();
                    })
                    .doOnNext(store::putAll);
        }
        return Observable.empty();
    }

    public Observable<NewsObject> news(String id) {
        return store.get(id)
                .flatMap(newsObject -> {
                    if (!TextUtils.isEmpty(newsObject.content)) {
                       return Observable.just(newsObject);
                    }
                    return client
                            .getNews(newsObject.slug)
                            .flatMap(this::updateNewsObject);
                });
    }

    private Observable<NewsObject> updateNewsObject(NewsObject newsObject) {
        return Observable.fromCallable(() -> {
            helper.openForRead().update(newsObject);
            helper.close();
            return newsObject;
        }).doOnNext(store::put);
    }

    public void initialize() {
        List<NewsObject> newsObjects = helper.openForRead().getAll();
        store.putAll(newsObjects);
        helper.close();
    }
}
