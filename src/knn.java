import java.util.Arrays;
import java.util.Comparator;

public class knn {
    int dimentions;
    int nValue;
    int size;
    double[][] data;
    double[] label;

    public knn( int nValue) {
        this.nValue = nValue;
    }

    public void fit(double[][] data){
        this.data = data;
        this.size = data.length;
        this.dimentions = data[0].length;
    }

    public double predict(double[] values) {
        double[] distances = new double[size];
        Integer[] indices = new Integer[size];

        // Calculate distances
        for (int i = 0; i < size; i++) {
            distances[i] = getEuclideanDistance(values, data[i]);
            indices[i] = i;
        }

        // Sort indices by distances
        Arrays.sort(indices, Comparator.comparingDouble(i -> distances[i]));

        // Find the most frequent label among the nearest neighbors
        Integer[] nearestIndices = Arrays.copyOfRange(indices, 0, nValue);
        return mostFrequent(nearestIndices);
    }

    private double getEuclideanDistance(double[] x1, double[] x2){
        double sum = 0.0;
        for (int i = 0; i < x1.length; i++) {
            double diff = x1[i] - x2[i+1];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }

    private double mostFrequent(Integer[] nearestIndices) {
        int falseCount = 0;
        int trueCount = 0;

        for (int index : nearestIndices) {
            if (data[index][0] == 0.0) {
                falseCount++;
            } else {
                trueCount++;
            }
        }

        return trueCount >= falseCount ? 1.0 : 0.0;
    }


}

