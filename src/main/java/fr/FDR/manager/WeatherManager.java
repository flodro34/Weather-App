package fr.FDR.manager;

import fr.FDR.pojo.Weather;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Getter
@Setter
public class WeatherManager {

    private Weather weatherDisplayed;

    public WeatherManager(String city){
        this.weatherDisplayed = new Weather(city);
        this.changeCity(city);
    }


    public void getWeather(){

        JSONObject json = new JSONObject();

        String token = "34b26c7dd55d7717fcd92947bf205f38";

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        int day = 0;

        try{
            json =  readJsonFromUrl("https://api.openweathermap.org/data/2.5/weather?q=" + this.weatherDisplayed.getCity() + "&appid=34b26c7dd55d7717fcd92947bf205f38&units=metric");
        }catch(IOException e){
            return;
        }

        if(json != null) {


            adaptWeatherToNewCity(json);

            calendar.add(Calendar.DATE, day);
            //formatage de la date
            this.weatherDisplayed.setDay(sdf.format(calendar.getTime()));
        }
    }

    private void adaptWeatherToNewCity(JSONObject json) {
        JSONObject jsonToDisplay = new JSONObject();
        jsonToDisplay = json.getJSONObject("main");
        this.weatherDisplayed.setTemperature(jsonToDisplay.getDouble("temp"));
        this.weatherDisplayed.setPressure(jsonToDisplay.get("pressure").toString()) ;
        this.weatherDisplayed.setHumidity(jsonToDisplay.get("humidity").toString()) ;

        jsonToDisplay = json.getJSONObject("wind");
        this.weatherDisplayed.setWindSpeed(jsonToDisplay.get("speed").toString());

        jsonToDisplay = json.getJSONObject("clouds");
        this.weatherDisplayed.setCloudiness(jsonToDisplay.get("all").toString());

        jsonToDisplay = json.getJSONArray("weather").getJSONObject(0);
        this.weatherDisplayed.setIcon(jsonToDisplay.getString("icon"));
        this.weatherDisplayed.setDescription(jsonToDisplay.getString("description").toString());
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
                "day='" + this.weatherDisplayed.getDay() + '\'' +
                ", temperature=" + this.weatherDisplayed.getTemperature() +
                ", pressure='" + this.weatherDisplayed.getPressure() + '\'' +
                ", humidity='" + this.weatherDisplayed.getHumidity() + '\'' +
                '}';
    }

    public void changeCity(String city) {
        this.weatherDisplayed.setCity(city);
        this.getWeather();
    }
}
