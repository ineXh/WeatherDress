package com.badboysstudio.weatherdress;

import com.badboysstudio.weatherdress.model.Weather;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TextView commentText;
    private Context context;
    ArrayList<City> cities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //http://api.openweathermap.org/data/2.5/weather?q=vancouver,canada&APPID=a70c9988f0ae23aca5840d9f6d957920
        /*
        {"coord":{"lon":-123.12,"getWeatherDatalat":49.25},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10d"}],"base":"stations","main":{"temp":288.9,"pressure":1028,"humidity":55,"temp_min":285.37,"temp_max":292.04},"wind":{"speed":1.54,"deg":100},"rain":{"1h":0.51},"clouds":{"all":64},"dt":1474761579,"sys":{"type":3,"id":37665,"message":0.0175,"country":"CA","sunrise":1474812258,"sunset":1474855355},"id":6173331,"name":"Vancouver","cod":200}
         */
        context = this;
        ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle("123");

        /*Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);*/

        commentText = (TextView)findViewById(R.id.comment);
        commentText.setText("comment123");

        //String city = "Vancouver,Canada";
        //String city = "chicago,us";
        CityName city = CityName.Chicago;
        String city_string = "newyork,us";


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            JSONWeatherTask task = new JSONWeatherTask();
            //task.execute(new String[]{city_string});
            task.execute(CityName.Chicago);
        } else {
            commentText.setText("No network connection available.");
        }
    } // end onCreate

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } // end onOptionsItemSelected

} // end MainActivity
