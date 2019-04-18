package com.techno.herbert.weatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Weather
{
    String urlInitial;

    String result_main;
    String result_description;

    Double result_temp;
    Double result_pressure;
    Double result_humidity;

    public Weather(String zipCode)
    {
        setZip(zipCode);
    }

    private void setZip(String z)
    {
        urlInitial = "https://api.openweathermap.org/data/2.5/weather?zip=" + z + "&appid=2c4cfd9f46764d63df3915877c10b49a";

        String result = "";

        try {
            URL url_weather = new URL(urlInitial);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStreamReader inputStreamReader =
                        new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader =
                        new BufferedReader(inputStreamReader, 8192);
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                bufferedReader.close();

                ParseResult(result);

            } else {
                System.out.println("Error in httpURLConnection.getResponseCode()!!!");
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(Weather.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ParseResult(String json) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(json);

        //"weather"
        JSONArray JSONArray_weather = jsonObject.getJSONArray("weather");
        JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
        result_main = JSONObject_weather.getString("main");
        result_description = JSONObject_weather.getString("description");


        //"main"
        JSONObject JSONObject_main = jsonObject.getJSONObject("main");
        result_temp = JSONObject_main.getDouble("temp");
        result_pressure = JSONObject_main.getDouble("pressure");
        result_humidity = JSONObject_main.getDouble("humidity");
    }

    public String getMain()
    {
        return result_main;
    }

    public String getDescription()
    {
        return result_description;
    }

    public double getTemperature()
    {
        return ((result_temp - 273.15) * (9.0/5.0) + 32) ;
    }

    public double getPressure()
    {
        return result_pressure;
    }

    public double getHumidity()
    {
        return result_humidity;
    }
}
