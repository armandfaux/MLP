import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvReader {
    public static Dataset readCSV(String filename) {
        ArrayList<double[]> inputsList = new ArrayList<>();
        ArrayList<double[]> outputsList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // skip header
            if (line == null) {
                throw new IOException("Empty file");
            }

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int label = Integer.parseInt(parts[0]);

                // Normalize pixel values (0-255 → 0.0-1.0)
                double[] input = new double[parts.length - 1];
                for (int i = 1; i < parts.length; i++) {
                    input[i - 1] = Double.parseDouble(parts[i]) / 255.0;
                }

                // One-hot encode label
                double[] output = new double[10];
                output[label] = 1.0;

                inputsList.add(input);
                outputsList.add(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double[][] inputs = inputsList.toArray(new double[0][]);
        double[][] outputs = outputsList.toArray(new double[0][]);

        return new Dataset(inputs, outputs);
    }
}
