package com.mkgroupe.root.clima;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 12/16/17.
 */

public class WeatherDataModel {
    //Variables
    protected int mTemperature;
    protected String mCity;
    protected int mCondition;
    protected String mIconName;


    public static WeatherDataModel formJSON(JSONObject object) throws JSONException {
        WeatherDataModel data=new WeatherDataModel();
        data.mCity = object.getString("name");
        data.mCondition = object.getJSONArray("weather").getJSONObject(0).getInt("id");
        data.mTemperature = (int) (object.getJSONObject("main").getDouble("temp")-272.65);
        data.mIconName = updateWeatherIcon(data.mCondition);
        return data;
    }


    // TODO: Uncomment to this to get the weather image name from the condition:
    private static String updateWeatherIcon(int condition) {

        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
           return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
       }

        return "dunno";
    }

    public int getTemperature() {
        return mTemperature;
    }

    public String getCity() {
        return mCity;
    }

    public String getIconName() {
        return mIconName;
    }
}
