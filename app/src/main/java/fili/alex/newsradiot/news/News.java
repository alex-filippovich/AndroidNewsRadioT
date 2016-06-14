package fili.alex.newsradiot.news;

public class News {
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


    public String info() {
        String info = "";
        info += link != null ? link.replaceAll("w{3}\\.", "") : "";
        info += ts != null ? " • " + ts : "";
        return info;
    }
}
