package at.fhburgenland.ssoa.program;

import at.fhburgenland.ssoa.rest.LocalWeatherClient;
import at.fhburgenland.ssoa.soap.GeocodingClient;

import java.util.Hashtable;
import java.util.Scanner;

/**
 * Created by Norbert.Fesel on 18.12.2016.
 * Der ServiceConsumer stellt die eigentliche Konsolenanwendung dar und bedient sich aller anderen Programmpackete, um die definierten Services anzuprogrammieren.
 */
public class ServiceConsumer {
    /**
     * Begrüßt den Benutzer der Konsolenanwendung und verarbeitet die gewählte Menuoption. Je nach gewählter Option, wird entweder ein Geocoding-Service, oder ein Wetter-Service
     * angesprochen. Der Input der für die Abfrage benötigten Parameter erfolgt in einer separaten Methode.
     * @param args
     * Standard main param
     */
    public static void main(String[] args) {
        int userChoice;
        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to TraTo! - Created by Norbert Fesel & Hermann Hafner-Kragl");

        //Applikationsmenü anzeigen
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

    /**
     * Fordert den Benutzer der Konsolenanwendung auf einen gültigen API Key sowie einen gewünschten Ort anzugeben, um den Request durchzuführen.
     */
    private static void requestInputForWeatherAPI() {
        Scanner input = new Scanner(System.in);

        System.out.println("Please enter a valid API key.");
        String key = input.nextLine();

        System.out.println("Please enter a location.");
        String location = input.nextLine();

        callLocalWeatherAPI(key, location);
    }

    /**
     * Versucht den Wetter-Service von "WorldWeatherOnline" mit den zuvor eingegebenen Paramtern abzufragen.
     * Bei erfolgreichem Response, werden die erhaltenen Informationen in der Konsole angezeigt.
     * @param key
     * gültiger API Key von WorldWeatherOnline
     * @param location
     * gewünschter Ort der Wetterabfrage als Name
     */
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

    /**
     * Fordert den Benutzer der Konsolenanwendung auf einen gültigen API Key, ein Land, sowie einen Ort anzugeben, um den Request durchzuführen.
     */
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

    /**
     * Versucht den Geocoding-Service von "PostCodeAnywhere" mit den zuvor eingegebenen Parametern abzufragen.
     * Bei erfolgreichem Response, werden die erhaltenen Informationen in der Konsole angezeigt.
     * @param key
     * gültiger API Key von PostCodeAnywhere
     * @param country
     * gewünschtes Land als Name in englischer Sprache bzw als ISO-Code
     * @param location
     * gewünschter Ort im gewählten Land als Name in englischer Sprache oder Postleitzahl
     */
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
