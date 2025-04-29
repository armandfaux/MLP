public class Main {
    // This exemple handles logic gates recognition
    public static void main(String[] args) {
        double[][] inputs = {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };

        double[][] expectedOutputs = {
            {0},
            {0},
            {0},
            {1}
        };

        // Network architecture: 2 inputs → 2 hidden → 1 output
        Network network = new Network(new int[]{2, 2, 1});
        network.setLearningRate(0.3);

        int epochs = 10_000;

        for (int epoch = 1; epoch <= epochs; epoch++) {
            double totalLoss = 0.0;

            for (int i = 0; i < inputs.length; i++) {
                double[] prediction = network.forward(inputs[i]);
                network.backward(expectedOutputs[i]);

                // Mean Squared Error
                double error = prediction[0] - expectedOutputs[i][0];
                totalLoss += error * error;
            }

            if (epoch % 1000 == 0) {
                System.out.printf("Epoch %d - Loss: %.6f\n", epoch, totalLoss / inputs.length);
            }
        }

        // Final Predictions
        System.out.println("\nFinal Predictions:");
        for (int i = 0; i < inputs.length; i++) {
            double[] prediction = network.forward(inputs[i]);
            System.out.printf("Input: [%d, %d] -> Output: %.4f\n", 
                (int)inputs[i][0], (int)inputs[i][1], prediction[0]);
        }
    }
}
