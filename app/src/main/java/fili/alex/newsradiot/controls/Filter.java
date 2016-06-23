package fili.alex.newsradiot.controls;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Filter.COMMON, Filter.GEEK, Filter.RECENT})
@Retention(RetentionPolicy.SOURCE)
public @interface Filter {
    int COMMON = 0;
    int GEEK = 1;
    int RECENT = 2;
}

