package fili.alex.newsradiot.controls;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Sorting.PRIORITY, Sorting.DATE, Sorting.COMMENT, Sorting.LIKE})
@Retention(RetentionPolicy.SOURCE)
public @interface Sorting {
    int PRIORITY = 0;
    int DATE = 1;
    int COMMENT = 2;
    int LIKE = 3;
}
