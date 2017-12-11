package IRPact_modellierung.helper;

import java.awt.geom.Point2D;
import java.util.Vector;

/**
 * This class used to calculate distances between two vectors, implementing p-metrics (explicitly euclidean, manhattan and maximum metric as well).
 * If instantiated, a string for the chosen metric is used (currentMetric), that needs to be set every time the metric is changed.
 *
 * @author Simon Johanning
 */
public class MetricHelper {

    /**
     * Calculates the (2-dimensional) distance between the vectors a and b according to currentMetric
     *
     * @param a vector between which the metric is calculated
     * @param b vector between which the metric is calculated
     * @param metric
     * @return the value of the distance function between a and b in the current metric
     * @throws IllegalStateException gets thrown when currentMetric is either not set or set to a value that is not implemented
     */
    public static double calculateDistance(Point2D a, Point2D b, String metric) throws IllegalStateException {
        //select which metric is used to calculate the difference between the vectors through the corresponding distance function
        switch (metric) {
            case "manhattanMetric":
                return manhattanDistance(a, b);
            case "euclideanMetric":
                return euclidianDistance(a, b);
            case "maximumMetric":
                return maximumDistance(a, b);
            default:
                throw new IllegalArgumentException("Metric "+metric+" is not implemented!!\nPlease provide a valid metric!");
        }
    }


    /**
     * Calculates the (2-dimensional) p-distance between the vectors a and b
     *
     * @param a (2-dimensional) vector between which the metric is calculated
     * @param b (2-dimensional) vector between which the metric is calculated
     * @return the value of the distance function between a and b in the (induced) p-metric
     * @throws IllegalArgumentException gets thrown when  p-value is < 1 (and thus not implemented)
     */
    public static double calculateDistance(double p, Point2D a, Point2D b) throws IllegalArgumentException{
        if(p>=1.0){
            //Calculate distance between vectors through the pNorm on the difference vector (d(a,b)=||a-b||p)
            //calculate distance through metric-inducing pNorm of the difference vector
            return pNorm(p, new Point2D.Double(a.getX() - b.getX(), a.getY() - b.getY()));
        } else {
            throw new IllegalArgumentException("p must be larger than 1.0, but is " + p);
        }
    }

    /**
     * Calculates the (2-dimensional) manhattan/taxicab distance between the vectors a and b
     *
     * @param a (2-dimensional) vector between which the metric is calculated
     * @param b (2-dimensional) vector between which the metric is calculated
     * @return the value of the distance function between a and b in the manhattan metric
     */
    public static double manhattanDistance(Point2D a, Point2D b){
        //Calculate distance through pDistance, since manhattan distance is a special case of p-distance (with p=1)
        return pDistance(1.0, a, b);
    }

    /**
     * Calculates the (2-dimensional) euclidian distance between the vectors a and b
     *
     * @param a (2-dimensional) vector between which the metric is calculated
     * @param b (2-dimensional) vector between which the metric is calculated
     * @return the value of the distance function between a and b in the euclidian metric
     */
    public static double euclidianDistance(Point2D a, Point2D b){
        //Calculate distance through pDistance, since euclidian distance is a special case of p-distance (with p=2)
        return pDistance(2.0, a, b);
    }

    /**
     * Calculates the (2-dimensional) maximum distance between the vectors a and b
     *
     * @param a (2-dimensional) vector between which the metric is calculated
     * @param b (2-dimensional) vector between which the metric is calculated
     * @return the value of the distance function between a and b in the maximum (infinity) metric
     */
    public static double maximumDistance(Point2D a, Point2D b){
        //calculate the difference vector between the input vectors whose norm induces the metric
        //calculate distance through inducing norm on the difference vector (d(a,b)=||a-b||)
        return maximumNorm(new Point2D.Double(Math.abs(a.getX() - b.getX()), Math.abs(a.getY() - b.getY())));
    }

    /**
     * Calculates the (2-dimensional) p-distance between the vectors a and b
     *
     * @param a (2-dimensional) vector between which the metric is calculated
     * @param b (2-dimensional) vector between which the metric is calculated
     * @return the value of the distance function between a and b in the (induced) p-metric
     * @throws IllegalArgumentException gets thrown when  p-value is < 1 (and thus not implemented)
     */
    public static double pDistance(double p, Point2D a, Point2D b) throws IllegalArgumentException{
        if(p>=1.0){
            //Calculate distance between vectors through the pNorm on the difference vector (d(a,b)=||a-b||p)
            //calculate distance through metric-inducing pNorm of the difference vector
            return pNorm(p, new Point2D.Double(a.getX() - b.getX(), a.getY() - b.getY()));
        } else {
            throw new IllegalArgumentException("p must be larger than 1.0, but is " + p);
        }
    }

    /**
     * Calculates the norm of the provided (2-dimensional)vector using the maximum norm
     *
     * @param differenceVector 2-dimensional vector whos norm is to be caluclated
     * @return the norm of the provided vector
     */
    private static double maximumNorm(Point2D differenceVector) {
        //Maximum norm is defined as largest component in the vector
        return Math.max(differenceVector.getX(), differenceVector.getY());
    }

    /**
     * Calculates the norm of the provided (d-dimensional)vector using the maximum norm
     *
     * @param differenceVector d-dimensional vector whos norm is to be caluclated
     * @return the norm of the provided vector
     */
    private static double maximumNorm(Vector<Number> differenceVector) {
        //determine largest component in the vector
        double currentMax = 0.0;
        for(int i=0; i<differenceVector.size(); i++){
            if((double) differenceVector.get(i) > currentMax){
                currentMax = (double) differenceVector.get(i);
            }
        }
        //Maximum norm is defined as largest component in the vector
        return currentMax;
    }

    /**
     * Calculates the norm of the provided (d-dimensional)vector using the (inducing) p-norm
     *
     * @param differenceVector d-dimensional vector whos norm is to be calculated
     * @param p p-value of the corresponding norm
     * @return the norm of the provided vector
     */
    private static double pNorm(double p, Vector<Number> differenceVector) {
        double sum = 0.0;
        //calculate norm value according to definition of p-norm
        for(int i=0; i<differenceVector.size(); i++){
            sum += Math.pow(Math.abs((double) differenceVector.get(i)),p);
        }
        return Math.pow(sum, 1/p);
    }

    /**
     * Calculates the norm of the provided (2-dimensional)vector using the (inducing) p-norm
     *
     * @param a 2-dimensional vector whos norm is to be calculated
     * @param p p-value of the corresponding norm
     * @return the norm of the provided vector
     */
    private static double pNorm(double p, Point2D a) {
        //calculate norm value according to definition of p-norm
        double sum = Math.pow(Math.abs(a.getX()), p) + Math.pow(Math.abs(a.getY()), p);
        return Math.pow(sum, 1/p);
    }

}
