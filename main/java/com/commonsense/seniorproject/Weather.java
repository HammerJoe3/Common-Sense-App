/* A class for pulling weather from the OpenWeatherMap API and storing it
Developed by Darron Herbert 5/9/2019 */
package com.commonsense.seniorproject;

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
    String result_message;

    Double result_temp;
    Double result_pressure;
    Double result_humidity;

    /*Constructor
    @params: String - Zip Code the user wants the weather for */
    public Weather(String zipCode)
    {
        setZip(zipCode);
    }

    /*Sets the Zip Code and connects to the API
    @params: String - Zip Code the user wants the weather for*/
    private void setZip(String z)
    {
        //The url of the API
        urlInitial = "https://api.openweathermap.org/data/2.5/weather?zip=" + z + "&appid=YOURAPIKEYHERE";

        String result = "";

        //Attempts to connect to the API with http
        //If it fails to connect it'll log an error and store nothing
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

    /*Parses the JSON from the API and stores the needed info
    @params: String - the JSON to be parsed*/
    private void ParseResult(String json) throws JSONException
    {
        JSONObject jsonObject = new JSONObject(json);

        JSONArray JSONArray_weather = jsonObject.getJSONArray("weather");
        JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
        result_main = JSONObject_weather.getString("main");
        result_description = JSONObject_weather.getString("description");

        JSONObject JSONObject_main = jsonObject.getJSONObject("main");
        result_temp = JSONObject_main.getDouble("temp");
        result_pressure = JSONObject_main.getDouble("pressure");
        result_humidity = JSONObject_main.getDouble("humidity");
    }

    //Returns the main weather description
    public String getMain()
    {
        return result_main;
    }

    //Returns a more detailed weather description
    public String getDescription()
    {
        return result_description;
    }

    //Returns the temperature
    public double getTemperature()
    {
        return ((result_temp - 273.15) * (9.0/5.0) + 32) ;
    }

    //Returns the pressure
    public double getPressure()
    {
        return result_pressure;
    }

    //Returns the humidity
    public double getHumidity()
    {
        return result_humidity;
    }

    //TODO: Make this work
    //Returns a message based on weather conditions
    public String getWeatherMessage()
    {
        if(result_temp > 85)
        {
            result_message = "Make sure you wear sunscreen today!";
        }
        else if(result_temp < 30)
        {
            result_message = "Be careful of any ice!";
        }
        else if(result_temp < 50)
        {
            result_message = "Use Common Sense to avoid bad germs!";
        }
        else
        {
            result_message = "Make sure you protect yourself with Common Sense!";
        }

        return result_message;
    }
}
