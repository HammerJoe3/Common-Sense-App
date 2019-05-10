/*A class for obtaining information from the Weather class
and displaying the information in the Android app
Developed by Darron Herbert 5/9/2019*/
package com.commonsense.seniorproject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class WeatherFragment extends Fragment
        implements TextView.OnEditorActionListener
{
    //Sets up variables for each View
    private View view;
    private EditText zipCodeEditText;
    private TextView weatherTextView;
    private TextView tempTextView;
    private TextView humidTextView;
    private TextView pressTextView;
    private TextView messageTextView;
    private boolean active;
    private String zipCode = "";
    private String weather = "";
    private String temp = "";
    private String humidity = "";
    private String pressure = "";
    private String message = "";

    private SharedPreferences prefs;

    /*Runs when the fragment is created
    Sets the Layout and instantiates the View variables*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_weather, container, false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        zipCodeEditText = (EditText) view.findViewById(R.id.textZipCode);
        weatherTextView = (TextView) view.findViewById(R.id.textWeather);
        tempTextView = (TextView) view.findViewById(R.id.textTemp);
        humidTextView = (TextView) view.findViewById(R.id.textHumidity);
        pressTextView = (TextView) view.findViewById(R.id.textPressure);
        messageTextView = (TextView) view.findViewById(R.id.textWeatherMessage);

        zipCodeEditText.setOnEditorActionListener(this);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return view;
    }

    /*Runs when the fragment is switched away from.
    Stores the current information to recall later*/
    @Override
    public void onPause()
    {
        Editor editor= prefs.edit();
        editor.putString("weather", weather);
        editor.putString("zipCode", zipCode);
        editor.putString("temp", temp);
        editor.putString("humidity", humidity);
        editor.putString("pressure", pressure);
        editor.putBoolean("active", active);


        super.onPause();
    }

    /*Called when the app is reopened after being paused.
    Restores the saved variables*/
    @Override
    public void onResume()
    {
        super.onResume();

        weather = prefs.getString("weather", "");
        zipCode = prefs.getString("zipCode", "");
        temp = prefs.getString("zipCode", "");
        humidity = prefs.getString("zipCode", "");
        pressure = prefs.getString("zipCode", "");
        active = prefs.getBoolean("active", false);

        setWeather();
    }

    /*Creates a weather object and stores the weather data from the object.
    Won't store anything if the Zip Code is invalid*/
    private void getWeather()
    {
        zipCode = zipCodeEditText.getText().toString();

        Weather w = new Weather(zipCode);

        weather = w.getMain();
        
        if(weather == null)
        {
            messageTextView.setText("Invalid Zip Code");
            return;
        }

        active = true;
        
        temp = String.format("%.2f", w.getTemperature());
        humidity = String.format("%.0f", w.getHumidity());
        pressure = String.format("%.0f", w.getPressure());

        setWeather();
    }

    /*Sets the TextViews with the weather data that was obtained*/
    private void setWeather()
    {
        if(active)
        {
            weatherTextView.setText(weather);
            tempTextView.setText(temp + "' F");
            humidTextView.setText(humidity + "%");
            pressTextView.setText(pressure + " hPa");
            messageTextView.setText(message);
        }
    }

    /*Action listener for when the user hits enter on the EditText*/
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
    {
        int keyCode = -1;
        if (event != null) {
            keyCode = event.getKeyCode();
        }

        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED ||
                keyCode == KeyEvent.KEYCODE_DPAD_CENTER ||
                keyCode == KeyEvent.KEYCODE_ENTER) {
            getWeather();
        }
        return false;
    }
}
