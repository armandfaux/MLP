public class Dataset {
    public double[][] inputs;
    public double[][] expectedOutputs;

    public Dataset(double[][] inputs, double[][] expectedOutputs) {
        this.inputs = inputs;
        this.expectedOutputs = expectedOutputs;
    }
}
