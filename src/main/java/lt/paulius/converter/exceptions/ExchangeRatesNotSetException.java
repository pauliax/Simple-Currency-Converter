package lt.paulius.converter.exceptions;

public class ExchangeRatesNotSetException extends Exception {

    public ExchangeRatesNotSetException(String errorMessage) {
        super(errorMessage);
    }
}
