package lt.paulius;

import lt.paulius.utils.FileReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FileReaderTest {

    @Test
    public void shouldReadExchangeRatesFromFile() {
        Map<String, String> expected = new HashMap<>();
        expected.put("EUR", "1.0");
        expected.put("USD", "0.809552722");
        expected.put("GBP", "1.126695");
        expected.put("BTC", "6977.0896569209");
        expected.put("ETH", "685.29447470022");
        expected.put("FKE", "0.025");

        FileReader fileReader = new FileReader();

        try {
            HashMap<String, String> exchangeRates = fileReader.readXLSX(App.FILE_PATH);
            assertThat(exchangeRates, is(expected));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowErrorIfFileIsNotPresent() throws IOException {
        FileReader fileReader = new FileReader();
        fileReader.readXLSX("kuku");
    }
}
