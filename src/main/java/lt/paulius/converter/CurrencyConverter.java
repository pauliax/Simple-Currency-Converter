package lt.paulius.converter;

import lt.paulius.converter.exceptions.CurrencyNotFoundException;
import lt.paulius.converter.exceptions.ExchangeRatesNotSetException;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.HashMap;

public class CurrencyConverter {

    public static final int DECIMALS_PRECISION = 18;

    // Exchange rates: currency symbol -> rate in EUR.
    private static HashMap<String, String> exchangeRates = new HashMap<>();

    public static void setExchangeRates(HashMap<String, String> exchangeRates) {
        CurrencyConverter.exchangeRates = exchangeRates;
    }

    public static HashMap<String, String> getExchangeRates() {
        return CurrencyConverter.exchangeRates;
    }

    // Converts a specified amount from one currency to another and returns the result.
    public static String convert(String from, String to, String amount)
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        if (exchangeRates == null || exchangeRates.isEmpty()) {
            throw new ExchangeRatesNotSetException("Exchange rates are not set!");
        }

        if (from == null || from.isEmpty()) {
            throw new IllegalArgumentException("Invalid input (from): " + from);
        }

        if (to == null || to.isEmpty()) {
            throw new IllegalArgumentException("Invalid input (to): " + to);
        }

        // Check if 'amount' is a number.
        if (amount == null || !NumberUtils.isCreatable(amount)) {
            throw new NumberFormatException("Invalid input (amount): " + amount);
        }

        String rateFrom = exchangeRates.get(from.toUpperCase());

        // Check if 'from' currency is present in the hashmap.
        if (rateFrom == null) {
            throw new CurrencyNotFoundException("Currency not found: " + from);
        }

        String rateTo = exchangeRates.get(to.toUpperCase());

        // Check if 'to' currency is present in the hashmap.
        if (rateTo == null) {
            throw new CurrencyNotFoundException("Currency not found: " + to);
        }

        // Check if 'from' rate is a number.
        if (!NumberUtils.isCreatable(rateFrom)) {
            throw new NumberFormatException("Invalid rate (" + from.toUpperCase() + "): " + rateFrom);
        }

        // Check if 'to' rate is a number.
        if (!NumberUtils.isCreatable(rateTo)) {
            throw new NumberFormatException("Invalid rate (" + to.toUpperCase() + "): " + rateTo);
        }

        BigDecimal amountDecimal = new BigDecimal(amount);

        // Check if 'amount' is not a negative number.
        if (amountDecimal.compareTo(BigDecimal.ZERO) < 0 ) {
            throw new IllegalArgumentException("Invalid input (amount): " + amount);
        }

        BigDecimal rateFromDecimal = new BigDecimal(rateFrom);
        BigDecimal rateToDecimal = new BigDecimal(rateTo);

        // Calculate the result.
        BigDecimal convertedAmount = amountDecimal
                .setScale(DECIMALS_PRECISION, BigDecimal.ROUND_HALF_UP)
                .multiply(rateFromDecimal)
                .divide(rateToDecimal, BigDecimal.ROUND_HALF_UP)
                .setScale(DECIMALS_PRECISION, BigDecimal.ROUND_HALF_UP);

        return convertedAmount.toString();
    }
}
