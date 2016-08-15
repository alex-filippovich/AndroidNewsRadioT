package fili.alex.newsradiot.ui.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fili.alex.newsradiot.R;
import fili.alex.newsradiot.model.Control;
import fili.alex.newsradiot.model.ControlItem;
import fili.alex.newsradiot.model.Filter;
import fili.alex.newsradiot.model.FilterItem;
import fili.alex.newsradiot.model.HeaderItem;
import fili.alex.newsradiot.model.Sorting;
import fili.alex.newsradiot.model.SortingItem;
import fili.alex.newsradiot.data.NewsDataRepository;
import fili.alex.newsradiot.model.NewsObject;
import fili.alex.newsradiot.ui.view.NewsView;
import fili.alex.newsradiot.util.AndroidHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsPresenter {
    private NewsView newsView;
    @Sorting
    private int activeSorting = Sorting.DATE;
    @Filter
    private int activeFilter = Filter.ALL;

    public NewsPresenter(NewsView newsView) {
        this.newsView = newsView;
    }

    public void setupControls() {
        newsView.setupControls(getControls(), activeFilter, activeSorting);
    }

    public void loadNews() {
        NewsDataRepository
                .getInstance()
                .newsList()
                .flatMapIterable(news -> news)
                .filter(n -> isFiltered(activeFilter, n))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    newsView.showNews(sortedNews(activeSorting, news));
                }, throwable -> {
                    newsView.showError(throwable);
                }, () -> {
                    newsView.showProgress(false);
                });
    }


    public void reloadNews() {
        NewsDataRepository
                .getInstance()
                .newsList()
                .flatMapIterable(news -> news)
                .filter(n -> isFiltered(activeFilter, n))
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(news -> {
                    newsView.showNews(sortedNews(activeSorting, news));
                }, throwable -> {
                    newsView.showError(throwable);
                }, () -> {
                    newsView.showProgress(false);
                    newsView.showInfoRecentNews(false);
                });
    }

    List<ControlItem> getControls() {
        String[] filterNames = AndroidHelper.getStringArray(R.array.filters);
        String[] sortingNames = AndroidHelper.getStringArray(R.array.sorting);
        List<ControlItem> controlItems = new ArrayList<>();

        controlItems.add(new HeaderItem(AndroidHelper.getString(R.string.header_filter)));
        controlItems.add(new FilterItem(filterNames[0], Filter.ALL));
        controlItems.add(new FilterItem(filterNames[1], Filter.COMMON));
        controlItems.add(new FilterItem(filterNames[2], Filter.GEEK));

        controlItems.add(new HeaderItem(AndroidHelper.getString(R.string.header_sorting)));
        controlItems.add(new SortingItem(sortingNames[0], Sorting.PRIORITY));
        controlItems.add(new SortingItem(sortingNames[1], Sorting.DATE));
        controlItems.add(new SortingItem(sortingNames[2], Sorting.COMMENT));
        controlItems.add(new SortingItem(sortingNames[3], Sorting.LIKE));

        return controlItems;
    }

    boolean isFiltered(@Filter int filter, NewsObject news) {
        boolean flag = false;

        switch (filter) {
            case Filter.ALL:
                flag = true;
                break;
            case Filter.COMMON:
                flag = !news.geek;
                break;
            case Filter.GEEK:
                flag = news.geek;
                break;
        }

        return flag;
    }


    List<NewsObject> sortedNews(@Sorting int sorting, List<NewsObject> newsList) {
        Comparator<NewsObject> comparator = null;

        switch (sorting) {
            case Sorting.PRIORITY:
                comparator = (lhs, rhs) -> Integer.compare(lhs.position, rhs.position);
                Collections.sort(newsList, Collections.reverseOrder(comparator));
                break;
            case Sorting.DATE:
                comparator = (lhs, rhs) -> lhs.ats.compareTo(rhs.ats);
                Collections.sort(newsList, Collections.reverseOrder(comparator));
                break;
            case Sorting.COMMENT:
                comparator = (lhs, rhs) -> Integer.compare(lhs.comments, rhs.comments);
                Collections.sort(newsList, Collections.reverseOrder(comparator));
                break;
            case Sorting.LIKE:
                comparator = (lhs, rhs) -> Integer.compare(lhs.likes, rhs.likes);
                Collections.sort(newsList, Collections.reverseOrder(comparator));
                break;
        }


        return newsList;
    }

    public void updateControl(ControlItem item) {
        switch (item.getType()) {
            case Control.FILTER:
                activeFilter = ((FilterItem) item).getFilter();
                break;
            case Control.SORTING:
                activeSorting = ((SortingItem) item).getSorting();
                break;
        }
        reloadNews();
    }

    public void loadRecentNews() {
        NewsDataRepository
            .getInstance()
            .recentNewsList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(newsList -> {
                if (newsList.size() > 0) {
                    newsView.showRecentNews(newsList);
                    newsView.showInfoRecentNews(true);
                }
            }, throwable -> {
                newsView.showError(throwable);
            });

    }

}
