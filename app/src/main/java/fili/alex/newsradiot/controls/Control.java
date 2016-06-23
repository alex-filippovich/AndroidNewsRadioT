package fili.alex.newsradiot.controls;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Control.FILTER, Control.SORTING, Control.HEADER})
@Retention(RetentionPolicy.SOURCE)
public @interface Control {
    int FILTER = 0;
    int SORTING = 1;
    int HEADER = 2;
}