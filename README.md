# MLP
Neural Network API for Java (Multilayer Perceptron architecture)

## Features
Activation options :
- Sigmoid
- ReLU (soon)
- TAHN

Weight initialization :
- Random
- Xavier (soon)
- HE (soon)

## Sample test

    int[] layers = new int[]{2, 2, 1} // 2 input neurons, 2 hidden, 1 output
    Network network = new Network(layers);

    // Default learning rate = 0.1
    network.setLearningRate(0.3);

    // Train the network over X epochs using forward and backward propagation
    network.train(inputs, expectedOutputs, 10_000);

    // Display result
    double prediction = network.predict(inputs[1])[0];
    System.out.printf("AND gate prediction for (0, 1) : %f\n", prediction);


~ See Main.java ~
