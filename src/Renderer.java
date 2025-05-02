import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

public class Renderer extends JPanel {
    private JFrame frame;
    private Network network;

    public Renderer(String title, int width, int height, Network network) {
        this.network = network;

        // Create the main window
        frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawConnections((Graphics2D) g);
        drawNetwork((Graphics2D) g);
    }

    private int[][][] getNeuronsPositions() {
        Layer[] layers = this.network.getLayers();
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int layerCount = layers.length + 1; // input + hidden/output
        int horizontalSpacing = panelWidth / (layerCount + 1);
        int[][][] neuronsPositions = new int[layerCount][][];

        for (int i = 0; i < layerCount; i++) {
            int neuronCount = (i == 0) ? layers[0].getPreviousLayerSize() : layers[i - 1].getSize();
            int verticalSpacing = panelHeight / (neuronCount + 1);
            neuronsPositions[i] = new int[neuronCount][2];
    
            for (int j = 0; j < neuronCount; j++) {
                int x = (i + 1) * horizontalSpacing;
                int y = (j + 1) * verticalSpacing;
                neuronsPositions[i][j][0] = x;
                neuronsPositions[i][j][1] = y;
            }
        }

        return neuronsPositions;
    }

    private void drawNetwork(Graphics2D g) {
        Layer[] layers = this.network.getLayers();
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int layerCount = layers.length + 1; // input + hidden/output
        int neuronRadius = 20;
        int horizontalSpacing = panelWidth / (layerCount + 1);

        for (int i = 0; i < layerCount; i++) {
            int neuronCount = (i == 0) ? layers[0].getPreviousLayerSize() : layers[i - 1].getSize();
            int verticalSpacing = panelHeight / (neuronCount + 1);

            for (int j = 0; j < neuronCount; j++) {
                int x = (i + 1) * horizontalSpacing;
                int y = (j + 1) * verticalSpacing;

                // Determine color based on bias
                double bias = 0.0;
                if (i > 0) {
                    bias = layers[i - 1].getBiasOfNeuron(j);
                }

                double clamped = Math.max(-1.0, Math.min(1.0, bias));
                int intensity = (int)(Math.abs(clamped) * 255);

                Color color;
                if (clamped < 0) {
                    color = new Color(0, 0, intensity);      // Blueish
                } else {
                    color = new Color(intensity, 0, 0);      // Reddish
                }

                g.setColor(color);

                g.fillOval(x - neuronRadius / 2, y - neuronRadius / 2, neuronRadius, neuronRadius);
                g.setColor(Color.BLACK);
                g.drawOval(x - neuronRadius / 2, y - neuronRadius / 2, neuronRadius, neuronRadius);
            }
        }
    }

    public void drawConnections(Graphics2D g2d) {
        Layer[] layers = this.network.getLayers();
        int[][][] neuronsPositions = this.getNeuronsPositions();
        int thicknessMax = 12;

        for (int layer = 0; layer < layers.length; layer++) {
            double[][] weights = layers[layer].getWeights();

            for (int neuron = 0; neuron < neuronsPositions[layer].length; neuron++) {
                for (int nextNeuron = 0; nextNeuron < neuronsPositions[layer + 1].length; nextNeuron++) {
                    int x1 = neuronsPositions[layer][neuron][0];
                    int y1 = neuronsPositions[layer][neuron][1];
                    int x2 = neuronsPositions[layer + 1][nextNeuron][0];
                    int y2 = neuronsPositions[layer + 1][nextNeuron][1];

                    double normalizedWeight = Activation.sigmoid(weights[nextNeuron][neuron]);
                    int thickness = (int) (normalizedWeight * thicknessMax);
                    g2d.setStroke(new BasicStroke(thickness));
                    g2d.setColor(new Color((int) (normalizedWeight * 255), 0, (int) ((1 - normalizedWeight) * 255)));
                    g2d.drawLine(x1, y1, x2, y2);
                }
            }
        }

        // // Coordinates of neurons in previous layer
        // ArrayList<Point> prevNeurons = null;

        // for (int i = 0; i < layers.length; i++) {
        //     double[][] weights = layers[i].getWeights();
        //     // if (i == 0) {
        //     //     for (double[] neuronWeights : weights) {
        //     //         System.out.println("Input neuron weights:");
        //     //         for (double w : neuronWeights) {
        //     //             System.out.printf("%.4f ", w);
        //     //         }
        //     //         System.out.println();
        //     //     }
        //     // }
        //     int currLayerSize = weights.length;
        //     int prevLayerSize = weights[0].length;

        //     int currX = (i + 1) * layerSpacing;

        //     // Compute Y positions of current layer neurons
        //     int totalHeight = currLayerSize * (neuronRadius + 10);
        //     int startY = (panelHeight - totalHeight) / 2;
        //     ArrayList<Point> currNeurons = new ArrayList<>();

        //     for (int j = 0; j < currLayerSize; j++) {
        //         int y = startY + j * (neuronRadius + 10) + neuronRadius / 2;
        //         currNeurons.add(new Point(currX, y));
        //     }

        //     if (prevNeurons != null) {
        //         // Draw lines from prevNeurons to currNeurons
        //         for (int to = 0; to < currLayerSize; to++) {
        //             for (int from = 0; from < prevLayerSize; from++) {
        //                 double weight = weights[to][from];

        //                 // Normalize weight for stroke size (e.g. between 1 and 5 px)
        //                 float thickness = (float)(Math.min(5.0, Math.max(0.5, Math.abs(weight) * 3)));

        //                 // Set color: positive = green, negative = orange
        //                 if (weight >= 0) g2d.setColor(new Color(0, 150, 0, 150));  // Semi-transparent green
        //                 else g2d.setColor(new Color(200, 100, 0, 150));            // Semi-transparent orange

        //                 g2d.setStroke(new BasicStroke(thickness));
        //                 Point p1 = prevNeurons.get(from);
        //                 Point p2 = currNeurons.get(to);
        //                 g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        //             }
        //         }
        //     }
        //     prevNeurons = currNeurons;
        // }

        // // Reset stroke
        // g2d.setStroke(new BasicStroke(1));
    }

    private int getMaxNeurons(Layer[] layers) {
        int max = 0;
        for (Layer l : layers) {
            max = Math.max(max, l.getSize());
            max = Math.max(max, l.getPreviousLayerSize());
        }
        return max;
    }

    // Call this to manually refresh the display
    public void refresh() {
        repaint();
    }
}
