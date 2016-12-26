package at.fhburgenland.ssoa.program;

import at.fhburgenland.ssoa.rest.LocalWeatherClient;
import at.fhburgenland.ssoa.soap.GeocodingClient;

import java.util.Hashtable;
import java.util.Scanner;

/**
 * Created by Norbert.Fesel on 18.12.2016.
 */
public class ServiceConsumer {

    public static void main(String[] args) {
        //Initialisierung der benötigten Variablen
        int userChoice;
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to TraTo! - Created by Norbert Fesel & Hermann Hafner-Kragl");

        //Applikationsmenü drucken
        userChoice = MenuHelper.menu();

        while (userChoice != 3) {
            switch (userChoice) {
                case 1:
                    requestInputForGeocodingAPI();
                    break;
                case 2:
                    requestInputForWeatherAPI();
                    break;
                default:
                    System.out.println(userChoice + " is not a valid option. Please try again.");
            }
            userChoice = MenuHelper.menu();
        }

        System.out.println("Goodbye! Please come again.");

    }

    private static void requestInputForWeatherAPI() {
        Scanner input = new Scanner(System.in);

        System.out.println("Please enter a valid API key.");
        String key = input.nextLine();

        System.out.println("Please enter a location.");
        String location = input.nextLine();

        callLocalWeatherAPI(key, location);
    }

    private static void callLocalWeatherAPI(String key, String location) {
        LocalWeatherClient lclient = new LocalWeatherClient();
        try {
            String result = lclient.callLocalWeatherAPI(key, location);
            lclient.printCurrentCondition(result);
        } catch (Exception e) {
            System.out.println("The following error occurred while querying data from the weather service:");
            System.out.println(e.getMessage());
            System.out.println("Please make sure your input is valid.\n");
        }
    }

    private static void requestInputForGeocodingAPI() {
        Scanner input = new Scanner(System.in);

        System.out.println("Please enter a valid API key.");
        String key = input.nextLine();

        System.out.println("Please enter the name or ISO 2 or 3 character code for the country to search in. Most country names will be recognised but the use of the ISO country code is recommended for clarity.");
        String country = input.nextLine();

        System.out.println("Please enter the location to geocode. This can be a postal code or place name.");
        String location = input.nextLine();

        System.out.println("Calling PostCodeAnywhere with the following parameters:");
        System.out.println("API Key: " + key + " | Country: " + country + " | Location: " + location + "\n");

        callInternationalGeocodingAPI(key, country, location);

    }

    private static void callInternationalGeocodingAPI(String key, String country, String location) {
        GeocodingClient gclient = new GeocodingClient();
        try {
            Hashtable[] geocodingResult = gclient.Geocoding_International_Geocode_v1_10(key, country, location);
            System.out.println("PostCodeAnywhere returned the following data:");
            gclient.printGeocodingResult(geocodingResult);
        } catch (Exception e) {
            System.out.println("The following error occurred while querying data from the geocoding service:");
            System.out.println(e.getMessage() + "\n");
        }
    }

}
