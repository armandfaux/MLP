class Layer {
    private final int size;
    private final int previousLayerSize;

    private final double[] biases;
    private final double[][] weights;

    public Layer(int size, int previousLayerSize) {
        this.size = size;
        this.previousLayerSize = previousLayerSize;

        biases = new double[size];
        weights = new double[size][previousLayerSize];

        this.initialize();
    }

    public void initialize() { 
        // Set biases to random values from 0 to 1
        for (int i = 0; i < this.size; i++) {
            biases[i] = Math.random();
        }

        // Set all weights to random values from 0 to 1
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.previousLayerSize; j++) {
                weights[i][j] = Math.random();
            }
        }
    }

    public double[] forward(double[] input) {
        display();

        // CHECK IF INPUT.LENGTH < PREVIOUS LAYER SIZE (*)

        double[] output = new double[this.size];

        for (int neuron = 0; neuron < weights.length; neuron++) {
            double sum_weighted_input = 0;

            // Sum of every input * corresponding weight
            for (int k = 0; k < this.previousLayerSize; k++) {
                sum_weighted_input += input[k] * weights[neuron][k];
            }

            // Add bias and activation function
            output[neuron] = Activation.sigmoid(sum_weighted_input + biases[neuron]);
        }

        return output;
    }

    public void display() {
        System.out.println("=== Layer Details ===");
    
        System.out.printf("%-10s %-15s %s\n", "Neuron", "Bias", "Weights");
        System.out.println("------------------------------------------------------");
    
        for (int i = 0; i < biases.length; i++) {
            // Print neuron index and bias
            System.out.printf("%-10d %-15.6f ", i, biases[i]);
    
            // Print all weights for this neuron
            for (int j = 0; j < weights[i].length; j++) {
                System.out.printf("%.6f ", weights[i][j]);
            }
            System.out.println();
        }
    
        System.out.println("======================================================\n");
    }
}