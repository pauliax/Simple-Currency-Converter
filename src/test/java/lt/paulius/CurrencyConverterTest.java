package lt.paulius;

import lt.paulius.converter.CurrencyConverter;
import lt.paulius.converter.exceptions.CurrencyNotFoundException;
import lt.paulius.converter.exceptions.ExchangeRatesNotSetException;
import lt.paulius.utils.FileReader;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CurrencyConverterTest {

    @BeforeClass
    public static void init() {
        setExchangeRates();
    }

    @Before
    public void setExchangeRatesIfNotSet() {
        if (CurrencyConverter.getExchangeRates() == null || CurrencyConverter.getExchangeRates().isEmpty()) {
            setExchangeRates();
        }
    }

    private static void setExchangeRates() {
        FileReader fileReader = new FileReader();

        try {
            HashMap<String, String> exchangeRates = fileReader.readXLSX(App.FILE_PATH);
            CurrencyConverter.setExchangeRates(exchangeRates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = ExchangeRatesNotSetException.class)
    public void shouldThrowErrorIfExchangeRatesAreNotSet()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.setExchangeRates(null);
        CurrencyConverter.convert("USD", "ETH", "500");
    }

    @Test(expected = ExchangeRatesNotSetException.class)
    public void shouldThrowErrorIfExchangeRatesAreEmpty()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.setExchangeRates(new HashMap<String, String>());
        CurrencyConverter.convert("USD", "ETH", "500");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorIfFromIsNotSet()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert(null, "ETH", "500");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorIfFromIsEmpty()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert("", "ETH", "500");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorIfToIsNotSet()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert("USD", null, "500");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorIfToIsEmpty()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert("USD", "", "500");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorIfAmountIsNotSet()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert("USD", "ETH", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorIfAmountIsNotANumber()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert("USD", "ETH", "kuku");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorIfAmountIsNegative()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert("USD", "ETH", "-5");
    }

    @Test(expected = CurrencyNotFoundException.class)
    public void shouldThrowErrorIfFromCurrencyIsNotFound()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert("kuku", "ETH", "500");
    }

    @Test(expected = CurrencyNotFoundException.class)
    public void shouldThrowErrorIfToCurrencyIsNotFound()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        CurrencyConverter.convert("USD", "kuku", "500");
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowErrorIfFromRateIsNotValid()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        HashMap<String, String> exchangeRates = CurrencyConverter.getExchangeRates();
        String eurRate = exchangeRates.get("USD");

        try {
            exchangeRates.put("USD", "kuku");
            CurrencyConverter.setExchangeRates(exchangeRates);
            CurrencyConverter.convert("USD", "EUR", "500");
        } finally {
            exchangeRates.put("USD", eurRate);
            CurrencyConverter.setExchangeRates(exchangeRates);
        }
    }

    @Test(expected = NumberFormatException.class)
    public void shouldThrowErrorIfToRateIsNotValid()
            throws ExchangeRatesNotSetException, CurrencyNotFoundException, IllegalArgumentException {
        HashMap<String, String> exchangeRates = CurrencyConverter.getExchangeRates();
        String eurRate = exchangeRates.get("EUR");

        try {
            exchangeRates.put("EUR", "kuku");
            CurrencyConverter.setExchangeRates(exchangeRates);
            CurrencyConverter.convert("USD", "EUR", "500");
        } finally {
            exchangeRates.put("EUR", eurRate);
            CurrencyConverter.setExchangeRates(exchangeRates);
        }
    }

    @Test
    public void shouldConvertFromUsdToEth() {
        try {
            String result = CurrencyConverter.convert("USD", "ETH", "500");
            assertEquals("500 USD must be 0.590660476544872476 ETH", result, "0.590660476544872476");
        } catch (ExchangeRatesNotSetException | CurrencyNotFoundException | IllegalArgumentException e) {
            System.out.println("Error6: " + e.getMessage());
        }
    }

    @Test
    public void shouldConvertFromEurToEur() {
        try {
            String result = CurrencyConverter.convert("EUR", "EUR", "1");
            assertEquals("1 EUR must be 1 EUR", result, "1.000000000000000000");
        } catch (ExchangeRatesNotSetException | CurrencyNotFoundException | IllegalArgumentException e) {
            System.out.println("Error5: " + e.getMessage());
        }
    }

    @Test
    public void shouldConvertFromGbpToEur() {
        try {
            String result = CurrencyConverter.convert("GBP", "EUR", "100");
            assertEquals("100 GBP must be 112.669500000000000000 EUR", result, "112.669500000000000000");
        } catch (ExchangeRatesNotSetException | CurrencyNotFoundException | IllegalArgumentException e) {
            System.out.println("Error4: " + e.getMessage());
        }
    }

    @Test
    public void shouldConvertFromBtcToUsd() {
        try {
            String result = CurrencyConverter.convert("BTC", "USD", "1.23");
            assertEquals("1.23 BTC must be 10600.693500000000000000 USD", result, "10600.693500000000000000");
        } catch (ExchangeRatesNotSetException | CurrencyNotFoundException | IllegalArgumentException e) {
            System.out.println("Error3: " + e.getMessage());
        }
    }

    @Test
    public void shouldConvertFromEthToFke() {
        try {
            String result = CurrencyConverter.convert("ETH", "FKE", "0.123");
            assertEquals("0.123 ETH must be 3371.648815525082400000 FKE", result, "3371.648815525082400000");
        } catch (ExchangeRatesNotSetException | CurrencyNotFoundException | IllegalArgumentException e) {
            System.out.println("Error2: " + e.getMessage());
        }
    }

    @Test
    public void shouldIgnoreCase() {
        try {
            String result = CurrencyConverter.convert("usd", "eth", "500");
            assertEquals("Should ignore currency letter case", result, "0.590660476544872476");
        } catch (ExchangeRatesNotSetException | CurrencyNotFoundException | IllegalArgumentException e) {
            System.out.println("Error1: " + e.getMessage());
        }
    }
}
