public class Main {
    public static void main(String[] args) {
        // Create a Layer
        int[] layerSizes = {3, 5, 10, 4}; // Number of neurons in this layer
        double[] input = {2.5, 6.5, 1.1};

        Network n = new Network(layerSizes);

        for (double output : n.forward(input)) {
            System.out.printf("%f", output);
        }
    }
}
