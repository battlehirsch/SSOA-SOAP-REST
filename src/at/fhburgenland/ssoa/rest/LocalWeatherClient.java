package at.fhburgenland.ssoa.rest;

import at.fhburgenland.ssoa.common.ConstantHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Norbert.Fesel on 18.12.2016.
 * REST-Client welcher den LocalWeatherService von "WorldWeatherOnline" anspricht.
 */
public class LocalWeatherClient {
    /**
     * Versucht den Wetter-Service von "WorldWeatherOnline" mit den zuvor eingegebenen Paramtern abzufragen.
     * Ist der API Key ungültig, oder wird kein Ort gefunden, kommt es zu einer Fehlermeldung.
     * @param key
     * gültiger API Key von WorldWeatherOnline
     * @param location
     * gewünschter Ort der Wetterabfrage als Name
     * @return
     * Retourniert die Antwort als String im JSON-Format.
     * @throws Exception
     * Fehlermeldung wird absichtlich geworfen, da sich im ServiceConsumer um eine korrekte Fehlerausgabe gekümmert wird.
     */
    public String callLocalWeatherAPI(String key, String location) throws Exception {
        StringBuffer response = new StringBuffer();
        String apiURL = ConstantHelper.WEATHER_URL;
        apiURL += "key=" + URLEncoder.encode(key);
        apiURL += "&q=" + URLEncoder.encode(location);
        apiURL += "&num_of_days=3";
        apiURL += "&format=json";

        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        } catch (Exception e) {
            throw e;
        }
        return response.toString();
    }

    /**
     * Verarbeitet die Antwort einer erfolgreichen Abfrage der "WorldWeatherOnline" API und zeigt relevante Informationen in der Konsole an.
     * @param result
     * Ergebnis der Abfrage als String im JSON-Format.
     */
    public void printCurrentCondition(String result) {
        try {
            JSONObject jsonResult = JSONObject.fromObject(result);
            JSONObject currentCondition = jsonResult.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0);
            JSONArray weather = jsonResult.getJSONObject("data").getJSONArray("weather");

            String observationTime = currentCondition.getString("observation_time");
            String tempC = currentCondition.getString("temp_C");
            String feelsLikeC = currentCondition.getString("FeelsLikeC");
            String windSpeedInKmph = currentCondition.getString("windspeedKmph");
            String humidityInPercent = currentCondition.getString("humidity");

            System.out.println("Current condition at " + observationTime + ":");
            System.out.println("The current temperature in degree Celsius is " + tempC + ", but it feels like " + feelsLikeC + " degrees Celsius!");
            System.out.println("Windspeed: " + windSpeedInKmph + " km/h");
            System.out.println("Humidity in %: " + humidityInPercent + "\n");

            System.out.println("Weather forecast for the following three days:");

            for (int i = 0; i < weather.size(); i++) {
                JSONObject obj = weather.getJSONObject(i);
                System.out.println("Date: " + obj.getString("date"));
                System.out.println("Maximum temperature in degree Celsius: " + obj.getString("maxtempC"));
                System.out.println("Minimum temperature in degree Celsius: " + obj.getString("mintempC") + "\n");
            }

        } catch (Exception e) {
            System.out.println("The following error occurred while evaluating the data from the weather service:");
            System.out.println(e.getMessage() + "\n");
        }
    }
}
