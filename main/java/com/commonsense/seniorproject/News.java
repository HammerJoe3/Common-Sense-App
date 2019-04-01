package com.commonsense.seniorproject;


public class News {
    String link;
    String articleName;
    String date;

    @Override
    public String toString() {
        return "News [link=" + link + ", articleName=" + articleName + "]";
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public News(String link, String articleName, String date) {
        this.link = link;
        this.articleName = articleName;
        this.date = date;
    }

}