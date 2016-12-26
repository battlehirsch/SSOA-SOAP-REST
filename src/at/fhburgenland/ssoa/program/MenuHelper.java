package at.fhburgenland.ssoa.program;

import java.util.Scanner;

/**
 * Created by Norbert.Fesel on 18.12.2016.
 * Helperklasse für die Menüführung des Programms
 */
public class MenuHelper {
    public static int menu() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println("Please enter a number to perfom the desired operation");
        System.out.println("-------------------------\n");
        System.out.println("1 - Call the international geocoding API provided by PostCodeAnywhere (SOAP)");
        System.out.println("2 - Call the global weather API provided by WorldWeatherOnline (REST)");
        System.out.println("3 - Quit");

        selection = input.nextInt();
        return selection;
    }
}
