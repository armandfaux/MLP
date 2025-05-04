public class Utils {
        public static int argMax(double[] output) {
            int maxIndex = 0;
            for (int i = 1; i < output.length; i++) {
                if (output[i] > output[maxIndex]) {
                    maxIndex = i;
                }
            }
            return maxIndex;
        }
}
