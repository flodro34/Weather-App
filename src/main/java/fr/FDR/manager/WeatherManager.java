package fr.FDR.manager;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Getter
@Setter
public class WeatherManager {

    private String city;
    private String day;
    private Integer temperature;
    private String icon;
    private String description;
    private String windSpeed;
    private String cloudiness;
    private String pressure;
    private String humidity;

    public WeatherManager(String city){
        this.city = city;
    }

    public void getWeather(){

        JSONObject json = new JSONObject();
        JSONObject jsonToDisplay = new JSONObject();

        String token = "34b26c7dd55d7717fcd92947bf205f38";

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        int day = 0;

        try{
            json =  readJsonFromUrl("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=34b26c7dd55d7717fcd92947bf205f38");
        }catch(Exception e){
            return;
        }

        if(json != null) {
            jsonToDisplay = json.getJSONObject("main");
            this.temperature = jsonToDisplay.getInt("temp");
            this.pressure = jsonToDisplay.getString("pressure");
            this.humidity = jsonToDisplay.getString("humidity");

            jsonToDisplay = json.getJSONObject("wind");
            this.windSpeed = jsonToDisplay.getString("speed");

            jsonToDisplay = json.getJSONObject("clouds");
            this.cloudiness = jsonToDisplay.getString("all");

            jsonToDisplay = json.getJSONArray("weather").getJSONObject(0);
            this.icon = jsonToDisplay.getString("icon");
            this.description = jsonToDisplay.getString("description");

            calendar.add(Calendar.DATE, day);
            this.day = sdf.format(calendar.getTime());
        }
    }

    private JSONObject readJsonFromUrl(String url) throws IOException, JSONException{
        JSONObject json = null;
        try(InputStream is = new URL(url).openStream()){
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            json = new JSONObject(jsonText);
        }
        return json;
    }

    private String readAll(BufferedReader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while((cp = rd.read()) != -1 ){
            sb.append((char) cp);
        }

        return sb.toString();

    }

    @Override
    public String toString() {
        return "WeatherManager{" +
                "day='" + day + '\'' +
                ", temperature=" + temperature +
                ", pressure='" + pressure + '\'' +
                ", humidity='" + humidity + '\'' +
                '}';
    }
}
