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

/**
 * Created by siuant on 9/25/2016.
 */
public class MainCityFragment extends android.support.v4.app.Fragment {
    private TextView commentText;
    Context context;
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
        commentText = (TextView)getView().findViewById(R.id.comment);
        commentText.setText("comment123");

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            JSONWeatherTask task = new JSONWeatherTask();
            //task.execute(new String[]{city_string});
            task.execute(CityName.Chicago);
        } else {
            commentText.setText("No network connection available.");
        }

    } // end onActivityCreated

    private class JSONWeatherTask extends AsyncTask<CityName, Void, Weather> {
        String teststring = "hey";

        @Override
        protected Weather doInBackground(CityName... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(context, params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                //weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

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
            commentText.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");

        } // end onPostExecute
    } // end JSONWeatherTask

} // end MainCityFragment
