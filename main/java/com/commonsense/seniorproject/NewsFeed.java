package com.commonsense.seniorproject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/*
Created By Dylan Shapiro 

*/


import java.util.ArrayList;
public class NewsFeed {
    ArrayList<News> newsList = new ArrayList<News>();
    public NewsFeed(){

    }
    /*
    constructor takes arraylist of news objects
    */
    public NewsFeed(ArrayList<News> newsList) {
        this.newsList = newsList;
    }
    /*
    to string prints all objects in the list
    */
    @Override
    public String toString() {
        return "NewsFeed [newsList=" + newsList + "]";
    }
    
    /*
    returns news list
    */
    public ArrayList<News> getNewsList() {
        return newsList;
    }
    
    /*
    sets the news list
    */
    public void setNewsList(ArrayList<News> newsList) {
        this.newsList = newsList;
    }
    /*
    parses html response from data base and returns it in a list of news objects
    */
    public NewsFeed getArticles(String l) {

        ArrayList<News> newsList = new ArrayList<News>();
        String str = l;
        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        for(int i=0; i<jsonarray.length(); i++){
            JSONObject obj = null;
            try {
                obj = jsonarray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String name = null;
            try {
                name = obj.getString("Title");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = null;
            try {
                url = obj.getString("Link");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String date = null;
            try {
                date = obj.getString("Date");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            newsList.add(new News(url, name, date));
        }
        NewsFeed news = new NewsFeed(newsList);
        return news;

    }



}
