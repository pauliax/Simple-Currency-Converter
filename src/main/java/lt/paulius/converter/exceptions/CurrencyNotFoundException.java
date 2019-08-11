package lt.paulius.converter.exceptions;

public class CurrencyNotFoundException extends Exception {

    public CurrencyNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
