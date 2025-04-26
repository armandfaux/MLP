public class Network {
    private final Layer[] layers; // Array of layers in the network

    public Network(int[] layerSizes) {
        this.layers = new Layer[layerSizes.length - 1];

        for (int i = 0; i < layerSizes.length - 1; i++) {
            layers[i] = new Layer(layerSizes[i + 1], layerSizes[i]);
        }
    }

    public double[] forward(double[] input) {
        double[] output = input;

        for (Layer l : layers) {
            output = l.forward(output);
        }

        return output;
    }
}
