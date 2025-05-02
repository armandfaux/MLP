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
            {1},
            {1},
            {0},
        };

        // Network architecture: 2 inputs → 2 hidden → 1 output
        Network network = new Network(new int[]{2, 2, 1});
        network.setLearningRate(0.9);
        network.setLossTrackingEpochs(10_000);
        network.setActivationOption("TANH");
        network.train(inputs, expectedOutputs, 100_000);

        double prediction = network.predict(inputs[1])[0];
        System.out.printf("AND gate prediction for (0, 1) : %f\n", prediction);

        prediction = network.predict(inputs[3])[0];
        System.out.printf("AND gate prediction for (1, 1) : %f\n\n", prediction);

        for (Layer l : network.getLayers()) {
            l.display();
        }

        //////////////

        Renderer renderer = new Renderer("Neural Network", 800, 600, network);

        while (true) {
            renderer.refresh();
            try {
                Thread.sleep(1000 / 60); // 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
