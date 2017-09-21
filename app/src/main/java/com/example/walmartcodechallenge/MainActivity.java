package com.example.walmartcodechallenge;

import android.app.Activity;
import android.icu.util.TimeZone;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.walmartcodechallenge.model.ForecastCondition;
import com.example.walmartcodechallenge.model.WeatherData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final static String API_KEY="1db5a3782d490009";
    final static String ZIP_CODE="30005";
    public WeatherData weatherData;
    Call<WeatherData> weatherDataCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestAPI restAPI = BaseURL.getAPI();
        weatherDataCall = restAPI.getAll(API_KEY,ZIP_CODE);

        new GetData().execute();
    }

    public class GetData extends AsyncTask<Void,Void,WeatherData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected WeatherData doInBackground(Void... voids) {
            try {
                Response<WeatherData> response= weatherDataCall.execute();
                weatherData = response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return weatherData;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(WeatherData weatherData) {
            super.onPostExecute(weatherData);
            Date currentTime = Calendar.getInstance().getTime();
            ((TextView) findViewById(R.id.cityState)).setText(weatherData.getCurrentObservation().getDisplayLocation().getFull());
            ((TextView) findViewById(R.id.tempDisplay)).setText(weatherData.getCurrentObservation().getCelcius()+(char) 0x00B0);
            ((TextView) findViewById(R.id.weatherType)).setText(weatherData.getCurrentObservation().getWeather());
            if(Float.parseFloat(weatherData.getCurrentObservation().getCelcius())<25){
                ((ConstraintLayout) findViewById(R.id.mainDisplay)).setBackgroundColor(getResources().getColor(R.color.lightBlue));
            }else{
                ((ConstraintLayout) findViewById(R.id.mainDisplay)).setBackgroundColor(getResources().getColor(R.color.saffron));
            }
            List<ForecastCondition> full = new ArrayList<ForecastCondition>();
            List<ForecastCondition> today = new ArrayList<ForecastCondition>();
            List<ForecastCondition> tomorrow = new ArrayList<ForecastCondition>();
            List<ForecastCondition> dayAfterTomorrow = new ArrayList<ForecastCondition>();

            full = weatherData.getForecastCondition();
            for(ForecastCondition temp: full){
                if(Integer.parseInt(temp.getFactTime().getMon())==(currentTime.getMonth()+1)){
                    if(Integer.parseInt(temp.getFactTime().getMday())==currentTime.getDate()){
                        today.add(temp);
                    } else if (Integer.parseInt(temp.getFactTime().getMday())==(currentTime.getDate()+1)){
                        tomorrow.add(temp);
                    }else if (Integer.parseInt(temp.getFactTime().getMday())==(currentTime.getDate()+2)){
                        dayAfterTomorrow.add(temp);
                    }
                }
            }

            GridView todayGridView = (GridView) findViewById(R.id.todayGrid);
            GridView tomorrowGridView = (GridView) findViewById(R.id.tomorrowGrid);
            GridView dayAfterTomorrowGridView = (GridView) findViewById(R.id.dayAfterTomorrowGrid);

            GridAdapter todayGrid = new GridAdapter(getApplicationContext(),today);
            GridAdapter tomorrowGrid = new GridAdapter(getApplicationContext(),tomorrow);
            GridAdapter dayAfterTomorrowGrid = new GridAdapter(getApplicationContext(),dayAfterTomorrow);

            todayGridView.setAdapter(todayGrid);
            tomorrowGridView.setAdapter(tomorrowGrid);
            dayAfterTomorrowGridView.setAdapter(dayAfterTomorrowGrid);
        }
    }


}
