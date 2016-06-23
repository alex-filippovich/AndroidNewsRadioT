package fili.alex.newsradiot;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fili.alex.newsradiot.controls.ControlItem;
import fili.alex.newsradiot.controls.ControlsAdapter;
import fili.alex.newsradiot.controls.Filter;
import fili.alex.newsradiot.controls.FilterItem;
import fili.alex.newsradiot.controls.HeaderItem;
import fili.alex.newsradiot.controls.Sorting;
import fili.alex.newsradiot.controls.SortingItem;
import fili.alex.newsradiot.news.DividerItemDecoration;
import fili.alex.newsradiot.news.GridItemDecoration;
import fili.alex.newsradiot.news.NewsAdapter;
import fili.alex.newsradiot.news.NewsClient;
import fili.alex.newsradiot.util.AndroidHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    NewsClient client;

    NewsAdapter newsAdapter;
    private DrawerLayout drawerLayout;
    @Sorting
    int activeSorting = Sorting.DATE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ControlsAdapter adapter = new ControlsAdapter(this, getControls(), getCurrentFilters(), activeSorting);

        RecyclerView filtersList = (RecyclerView) findViewById(R.id.controls);
        filtersList.setLayoutManager(new LinearLayoutManager(this));
        filtersList.setHasFixedSize(true);
        filtersList.setAdapter(adapter);

        RecyclerView newsList = (RecyclerView) findViewById(R.id.news_list);
        newsAdapter = new NewsAdapter(this);
        newsList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = null;
        RecyclerView.ItemDecoration itemDecoration = null;
        if (AndroidHelper.isTablet() && AndroidHelper.isLandscape()) {
            int column = AndroidHelper.isSmallTablet() ? 2 : 3;
            layoutManager = new GridLayoutManager(this, column, LinearLayoutManager.VERTICAL, false);
            itemDecoration = new GridItemDecoration(this, column, 12);
        } else {
            layoutManager = new LinearLayoutManager(this);
            itemDecoration = new DividerItemDecoration(1);
        }
        newsList.setLayoutManager(layoutManager);
        newsList.addItemDecoration(itemDecoration);
        newsList.setAdapter(newsAdapter);
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

    List<ControlItem> getControls() {
        String[] filterNames = getResources().getStringArray(R.array.filters);
        String[] sortingNames = getResources().getStringArray(R.array.sorting);
        List<ControlItem> controlItems = new ArrayList<>();

        controlItems.add(new HeaderItem(getString(R.string.header_filter)));
        controlItems.add(new FilterItem(filterNames[0], Filter.COMMON));
        controlItems.add(new FilterItem(filterNames[1], Filter.GEEK));
        controlItems.add(new FilterItem(filterNames[2], Filter.RECENT));

        controlItems.add(new HeaderItem(getString(R.string.header_sorting)));
        controlItems.add(new SortingItem(sortingNames[0], Sorting.PRIORITY));
        controlItems.add(new SortingItem(sortingNames[1], Sorting.DATE));
        controlItems.add(new SortingItem(sortingNames[2], Sorting.COMMENT));
        controlItems.add(new SortingItem(sortingNames[3], Sorting.LIKE));

        return controlItems;
    }

    int getCurrentFilters() {
        return 7;
    }
}
