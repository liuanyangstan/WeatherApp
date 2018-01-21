package com.stananyangliu.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView tempTextView;
    TextView dateTextView;
    TextView weatherDescTextView;
    TextView cityTextView;
    ImageView weatherImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = findViewById(R.id.tempTextView);
        dateTextView = findViewById(R.id.dateTextView);
        weatherDescTextView = findViewById(R.id.weatherDescTextView);
        cityTextView = findViewById(R.id.cityTextView);

        weatherImageView = findViewById(R.id.weatherImageView);

        dateTextView.setText(getCurrentDate());

        //String url = "http://api.openweathermap.org/data/2.5/weather?q=Detroit,us&appid=47427adbcd84964e42aa3d767696544d&units=Imperial";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Seattle,us&appid=47427adbcd84964e42aa3d767696544d&units=Imperial";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //tempTextView.setText("Response: " + response.toString());
                        Log.v("WEATHER", "Response" + response.toString());

                        try {
                            JSONObject mainJSONObject = response.getJSONObject("main");
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject firstWeatherObject = weatherArray.getJSONObject(0);

                            String temp = Integer.toString((int)Math.round(mainJSONObject.getDouble("temp")));
                            String weatherDescription = firstWeatherObject.getString("description");
                            String city = response.getString("name");

                            tempTextView.setText(temp);
                            weatherDescTextView.setText(weatherDescription);
                            cityTextView.setText(city);

                            //Updated image automatically.
                            //Find the weather icon base on the weather description we got and replaces
                            int iconResourceId = getResources().getIdentifier("icon_"
                                    + weatherDescription.replace(" ", ""), "drawable", getPackageName());
                            weatherImageView.setImageResource(iconResourceId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

        // Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
        String formattedDate = dateFormat.format(calendar.getTime());

        return formattedDate;
    }
}
