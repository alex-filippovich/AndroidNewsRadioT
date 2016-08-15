package fili.alex.newsradiot.ui.view;


import java.util.List;

import fili.alex.newsradiot.model.ControlItem;
import fili.alex.newsradiot.model.Filter;
import fili.alex.newsradiot.model.Sorting;
import fili.alex.newsradiot.model.NewsObject;

public interface NewsView {
    void showNews(List<NewsObject> newsList);
    void showRecentNews(List<NewsObject> newsList);
    void showError(Throwable throwable);
    void showProgress(boolean show);
    void showInfoRecentNews(boolean show);
    void setupControls(List<ControlItem> controlItems, @Filter int activeFilter, @Sorting int activeSorting);
}
