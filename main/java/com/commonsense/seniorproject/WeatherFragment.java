package com.techno.herbert.weatherapp;

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
    private View view;
    private EditText zipCodeEditText;
    private TextView weatherTextView;
    private TextView tempTextView;
    private TextView humidTextView;
    private TextView pressTextView;

    private String zipCode = "";
    private String weather = "";
    private String temp = "";
    private String humidity = "";
    private String pressure = "";

    private SharedPreferences prefs;

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

        zipCodeEditText.setOnEditorActionListener(this);

        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return view;
    }

    @Override
    public void onPause()
    {
        Editor editor= prefs.edit();
        editor.putString("weather", weather);
        editor.putString("zipCode", zipCode);
        editor.putString("temp", temp);
        editor.putString("humidity", humidity);
        editor.putString("pressure", pressure);


        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        weather = prefs.getString("weather", "");
        zipCode = prefs.getString("zipCode", "");
        temp = prefs.getString("zipCode", "");
        humidity = prefs.getString("zipCode", "");
        pressure = prefs.getString("zipCode", "");

        setWeather();
    }

    private void getWeather()
    {
        zipCode = zipCodeEditText.getText().toString();

        Weather w = new Weather(zipCode);

        weather = w.getMain();
        temp = String.format("%.2f", w.getTemperature());
        humidity = String.format("%.0f", w.getHumidity());
        pressure = String.format("%.0f", w.getPressure());

        setWeather();
    }

    private void setWeather()
    {
        weatherTextView.setText(weather);
        tempTextView.setText(temp);
        humidTextView.setText(humidity);
        pressTextView.setText(pressure);
    }

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
