import java.util.ArrayList;
import java.util.List;

public class ClustroidCalculator {
    public static void main(String[] args) {
        List<Pair<Integer, Integer>> clusterA = new ArrayList<>();
        List<Pair<Integer, Integer>> clusterB = new ArrayList<>();
        List<Pair<Integer, Integer>> clusterC = new ArrayList<>();
        initA(clusterA);
        initB(clusterB);
        initC(clusterC);
        System.out.println(calcuteClustroid(clusterA));
        System.out.println();
        System.out.println(calcuteClustroid(clusterB));
        System.out.println();
        System.out.println(calcuteClustroid(clusterC));
    }

    private static Pair<Integer, Integer> calcuteClustroid(List<Pair<Integer, Integer>> cluster) {
        Double[][] matrix = new Double[cluster.size()][cluster.size()];
        for (int i = 0; i < cluster.size(); i++) {
            for (int j = i; j < cluster.size(); j++) {
                matrix[i][j] = calcuteDistance(cluster.get(i), cluster.get(j));
                matrix[j][i] = matrix[i][j];
            }
        }
        for (int i = 0; i < cluster.size(); i++) {
            for (int j = 0; j < cluster.size(); j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }
        int num = getMin(matrix);
        return cluster.get(num);
    }

    private static Double calcuteDistance(Pair<Integer, Integer> first, Pair<Integer, Integer> second) {
        return Math.sqrt(Math.pow(first.getX() - second.getX(), 2) + Math.pow(first.getY() - second.getY(), 2));
    }

    private static int getMin(Double[][] matrix) {
        int min = 0;
        double result = Double.MAX_VALUE;
        double currentResult = 0;
        for (int i = 0; i < matrix.length; i++) {
            currentResult = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                currentResult += matrix[i][j];
            }
            if (currentResult - result < 1e-7){
                result = currentResult;
                min = i;
            }
        }
        return min;
    }

    private static void initC(List<Pair<Integer, Integer>> cluster) {
        cluster.add(new Pair<>(4, 10));
        cluster.add(new Pair<>(7, 10));
        cluster.add(new Pair<>(4, 8));
        cluster.add(new Pair<>(6, 8));
    }

    private static void initB(List<Pair<Integer, Integer>> cluster) {
        cluster.add(new Pair<>(3, 4));
        cluster.add(new Pair<>(2, 2));
        cluster.add(new Pair<>(5, 2));
    }

    private static void initA(List<Pair<Integer, Integer>> cluster) {
        cluster.add(new Pair<>(12, 6));
        cluster.add(new Pair<>(10, 5));
        cluster.add(new Pair<>(11, 4));
        cluster.add(new Pair<>(12, 3));
        cluster.add(new Pair<>(9, 3));
    }

}
