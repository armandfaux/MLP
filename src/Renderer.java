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
        drawNeurons((Graphics2D) g);
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

    private void drawNeurons(Graphics2D g) {
        Layer[] layers = this.network.getLayers();
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int layerCount = layers.length + 1; // input + hidden/output
        int neuronRadius = 30;
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

                double normalizedBias = Activation.tanh(bias);

                Color neuronColor;
                if (normalizedBias > 0) {
                    neuronColor = new Color((int) (normalizedBias * 255), 0, 0);
                } else {
                    neuronColor = new Color(0, 0, (int) (normalizedBias * -255));
                }

                int outlineRadius = neuronRadius + 4;
                g.setColor(Color.BLACK);
                g.fillOval(x - outlineRadius / 2, y - outlineRadius / 2, outlineRadius, outlineRadius);
                g.setColor(neuronColor);
                g.fillOval(x - neuronRadius / 2, y - neuronRadius / 2, neuronRadius, neuronRadius);
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
    }

    // Call this to manually refresh the display
    public void refresh() {
        repaint();
    }
}
