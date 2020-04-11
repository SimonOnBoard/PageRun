package starter;

import javax.management.QueryEval;
import java.io.*;
import java.sql.*;
import java.util.*;

public class RunerNextV {
    public static Map<String, ArrayList<String>> listMap = new HashMap<>();
    public static Map<String, ArrayList<String>> destination = new HashMap<>();
    public static Map<String, Double> currentRun = new HashMap<>();
    private static final double d = 0.85;
    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://database-1.cdm5bcwbgp5i.us-east-1.rds.amazonaws.com:5432/graph", "postgres", "12345123");
        //language=sql
        String sql = "SELECT DISTINCT source from graph";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            currentRun.put(resultSet.getString("source"),0d);
            listMap.put(resultSet.getString("source"), new ArrayList<>());
            destination.put(resultSet.getString("source"), new ArrayList<>());
        }
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * from graph where source = ?");
        for (String string : listMap.keySet()) {
            preparedStatement1.setString(1, string);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            ArrayList<String> current = listMap.get(string);
            while (resultSet1.next()) {
                destination.get(resultSet1.getString(2)).add(string);
                current.add(resultSet1.getString(2));
            }
            System.out.println(string + ":" + listMap.get(string).size());
        }
        double n = listMap.keySet().size();
        int i = startRun(n);
        double sum = 0;
        Writer out;
        PrintWriter printWriter = new PrintWriter(new FileWriter(new File("results.txt")));
        printWriter.println("Iterations:" + i);
        for(String key: currentRun.keySet()){
            printWriter.println(key + " :" + currentRun.get(key)/n);
            sum += (currentRun.get(key)/n);
        }
        printWriter.println("Result sum:" + i);
        printWriter.close();
        //System.out.println(sum);



//        String start = null;
//        int max = 0;
//        int k;
//        for(String string: destination.keySet()){
//            if((k = destination.get(string).size()) > max) {
//                max = k;
//                start = string;
//            }
//        }
    }

    private static int startRun(double n) {
        int i = 0;
        while(!checkSum(n)) {
            i++;
            for(String string: listMap.keySet()){
                currentRun.put(string,calculate(string));
            }
        }
        return i;
    }

    private static Double calculate(String string) {
        return  (1-d) + d * (getSum(string));
    }

    private static double getSum(String string) {
        double sum = 0d;
        for(String string1: destination.get(string)){
            sum+= currentRun.get(string1)/listMap.get(string1).size();
        }
        return sum;
    }

    private static boolean checkSum(double n ) {
        double eps = 1e-15;
        double sum = 0d;
        for(String string: currentRun.keySet()){
            sum += currentRun.get(string);
        }
        return (1 - sum/n) < eps;
    }

//    private static Queue<String> launchQ(Pair<String, Integer> start) {
//        Queue<String> nextHops = new LinkedList<>();
//        for(String key: destination.keySet()){
//            if(destination.get(key).size() == 1 && destination.get(key).contains(start.key)){
//                double res = (1-d) + d * (currentRun.get(start.key)/listMap.get(start.key).size());
//                currentRun.put(key, res);
//                nextHops.add(key);
//            }
//        }
//        return nextHops;
//    }
//
//    private static Pair<String, Integer> nextStart(Pair<String, Integer> start) {
//        String hop = null;
//        int max = 0;
//        int k;
//        for(String string: destination.keySet()){
//            k = destination.get(string).size();
//            if(k > max && k < start.value) {
//                max = k;
//                hop = string;
//            }
//        }
//        if(hop == null) throw new IllegalStateException();
//        return new Pair<>(hop,max);
//    }
}
