import java.security.SecureRandom;
import java.util.*;

public class Clusterization {
    public static void main(String[] args) {
        List<Pair<Double, Double>> cluster = new ArrayList<>();
        init(cluster);
        List<Pair<Double, Double>> startPoints = calculateStartPoints(cluster, 4);
        System.out.println(startPoints.toString());
        List<Cluster> clusters = initializeClusters(startPoints);
        fullUp(clusters, cluster);
    }

    private static void fullUp(List<Cluster> clusters, List<Pair<Double, Double>> availablePoints) {
        while (availablePoints.size() > 0){
            Pair<Cluster, Pair<Double,Double>> pair = findCandidate(clusters, availablePoints);
            pair.getX().addPoint(pair.getY());
            availablePoints.remove(pair.getY());
        }
        for(Cluster cluster1: clusters){
            System.out.println(cluster1.size() + ": " + cluster1.getPoints().toString());
        }
    }

    private static Pair<Cluster,Pair<Double, Double>> findCandidate(List<Cluster> clusters, List<Pair<Double, Double>> availablePoints) {
        Pair<Cluster,Pair<Double, Double>> candidate = new Pair<Cluster, Pair<Double, Double>>(clusters.get(0), availablePoints.get(0));
        double minDistance = Double.MAX_VALUE;
        double distance;
        for(Cluster currentCluster: clusters){
            for(Pair<Double,Double> point: availablePoints){
                distance = calculateDistance(currentCluster.getCentroid(), point);
                if (distance - minDistance < 1e-15) {
                    minDistance= distance;
                    candidate = new Pair<>(currentCluster, point);
                }
            }
        }
        return candidate;
    }

    private static List<Cluster> initializeClusters(List<Pair<Double, Double>> startPoints) {
        List<Cluster> clusters = new ArrayList<>();
        for (Pair<Double, Double> pair : startPoints) {
            clusters.add(new Cluster(pair));
        }
        return clusters;
    }

    private static List<Pair<Double, Double>> calculateStartPoints(List<Pair<Double, Double>> availablePoints, int i) {
        Random random = new SecureRandom();
        List<Pair<Double, Double>> startPoints = new ArrayList<>();
        startPoints.add(availablePoints.get(random.nextInt(availablePoints.size())));
        availablePoints.removeAll(startPoints);
        while (startPoints.size() != i) {
            Pair<Double, Double> candidate = getNextInitPoint(startPoints, availablePoints);
            startPoints.add(candidate);
            availablePoints.remove(candidate);
        }
        return startPoints;
    }

    private static Pair<Double, Double> getNextInitPoint(List<Pair<Double, Double>> startPoints, List<Pair<Double, Double>> availablePoints) {
        Map<Pair<Double, Double>, Double> distanceMap = new HashMap<>();
        double resultDistance;
        double distance;
        for (Pair<Double, Double> candidate : availablePoints) {
            resultDistance = Double.MAX_VALUE;
            for (Pair<Double, Double> point : startPoints) {
                distance = calculateDistance(candidate, point);
                if (distance - resultDistance < 1e-7) {
                    resultDistance = distance;
                }
            }
            distanceMap.put(candidate, resultDistance);
        }
        return getCandidate(distanceMap);
    }

    private static Pair<Double, Double> getCandidate(Map<Pair<Double, Double>, Double> distanceMap) {
        Optional<Map.Entry<Pair<Double, Double>, Double>> x = distanceMap.entrySet().stream().max(Map.Entry.comparingByValue());
        return x.get().getKey();
    }

    private static Double calculateDistance(Pair<Double, Double> first, Pair<Double, Double> second) {
        return Math.sqrt(Math.pow(first.getX() - second.getX(), 2) + Math.pow(first.getY() - second.getY(), 2));
    }


    private static void init(List<Pair<Double, Double>> cluster) {
        cluster.add(new Pair<>(4d, 10d));
        cluster.add(new Pair<>(7d, 10d));
        cluster.add(new Pair<>(4d, 8d));
        cluster.add(new Pair<>(6d, 8d));
        cluster.add(new Pair<>(3d, 4d));
        cluster.add(new Pair<>(2d, 2d));
        cluster.add(new Pair<>(5d, 2d));
        cluster.add(new Pair<>(12d, 6d));
        cluster.add(new Pair<>(10d, 5d));
        cluster.add(new Pair<>(11d, 4d));
        cluster.add(new Pair<>(12d, 3d));
        cluster.add(new Pair<>(9d, 3d));
    }
}
