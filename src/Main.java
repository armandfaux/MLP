public class Main {
    // This exemple handles logic gates recognition
    public static void main(String[] args) {
        double[][] inputs = {
            {0, 0},
            {0, 1},
            {1, 0},
            {1, 1}
        };

        // AND gate behavior
        double[][] expectedOutputs = {
            {0},
            {0},
            {0},
            {1},
        };

        // Network architecture: 2 inputs → 2 hidden → 1 output
        Network network = new Network(new int[]{2, 2, 1});
        network.setLearningRate(0.3);
        network.train(inputs, expectedOutputs, 10_000);

        double prediction = network.predict(inputs[1])[0];
        System.out.printf("AND gate prediction for (0, 1) : %f\n", prediction);

        prediction = network.predict(inputs[3])[0];
        System.out.printf("AND gate prediction for (1, 1) : %f\n", prediction);
    }
}
