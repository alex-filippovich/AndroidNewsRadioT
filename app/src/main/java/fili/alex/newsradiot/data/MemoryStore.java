package fili.alex.newsradiot.data;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import fili.alex.newsradiot.model.NewsObject;
import rx.Observable;

public class MemoryStore {
    private ConcurrentHashMap<String, NewsObject> store = new ConcurrentHashMap<>();

    public boolean isCached(String id) {
        return store.containsKey(id);
    }

    public void put(NewsObject newsObject) {
        if (!isCached(newsObject.id)) {
            store.put(newsObject.id, newsObject);
        }
    }

    public void putAll(List<NewsObject> newsObjects) {
        for (NewsObject newsObject : newsObjects) {
            put(newsObject);
        }
    }

    public Observable<List<NewsObject>> getAll() {
        return Observable.just(new ArrayList<>(store.values()));
    }

    public Observable<NewsObject> get(String id) {
        return Observable.just(store.get(id));
    }

    public int size() {
        return store.size();
    }
}
