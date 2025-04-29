public class Activation {
    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    public static double derivativeSigmoid(double y) {
        return y * (1 - y);
    }
}
