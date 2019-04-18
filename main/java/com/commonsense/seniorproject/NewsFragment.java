package com.commonsense.seniorproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.app.ProgressDialog;

import java.util.Map;
import java.util.HashMap;

import android.widget.ListView;

import android.app.Fragment;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    View view;
    ArrayList<News> list;
    ListView listView;
    private ProgressDialog pDialog;
    private SessionManager session;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
        // get the reference of Button
        listView= (ListView) view.findViewById(R.id.listview);

        // Progress Dialog
        pDialog = new ProgressDialog(view.getContext());
        pDialog.setCancelable(false);

        session = new SessionManager(view.getContext());

        getNewsFromDatabase();

        return view;
    }

    /**
     * Attempts to register the user and add them into the database.  If successful, return to login,
     * Otherwise, tell the user that the information was wrong and prompt the user to try again
     */


    private void getNewsFromDatabase() {
        //tag used to cancel the request
        String tag_string_req = "req_news";

        pDialog.setMessage("gathering articles...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, CommonSenseConfig.URL_NEWS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponse", "news response: " + response);
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in JSON
                    if (!error) {

                        String database_news_list = jObj.getString("News");

                        /* TODO: this is the solution, apparently
                           TODO: THIS IS REALLY UGLY AND STUPID, ANY OTHER WAY? */
                        NewsFeed news = new NewsFeed();
                        news = news.getArticles(database_news_list);
                        list = news.getNewsList();

                        final ArrayList<String> arrayList = new ArrayList<>();


                        for(int i =0; i< list.size(); i++) {
                            arrayList.add(list.get(i).getArticleName());
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_expandable_list_item_1, arrayList);
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                int itemPostion = position;
                                String value = (String) listView.getItemAtPosition(position);

                                int count;
                                for(count = 0; count< arrayList.size(); count++){
                                    if(value.equals(list.get(count).getArticleName())){
                                        Intent i = new Intent(Intent.ACTION_VIEW,
                                                Uri.parse(list.get(count).getLink()));
                                        startActivity(i);
                                    }
                                }
                            }
                        });
                        /* TODO: NEWS CODE ENDS HERE */

                    } else {
                        // error in login, get Error Message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(view.getContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(view.getContext(), "JSON error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "Login error: " + error.getMessage());
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to news url
                Map<String, String> params = new HashMap<String, String>();

                //params.put("userID",session.getUserID());

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

