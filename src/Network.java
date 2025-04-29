public class Network {
    private Layer[] layers; // Array of layers in the network
    private double learningRate;
    private String activationOption; // SIGM / RELU / TANH
    private String weightInitOption; // RAND / XAVIER / HE

    public Network(int[] layerSizes) {
        this.layers = new Layer[layerSizes.length - 1];

        // Set default parameters
        this.learningRate = 0.1;
        this.activationOption = "SIGM";
        this.weightInitOption = "RAND";

        for (int i = 0; i < layerSizes.length - 1; i++) {
            this.layers[i] = new Layer(layerSizes[i + 1], layerSizes[i]);
            this.layers[i].initialize(this.weightInitOption);
        }
    }

    public void train(double[][] inputs, double[][] expectedOutputs, int epochs) {
        System.out.println("Training network...\n");

        for (int epoch = 1; epoch <= epochs; epoch++) {
            double totalLoss = 0.0;

            for (int i = 0; i < inputs.length; i++) {
                double[] prediction = this.forward(inputs[i]);
                this.backward(expectedOutputs[i]);

                // Mean Squared Error
                double error = prediction[0] - expectedOutputs[i][0];
                totalLoss += error * error;
            }

            if (epoch % 1000 == 0) {
                System.out.printf("Epoch %d - Loss: %.6f\n", epoch, totalLoss / inputs.length);
            }
        }
        System.out.println("***\nTraining complete\n");
    }

    public double[] predict(double[] input) {
        return this.forward(input);
    }

    public double[] forward(double[] input) {
        double[] output = input;

        for (Layer l : this.layers) {
            output = l.forward(output);
        }

        return output;
    }

    public void backward(double[] expectedOutput) {
        // Step 1: Compute the error at the output layer

        double[] actualOutput = this.layers[this.layers.length - 1].getLastOutput(); // OPTION 1 : get output from output layer
        double[] error = new double[expectedOutput.length];
    
        for (int i = 0; i < expectedOutput.length; i++) {
            // Chain rule
            error[i] = (actualOutput[i] - expectedOutput[i]) * Activation.derivativeSigmoid(actualOutput[i]);
        }
    
        // Step 2: Backpropagate through the layers
        for (int i = this.layers.length - 1; i >= 0; i--) {
            error = this.layers[i].backward(error, this.learningRate); 
            // This method will:
            // - compute gradients
            // - update weights and biases
            // - return the propagated error to the previous layer
        }
    }

    public double getLearningRate() {
        return this.learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setActivationOption(String activationOption) {
        this.activationOption = activationOption;
    }

    public String getActivationOption() {
        return this.activationOption;
    }

    public void setWeightInitOption(String weightInitOption) {
        if (!weightInitOption.equals("RAND") && !weightInitOption.equals("XAVIER") && !weightInitOption.equals("HE")) {
            throw new IllegalArgumentException("Invalid weight initialization option. Use 'RAND', 'XAVIER', or 'HE'.");
        }

        this.weightInitOption = weightInitOption;
    }

    public String getWeightInitOption() {
        return this.weightInitOption;
    }
}
