package fili.alex.newsradiot.model;


import android.text.Html;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsObject {
    public static final String LOCAL_ID = "_id";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String SNIPPET = "snippet";
    public static final String PIC = "pic";
    public static final String AUTHOR = "author";
    public static final String LINK = "link";
    public static final String TS = "ts";
    public static final String ATS = "ats";
    public static final String ACTIVE = "active";
    public static final String ACTIVETS = "activets";
    public static final String GEEK = "geek";
    public static final String VOTES = "votes";
    public static final String LIKES = "likes";
    public static final String DEL = "del";
    public static final String SLUG = "slug";
    public static final String EXTTITLE = "exttitle";
    public static final String COMMENTS = "comments";
    public static final String ORIGLINK = "origlink";
    public static final String DOMAIN = "domain";
    public static final String POSITION = "position";
    public static final String ID = "id";

    public long _id;
    public String title;
    public String content;
    public String snippet;
    public String pic;
    public String author;
    public String link;
    public String ts;
    public String ats;
    public boolean active;
    public String activets;
    public boolean geek;
    public int votes;
    public boolean del;
    public String slug;
    public String exttitle;
    public String id;
    public int comments;
    public int likes;
    public String origlink;
    public String domain;
    public int position;

    private static SimpleDateFormat formatFrom;
    private static SimpleDateFormat formatTo;

    static {
        formatFrom = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        formatTo = new SimpleDateFormat("HH:MM", Locale.US);
    }

    public String getDomain() {
        return domain != null ? domain.replaceAll("w{3}\\.", "") : "";
    }

    public String ts() {
        if (ts != null) {

            try {
                Date date = formatFrom.parse(ts);
                return formatTo.format(date);
            } catch (ParseException e) {
                return "";
            }
        }

        return "";
    }

    public String getSnippet() {
        if (snippet != null) {
            return Html.fromHtml(snippet).toString();
        }
        return null;
    }
}
