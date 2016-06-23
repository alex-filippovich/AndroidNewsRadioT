package fili.alex.newsradiot.news;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private int margin;
    private int numberColumn;

    public GridItemDecoration(Context context, int numberColumn, int margin) {
        this.numberColumn = numberColumn;
        this.margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, margin, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int column = position % numberColumn;

        outRect.left = margin - column * margin / numberColumn;
        outRect.right = (column + 1) * margin / numberColumn;
        outRect.top = position < numberColumn ? margin : 0;
        outRect.bottom = margin;
    }
}
