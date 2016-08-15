package fili.alex.newsradiot.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Filter.ALL, Filter.COMMON, Filter.GEEK})
@Retention(RetentionPolicy.SOURCE)
public @interface Filter {
    int ALL = 0;
    int COMMON = 1;
    int GEEK = 2;
}

