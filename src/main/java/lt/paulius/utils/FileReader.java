package lt.paulius.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class FileReader {

    // Reads XLSX data line by line from file. First cell - currency symbol, second cell - rate to EUR.
    public HashMap<String, String> readXLSX(String filePath) throws IOException {
        HashMap<String, String> fileContents = new HashMap<>();

        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet firstSheet = workbook.getSheetAt(0);

        if (firstSheet != null) {
            for (Row row : firstSheet) {
                Cell firstCell = row.getCell(0);
                Cell secondCell = row.getCell(1);

                if (firstCell != null && secondCell != null) {
                    String symbol = firstCell.toString();
                    String eurRate = secondCell.toString();

                    if (symbol != null && eurRate != null) {
                        fileContents.put(symbol.toUpperCase(), eurRate);
                    }
                }
            }

            workbook.close();
            fileInputStream.close();
        }

        return fileContents;
    }
}
