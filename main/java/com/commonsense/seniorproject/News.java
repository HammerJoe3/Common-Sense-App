package com.commonsense.seniorproject;
/*
Created By Dylan Shapiro 

*/

public class News {
    String link;
    String articleName;
    String date;
    /*
    constructor for news takes link name and date crawled
    */
    public News(String link, String articleName, String date) {
        this.link = link;
        this.articleName = articleName;
        this.date = date;
    }
    /*
    to string method prints out article name and link
    */
    @Override
    public String toString() {
        return "News [link=" + link + ", articleName=" + articleName + "]";
    }
    /*
    returns link
    */
    public String getLink() {
        return link;
    }
    /*
    sets the link
    */
    public void setLink(String link) {
        this.link = link;
    }
    /*
    gets the article name
    */
    public String getArticleName() {
        return articleName;
    }
    /*
    sets the article name 
    */
    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }
    

}
