import java.util.ArrayList;
import java.util.List;

public class Cluster {
    private List<Pair<Double, Double>> points;
    private Pair<Double, Double> centroid;

    public Cluster(Pair<Double, Double> pair) {
        points = new ArrayList<>();
        points.add(pair);
        calculateCentroid();
    }

    private void calculateCentroid() {
        double sumX = 0;
        double sumY = 0;
        for (Pair<Double, Double> pair : points) {
            sumX += pair.getX();
            sumY += pair.getY();
        }
        centroid = new Pair<>(sumX / points.size(), sumY / points.size());
    }

    public void addPoint(Pair<Double, Double> point) {
        points.add(point);
        calculateCentroid();
    }

    public int size() {
        return points.size();
    }

    public List<Pair<Double, Double>> getPoints() {
        return points;
    }

    public void setPoints(List<Pair<Double, Double>> points) {
        this.points = points;
    }

    public Pair<Double, Double> getCentroid() {
        return centroid;
    }

    public void setCentroid(Pair<Double, Double> centroid) {
        this.centroid = centroid;
    }
}
