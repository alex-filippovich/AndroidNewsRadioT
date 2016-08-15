package fili.alex.newsradiot.component;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ImageSpan;

import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Arrays;

import fili.alex.newsradiot.App;
import fili.alex.newsradiot.R;
import fili.alex.newsradiot.model.NewsObject;
import fili.alex.newsradiot.util.AndroidHelper;

public class NewsCellView extends View {
    private static TextPaint titlePaint;
    private static TextPaint descriptionPaint;
    private static Paint iconPaint;
    private static RectF iconRectF;
    private static Paint linePaint;
    private static TextPaint domainPaint;

    private StaticLayout titleLayout;
    private StaticLayout descriptionLayout;
    private StaticLayout domainLayout;
    private StaticLayout countLikesLayout;
    private StaticLayout countCommentsLayout;

    private NewsObject news;

    private static Drawable geekDrawable;
    private static Drawable likesDrawable;
    private static Drawable commentsDrawable;
    private int maxWidth, maxHeight;
    private int heightTitle;
    private int heightDescription = 0;
    private boolean useSeparator;
    boolean hasLink;
    private int widthLikes;
    private int widthComments;
    private int rightComments;
    private int rightLikes;
    private Bitmap bitmap;

    public NewsCellView(Context context, int width) {
        super(context);

        if (titlePaint == null) {
            titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            titlePaint.setSubpixelText(true);
            titlePaint.setTextSize(dp(18));

            titlePaint.setTypeface(Typeface.DEFAULT_BOLD);

            descriptionPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            descriptionPaint.setTextSize(dp(18));

            iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            iconPaint.setColor(0xffd9d9d9);

            iconRectF = new RectF();

            geekDrawable = ContextCompat.getDrawable(context, R.drawable.glasses);
            geekDrawable.setBounds(0, 0, dp(18), dp(18));

            likesDrawable = ContextCompat.getDrawable(context, R.drawable.heart);
            likesDrawable.setBounds(0, 0, dp(14), dp(14));

            commentsDrawable = ContextCompat.getDrawable(context, R.drawable.comment);
            commentsDrawable.setBounds(0, 0, dp(14), dp(14));

            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setColor(0xffbdbdbd);

            domainPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            domainPaint.setColor(0xff969696);
            domainPaint.setTextSize(dp(13));
        }

        maxWidth = width;
    }

    public void setNews(NewsObject news, boolean useSeparator) {
        this.news = news;
        this.useSeparator = useSeparator;

        heightTitle = 0;
        heightDescription = 0;
        titleLayout = null;
        descriptionLayout = null;
        countCommentsLayout = null;
        countLikesLayout = null;
        widthLikes = 0;
        widthComments = 0;
        rightLikes = 0;
        rightComments = 0;

        hasLink = news.pic != null && !news.pic.isEmpty();

        if (hasLink) {
            Picasso picasso = Picasso
                    .with(App.appContext);

            picasso.setLoggingEnabled(true);

            picasso.load(news.pic)
                    .resize(dp(68), dp(68))
                    .centerCrop()
                    .into(target);
        }

        if (news.title != null) {
            int widthTitle = hasLink ? maxWidth - dp(32) - dp(68) - dp(8) : maxWidth - dp(32);
            SpannableStringBuilder ssTitle = new SpannableStringBuilder(news.title);
            if (news.geek) {
                ssTitle.insert(0, "  ");
                ssTitle.setSpan(new ImageSpan(geekDrawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            titleLayout = new StaticLayout(ssTitle, titlePaint, widthTitle, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true);
            heightTitle = titleLayout.getHeight();
        }

        String snippet = news.getSnippet();
        if (snippet != null) {
            int widthDescription;
            SpannableStringBuilder ssDescription;

            int maxDescriptionWidth = maxWidth - dp(32);
            if (hasLink && heightTitle < dp(68)) {
                int lines = (dp(68) - heightTitle) / dp(16);
                widthDescription = maxDescriptionWidth - dp(32) - dp(68) - dp(16);
                ssDescription = divideStringByLines(snippet, descriptionPaint, widthDescription, lines);
            } else {
                ssDescription = new SpannableStringBuilder(snippet);
            }

            descriptionLayout = new StaticLayout(ssDescription, descriptionPaint, maxDescriptionWidth, Layout.Alignment.ALIGN_NORMAL, 1f, 1.5f, true);
            heightDescription = descriptionLayout.getHeight();
        }

        StringBuilder sbDomain = new StringBuilder();
        if (news.domain != null) {
            sbDomain.append(news.getDomain());
        }
        if (news.ts != null) {
            if (sbDomain.length() > 0) sbDomain.append(" • ");
            sbDomain.append(news.ts());
        }

        int widthDomain = (int) domainPaint.measureText(sbDomain.toString());
        domainLayout = new StaticLayout(sbDomain, domainPaint, widthDomain, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true);

        if (news.comments > 0) {
            String countLikes = String.valueOf(news.comments);
            widthComments = (int) domainPaint.measureText(countLikes);
            countCommentsLayout = new StaticLayout(countLikes, domainPaint, widthComments, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true);
        }

        if (news.likes > 0) {
            String countLikes = String.valueOf(news.likes);
            widthLikes = (int) domainPaint.measureText(countLikes);
            countLikesLayout = new StaticLayout(countLikes, domainPaint, widthLikes, Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true);
        }


        maxHeight = dp(16) * 2 + Math.max(heightTitle + heightDescription, dp(68)) + dp(14) + dp(5);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTitle(canvas);
        drawIcon(canvas);
        drawDescription(canvas);
        drawDomain(canvas);
        drawComment(canvas);
        drawLikes(canvas);
        drawSeparator(canvas);
    }

    private void drawTitle(Canvas canvas) {
        if (titleLayout != null) {
            canvas.save();
            canvas.translate(dp(16), dp(16));
            titleLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawDescription(Canvas canvas) {
        if (descriptionLayout != null) {
            canvas.save();
            canvas.translate(dp(16), dp(16) + heightTitle);
            descriptionLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawIcon(Canvas canvas) {
        if (hasLink && bitmap != null) {
            int size = dp(68);
//            iconRectF.left = getMeasuredWidth() - size - dp(16);
//            iconRectF.top = dp(18);
//            iconRectF.right = iconRectF.left + size;
//            iconRectF.bottom = iconRectF.top + size;

            Rect rect = new Rect();
            rect.left = getMeasuredWidth() - size - dp(16);
            rect.top = dp(18);
            rect.right = (int) (iconRectF.left + size);
            rect.bottom = (int) (iconRectF.top + size);

//            canvas.drawRoundRect(iconRectF, dp(3), dp(3), iconPaint);

            canvas.drawBitmap(getRoundedCornerBitmap(bitmap), getMeasuredWidth() - size - dp(16), dp(18), iconPaint);
        }
    }

    private void drawDomain(Canvas canvas) {
        if (domainLayout != null) {
            canvas.save();
            canvas.translate(dp(16), maxHeight - dp(16) - dp(14));
            domainLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawComment(Canvas canvas) {
        rightComments = maxWidth - dp(16);
        if (countCommentsLayout != null) {
            canvas.save();
            canvas.translate(rightComments - widthComments, maxHeight - dp(18.5f) - dp(11));
            countCommentsLayout.draw(canvas);
            canvas.restore();

            canvas.save();
            if (widthComments > 0) {
                rightComments -= (dp(16) + widthComments);
            }
            rightComments -= dp(4);

            canvas.translate(rightComments, maxHeight - dp(14) - dp(13));
            commentsDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private void drawLikes(Canvas canvas) {
        rightLikes = rightComments;
        if (widthComments > 0) {
            rightLikes -= dp(20);
        }
        if (countLikesLayout != null) {
            canvas.save();
            canvas.translate(rightLikes - widthLikes, maxHeight - dp(18.5f) - dp(11));
            countLikesLayout.draw(canvas);
            canvas.restore();

            if (widthLikes > 0) {
                rightLikes -= (dp(16) + widthLikes);
            }

            rightLikes -= dp(4);

            canvas.save();
            canvas.translate(rightLikes, maxHeight - dp(14) - dp(13));
            likesDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private void drawSeparator(Canvas canvas) {
        if (useSeparator) {
            canvas.drawLine(0, maxHeight - 0.5f, maxWidth, maxHeight - 0.3f, linePaint);
        }
    }


    private int dp(float value) {
        return AndroidHelper.dp(value);
    }

    private SpannableStringBuilder divideStringByLines(String str, TextPaint paint, int w, int maxLines) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        int start = 0;
        int breakChars;
        int l = str.length();

        String line;

        while (start != l && maxLines != 0) {
            breakChars = paint.breakText(str, start, l, true, w, null);

            if (str.charAt(start + breakChars - 1) != ' ' && (start + breakChars) != l) {
                breakChars = lastSpace(str.substring(start, start + breakChars));
            }
            line = str.substring(start, start + breakChars).trim();

            builder.append(line);
            start += breakChars;
            if (breakChars != l) {
                builder.append("\n");
            }
            maxLines--;
        }
        if (start != l) {
            builder.append(str.substring(start, l).trim());
        }

        return builder;
    }

    private int lastSpace(String str) {
        int l = str.length();

        for (int i = l - 1; i >= 0; i--) {
            char c = str.charAt(i);
            if (c == ' ') {
                return i;
            }
        }

        return 0;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        Picasso.with(App.appContext).cancelRequest(target);

        Log.d("NewsCellView", "onDetachedFromWindow");
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap loadBitmap, Picasso.LoadedFrom from) {
            bitmap = loadBitmap;

            Log.d("BitmapLoading", "from" + from.toString() + " success is " + (loadBitmap != null));
            invalidate();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.e("BitmapLoading", "error" );

            bitmap = null;
            invalidate();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            int size = dp(68);
            int[] colors = new int[size*size];
            Arrays.fill(colors, 0, size*size, 0xffd9d9d9);
            bitmap = Bitmap.createBitmap(colors, size, size, Bitmap.Config.RGB_565);
        }
    };

    public  Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffd9d9d9;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = dp(3);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
