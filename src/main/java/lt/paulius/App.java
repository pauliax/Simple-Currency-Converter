package lt.paulius;

import lt.paulius.converter.CurrencyConverter;
import lt.paulius.converter.exceptions.CurrencyNotFoundException;
import lt.paulius.converter.exceptions.ExchangeRatesNotSetException;
import lt.paulius.utils.FileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * App entry point.
 *
 */
public class App {

    public static final String FILE_PATH = "data\\rates.xlsx";

    public static void main( String[] args ) {
        initRates();
        printMenu();
    }

    // Reads from xlsx file and sets the exchange rates.
    private static void initRates() {
        FileReader fileReader = new FileReader();

        try {
            HashMap<String, String> exchangeRates = fileReader.readXLSX(FILE_PATH);
            CurrencyConverter.setExchangeRates(exchangeRates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Interactive console UI.
    private static void printMenu() {
        System.out.println("Welcome to the simple currency converter");

        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        do {
            System.out.println("\n*************************************");
            System.out.println("Choose an action:");
            System.out.println("0. Exit");
            System.out.println("1. Help");
            System.out.println("2. Convert");
            System.out.println("*************************************\n");

            try {
                String option = bufferedReader.readLine();

                switch (option) {
                    case "0":
                        System.out.println("Bye...");
                        System.exit(0);
                    case "1":
                        System.out.println("Simple currency converter that uses exchange rates from XLSX file " +
                                "(data/rates.xlsx) and converts a specified amount of one currency to another currency.\n");
                        printExchangeRates();
                        break;
                    case "2":
                        String amount = null;

                        while (amount == null) {
                            System.out.println("Enter amount:");
                            amount = bufferedReader.readLine();
                        }

                        String from = null;

                        while (from == null) {
                            System.out.println("Enter the currency you want to convert from:");
                            from = bufferedReader.readLine();
                        }

                        String to = null;

                        while (to == null) {
                            System.out.println("Enter the currency you want to convert to:");
                            to = bufferedReader.readLine();
                        }

                        convert(from, to, amount);
                        break;
                    default:
                        System.out.println("Wrong input");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while(true);
    }

    private static void convert(String from, String to, String amount) {
        try {
            String convertedAmount = CurrencyConverter.convert(from, to, amount);

            if (convertedAmount != null) {
                System.out.println("Successfully converted:");
                System.out.println(amount + " " + from + " = " + convertedAmount + " " + to);
            } else {
                System.out.println("Conversion failed.");
            }
        } catch (ExchangeRatesNotSetException | CurrencyNotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void printExchangeRates() {
        System.out.println("Exchange rates (in EUR):");

        HashMap<String, String> exchangeRates = CurrencyConverter.getExchangeRates();

        if (exchangeRates != null && !exchangeRates.isEmpty()) {
            for (String currency : exchangeRates.keySet()) {
                System.out.println(currency + ": " + exchangeRates.get(currency));
            }
        } else {
            System.out.println("Exchange rates are not available.");
        }
    }
}
