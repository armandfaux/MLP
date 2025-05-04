public class Main {
    public static void main(String[] args) {
        // Load training dataset
        System.out.println("Loading training data...");
        Dataset trainingData = CsvReader.readCSV("mnist_train.csv");

        Network mlp = new Network(new int[]{784, 64, 32, 16, 10});

        // Train network
        mlp.setLearningRate(0.02);
        mlp.setLossTrackingEpochs(1);
        mlp.train(trainingData.inputs, trainingData.expectedOutputs, 10);

        // Load test dataset
        System.out.println("Loading test data...");
        Dataset testData = CsvReader.readCSV("mnist_test.csv");

        // Evaluate on test dataset
        int correct = 0;
        int total = testData.inputs.length;

        for (int i = 0; i < total; i++) {
            double[] prediction = mlp.predict(testData.inputs[i]);

            int predictedDigit = Utils.argMax(prediction);
            int actualDigit = Utils.argMax(testData.expectedOutputs[i]);

            if (predictedDigit == actualDigit) {
                correct++;
            }
        }

        double accuracy = 100.0 * correct / total;
        System.out.printf("Test accuracy: %.2f%% (%d/%d correct)%n", accuracy, correct, total);
    }
}
