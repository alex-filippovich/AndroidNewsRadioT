package fili.alex.newsradiot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.widget.TextView;

public class HeaderView extends TextView {
    private static Paint paint;
    private boolean showDivider;
    private int offsetTop;
    private int padding;

    static {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xff9e9e9e);
    }

    public HeaderView(Context context) {
        super(context);

        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics());

        offsetTop = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());

        setTextSize(16);
        setPadding(padding, padding, padding, padding);
        setTypeface(Typeface.DEFAULT_BOLD);
        setSingleLine(true);
        setMaxLines(1);
        setGravity(Gravity.CENTER_VERTICAL);
        setTextColor(0xff616161);
    }


    public void setShowDivider(boolean show) {
        showDivider = show;
        setPadding(padding, padding + offsetTop, padding, padding);
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, showDivider ? heightMeasureSpec + offsetTop : heightMeasureSpec);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (showDivider) {
            canvas.drawLine(0, offsetTop + 1, getMeasuredWidth(), offsetTop + 1, paint);
        }
    }
}
