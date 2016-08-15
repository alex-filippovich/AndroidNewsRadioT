package fili.alex.newsradiot.ui;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import fili.alex.newsradiot.R;
import fili.alex.newsradiot.model.ControlItem;
import fili.alex.newsradiot.ui.adapter.ControlsAdapter;
import fili.alex.newsradiot.model.Filter;
import fili.alex.newsradiot.model.Sorting;
import fili.alex.newsradiot.model.NewsObject;
import fili.alex.newsradiot.ui.adapter.NewsAdapter;
import fili.alex.newsradiot.ui.presenter.NewsPresenter;
import fili.alex.newsradiot.ui.view.NewsView;

public class MainActivity extends AppCompatActivity implements NewsView, View.OnClickListener {
    private NewsAdapter newsAdapter;
    private ControlsAdapter controlAdapter;
    private DrawerLayout drawerLayout;
    private ProgressBar progressBar;
    private RecyclerView newsList;
    private NewsPresenter newsPresenter;
    private TextView infoAboutRecentNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        infoAboutRecentNews = (TextView) findViewById(R.id.info_about_recent_news);

        controlAdapter = new ControlsAdapter();

        RecyclerView filtersList = (RecyclerView) findViewById(R.id.controls);
        filtersList.setLayoutManager(new LinearLayoutManager(this));
        filtersList.setHasFixedSize(true);
        filtersList.setAdapter(controlAdapter);

        newsList = (RecyclerView) findViewById(R.id.news_list);
        newsAdapter = new NewsAdapter(this);
        newsList.setHasFixedSize(true);
        newsList.setLayoutManager(new LinearLayoutManager(this));
        newsList.setAdapter(newsAdapter);

        newsPresenter = new NewsPresenter(this);
        newsPresenter.setupControls();
        newsPresenter.loadNews();

        controlAdapter.setControlListener(item -> {
            newsPresenter.updateControl(item);
            drawerLayout.closeDrawers();
        });

        infoAboutRecentNews.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        newsPresenter.loadRecentNews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                drawerLayout.openDrawer(GravityCompat.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showNews(List<NewsObject> newsList) {
        newsAdapter.addAll(newsList);
    }

    @Override
    public void showRecentNews(List<NewsObject> newsList) {
        infoAboutRecentNews.setText(String.format("%d recent news", newsList.size()));
    }

    @Override
    public void showError(Throwable throwable) {
        Log.e("MainActivity", throwable.getMessage());
    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            newsList.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            newsList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showInfoRecentNews(boolean show) {
        infoAboutRecentNews.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setupControls(List<ControlItem> controlItems, @Filter int activeFilter, @Sorting int activeSorting) {
        controlAdapter.addAll(controlItems);
        controlAdapter.setFilter(activeFilter);
        controlAdapter.setSorting(activeSorting);
        controlAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.info_about_recent_news) {
            newsPresenter.reloadNews();
        }
    }
}
