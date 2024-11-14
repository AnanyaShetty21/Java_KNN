import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;

public class Utils {

    static double[][] convertCSVtoArray(String csvFilePath, int size) {
        double[][] array2d = null;

        try (FileReader fileReader = new FileReader(csvFilePath);
             CSVReader csvReader = new CSVReader(fileReader)) {

            // Initialize array once we know the length of the header
            String[] header = csvReader.readNext();
            if (header == null) return new double[0][0];
            int length = header.length;
            array2d = new double[size - 1][length];

            // Read each row into the array
            for (int i = 0; i < size - 1; i++) {
                String[] nextRecord = csvReader.readNext();
                if (nextRecord == null) break; // Stop if fewer rows are available
                for (int j = 0; j < length; j++) {
                    array2d[i][j] = Double.parseDouble(nextRecord[j]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return array2d;
    }


    public static void main(String[] args) {
        String csvFilePath = "data/train.csv";
        int size = 500000;
        double[][] array = convertCSVtoArray(csvFilePath, size);
        printArray(array);
    }
    private static void printArray(double[][] array) {
        for (double[] row : array) {
            for (double value : row) {
                System.out.print(value + " ");
            }
            System.out.println(); // Move to the next line after each row
        }
    }
}
