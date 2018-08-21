package com.example.roskata.newsappvol2;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Article {
    private String section;
    private String title;
    private String authorName;
    private String publishedDate;
    private String imageUrl;
    private String url;

    public Article( String section ,String title, String authorName, String publishedDate,  String imageUrl, String url) {
        this.section = section;
        this.title = title;
        this.authorName = authorName;
        this.publishedDate = publishedDate;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    public String getSection() { return section; }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        if (authorName != null) {
            return StringUtils.capitalize(authorName);
        }
        return null;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    private Date formatDate(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC+2");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFormattedDate() {
        Date date = formatDate(getPublishedDate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Article{" +
                "section='" + section + '\'' +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", authorName='" + authorName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
