
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadCsv {

    public void run() {

        String csvFile = "paddy.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(cvsSplitBy);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done loading");
    }

    private double normalize_area(double area) {
        return (area - 2750) / (38500 - 2750);
    }

    private double normalize_fertilizer(double f) {
        return (f - 2.9) / (151.25 - 2.9);
    }

    private double normalize_rain(double f) {
        return (f - 25.40231) / (289.05 - 25.40231);
    }

    private double normalize_temp(double f) {
        return (f - 8.95) / (229.4475 - 8.95);
    }

    private double final_value(double f) {
        return ((f * (6420 - 1173.33)) + 1173.33);
    }

}
