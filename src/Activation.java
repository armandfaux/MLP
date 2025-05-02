public class Activation {
    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static double derivativeSigmoid(double y) {
        return y * (1 - y);
    }

    public static double tanh(double x) {
        return Math.tanh(x);
    }

    public static double derivativeTanh(double y) {
        return 1.0 - (y * y);
    }

    public static double relu(double x) {
        return Math.max(0.0, x);
    }

    public static double derivativeRelu(double x) {
        return x > 0.0 ? 1.0 : 0.0;
    }
}
