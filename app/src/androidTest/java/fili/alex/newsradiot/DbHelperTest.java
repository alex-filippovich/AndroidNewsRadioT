package fili.alex.newsradiot;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import fili.alex.newsradiot.data.NewsDbHelper;
import fili.alex.newsradiot.model.NewsObject;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DbHelperTest {

    private NewsDbHelper helper;

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(NewsDbHelper.DATABASE_NAME);
        helper = new NewsDbHelper(getTargetContext());
    }

    @Test
    public void save() throws Exception {
        List<NewsObject> listTo = getNewsObjects();

        helper.open().addAll(listTo);

        helper.close();

        List<NewsObject> listFrom = helper.openForRead().getAll();
        helper.close();

        assertTrue(listTo.size() == listFrom.size());
    }

    List<NewsObject> getNewsObjects() {
        List<NewsObject> newsObjects = new ArrayList<>();

        NewsObject newsObject1 = new NewsObject();
        newsObject1.id = "1";
        newsObject1.title = "title 1";
        newsObject1.snippet = "snippet 1";

        NewsObject newsObject2 = new NewsObject();
        newsObject2.id = "2";
        newsObject2.title = "title 2";
        newsObject2.snippet = "snippet 2";

        NewsObject newsObject3 = new NewsObject();
        newsObject3.id = "3";
        newsObject3.title = "title 3";
        newsObject3.snippet = "snippet 3";

        NewsObject newsObject4 = new NewsObject();
        newsObject4.id = "4";
        newsObject4.title = "title 4";
        newsObject4.snippet = "snippet 4";

        NewsObject newsObject5 = new NewsObject();
        newsObject5.id = "5";
        newsObject5.title = "title 5";
        newsObject5.snippet = "snippet 5";

        newsObjects.add(newsObject1);
        newsObjects.add(newsObject2);
        newsObjects.add(newsObject3);
        newsObjects.add(newsObject4);
        newsObjects.add(newsObject5);

        return newsObjects;
    }
}
