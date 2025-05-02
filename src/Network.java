import java.util.function.Function;

public class Network {
    private Layer[] layers; // Array of layers in the network
    private double learningRate;
    private String activationOption; // SIGM / RELU / TANH
    private String weightInitOption; // RAND / XAVIER / HE
    private int lossTrackingEpochs;

    private Function<Double, Double> activationFunction;
    private Function<Double, Double> derivativeFunction;

    public Network(int[] layerSizes) {
        this.layers = new Layer[layerSizes.length - 1];

        // Set default parameters
        this.learningRate = 0.1;
        this.activationOption = "SIGM";
        this.weightInitOption = "RAND";

        this.activationFunction = Activation::sigmoid;
        this.derivativeFunction = Activation::derivativeSigmoid;

        this.lossTrackingEpochs = 1000; // Display loss every n epochs

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

            if (epoch % this.lossTrackingEpochs == 0) {
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
            output = l.forward(output, this.activationFunction);
        }

        return output;
    }

    public void backward(double[] expectedOutput) {
        // Step 1: Compute the error at the output layer
        double[] actualOutput = this.layers[this.layers.length - 1].getLastOutput();
        double[] error = new double[expectedOutput.length];
    
        for (int i = 0; i < expectedOutput.length; i++) {
            // Chain rule
            error[i] = (actualOutput[i] - expectedOutput[i]) * this.derivativeFunction.apply(actualOutput[i]);
        }
    
        // Step 2: Backpropagate through the layers
        for (int i = this.layers.length - 1; i >= 0; i--) {
            error = this.layers[i].backward(error, this.learningRate);
        }
    }

    public double getLearningRate() {
        return this.learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }
    
    public String getActivationOption() {
        return this.activationOption;
    }
    
    public void setActivationOption(String activationOption) {
        this.activationOption = activationOption;
        this.activationFunction = switch (activationOption) {
            case "SIGM" -> Activation::sigmoid;
            case "RELU" -> Activation::relu;
            case "TANH" -> Activation::tanh;
            default -> throw new IllegalArgumentException("Invalid activation option. Use 'SIGM', 'RELU', or 'TANH'.");
        };
    }
    
    public String getWeightInitOption() {
        return this.weightInitOption;
    }

    public void setWeightInitOption(String weightInitOption) {
        if (!weightInitOption.equals("RAND") && !weightInitOption.equals("XAVIER") && !weightInitOption.equals("HE")) {
            throw new IllegalArgumentException("Invalid weight initialization option. Use 'RAND', 'XAVIER', or 'HE'.");
        }

        this.weightInitOption = weightInitOption;
    }

    public int getLossTrackingEpochs() {
        return lossTrackingEpochs;
    }

    public void setLossTrackingEpochs(int lossTrackingEpochs) {
        this.lossTrackingEpochs = lossTrackingEpochs;
    }

    public Layer[] getLayers() {
        return layers;
    }

}
