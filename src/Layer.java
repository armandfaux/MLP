class Layer {
    private int size;
    private int previousLayerSize;

    private double[] biases;
    private double[][] weights;

    private double[] lastOutput;
    private double[] lastInput;

    public Layer(int size, int previousLayerSize) {
        this.size = size;
        this.previousLayerSize = previousLayerSize;

        this.biases = new double[size];
        this.weights = new double[size][previousLayerSize];

        this.lastOutput = new double[size];
        this.lastInput = new double[previousLayerSize];
    }

    public void initialize(String weightOption) {
        for (int i = 0; i < this.size; i++) {
            biases[i] = Math.random();
        }

        // Set all weights to random values from 0 to 1
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.previousLayerSize; j++) {
                // this.weights[i][j] = Math.random();
                this.weights[i][j] = (Math.random() - 0.5) * 2;
            }
        }
    }

    public double[] forward(double[] input) {
        // display();

        if (input.length != this.previousLayerSize) {
            throw new IllegalArgumentException("Input size does not match the previous layer size.");
        }

        // (can also use the existing member lastOutput instead)
        double[] output = new double[this.size];

        for (int neuron = 0; neuron < this.weights.length; neuron++) {
            double sum_weighted_input = 0;

            // Sum of every input * corresponding weight
            for (int k = 0; k < this.previousLayerSize; k++) {
                sum_weighted_input += input[k] * this.weights[neuron][k];
            }

            // Add bias and activation function
            output[neuron] = Activation.sigmoid(sum_weighted_input + this.biases[neuron]);
        }

        this.lastInput = input;
        this.lastOutput = output;
        return output;
    }

    public double[] backward(double[] delta, double learningRate) {
        double[] newDelta = new double[this.previousLayerSize];

        // For each neuron in this layer
        for (int neuron = 0; neuron < this.size; neuron++) {
            // Compute delta (error)
            double derivative = Activation.derivativeSigmoid(this.lastOutput[neuron]);
            double delta_i = delta[neuron] * derivative;

            for (int i = 0; i < this.previousLayerSize; i++) {
                // Update weight using gradient descent
                double gradient = delta_i * this.lastInput[i];
                weights[neuron][i] -= learningRate * gradient;

                // Accumulate delta to propagate to previous layer
                newDelta[i] += delta_i * weights[neuron][i];
            }

            // Update bias
            biases[neuron] -= learningRate * delta_i;
        }

        return newDelta;
    }

    public double[] getLastOutput() {
        return lastOutput;
    }

    public int getSize() {
        return size;
    }

    public int getPreviousLayerSize() {
        return previousLayerSize;
    }

    public double[][] getWeights() {
        return weights;
    }

    public double getBiasOfNeuron(int index) {
        return this.biases[index];
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