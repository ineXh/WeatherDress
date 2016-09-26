package com.badboysstudio.weatherdress;


import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badboysstudio.weatherdress.model.Weather;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by siuant on 9/25/2016.
 */
public class MainCityFragment extends android.support.v4.app.Fragment {
    private TextView commentText;
    Context context;
    ArrayList<City> cities;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_old, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        context = getActivity();
        View view = getView();
        commentText = (TextView)getView().findViewById(R.id.comment);
        commentText.setText("comment123");

        cities = new ArrayList<City>();
        cities.add(new City(context, view, CityName.Chicago));
        cities.add(new City(context, view, CityName.Seattle));
        cities.add(new City(context, view, CityName.NewYork));

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            JSONWeatherTask task = new JSONWeatherTask();
            //task.execute(new String[]{city_string});
            //task.execute(CityName.Chicago);
            //for(City  city : cities){
                //task.execute(city);
            //}
            task.execute(cities.get(0), cities.get(1));
        } else {
            commentText.setText("No network connection available.");
        }

    } // end onActivityCreated

    private class JSONWeatherTask extends AsyncTask<City, Void, Weather> {
        String teststring = "hey";
        City city;

        @Override
        protected Weather doInBackground(City... params) {
            Weather weather = new Weather();
            this.city = params[0];
            String data = ( (new WeatherHttpClient()).getWeatherData(context, params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        } // end doInBackground

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            String string = teststring;

            /*if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");*/
            //commentText.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
            city.setTemperatureText(Math.round((weather.temperature.getTemp() - 273.15)));
        } // end onPostExecute
    } // end JSONWeatherTask

} // end MainCityFragment
