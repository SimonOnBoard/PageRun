package starter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Hits {
    public static Map<String, ArrayList<String>> listMap = new HashMap<>();

    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/graph", "postgres", "qscwerdf");


        //достаём список всех вершин их направлений
        //language=sql
        String sql = "SELECT DISTINCT source from graph";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            listMap.put(resultSet.getString("source"), new ArrayList<>());
        }
        sql = "SELECT DISTINCT destination from graph";
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
        resultSet = preparedStatement2.executeQuery();
        while (resultSet.next()) {
            listMap.put(resultSet.getString("destination"), new ArrayList<>());
        }
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * from graph where source = ?");
        for (String string : listMap.keySet()) {
            preparedStatement1.setString(1, string);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            ArrayList<String> current = listMap.get(string);
            while (resultSet1.next()) {
                current.add(resultSet1.getString(2));
            }
            System.out.println(string + ":" + listMap.get(string).size());
        }
        int size = listMap.keySet().size();
        ArrayList<String> keys = new ArrayList<>();
        keys.addAll(listMap.keySet());

        //делаем две матрицы для наглядности: в реальности можно обойтись одной
        double matrix[][] = new double[size][size];
        for (int i = 0; i < size; i++) {
            ArrayList<String> curr = listMap.get(keys.get(i));
            for (int j = 0; j < size; j++) {
                if (curr.contains(keys.get(j))) {
                    matrix[i][j] = 1d;
                } else {
                    matrix[i][j] = 0d;
                }
            }
        }

        //выводим матрицы на экран чтобы убедиться в их правильности
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + "    ");
            }
            System.out.print(keys.get(i) + " ");
            System.out.println();
        }
        System.out.println();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[j][i] + "    ");
            }
            System.out.print(keys.get(i) + " ");
            System.out.println();
        }
        System.out.println(size);

        //запоняем вектор h начальными значениями
        double[] h = new double[size];
        for (int i = 0; i < size; i++) {
            h[i] = 1;
        }
        //инициализируем a
        double[] a = new double[size];

        for(int i = 0; i < 20; i++){
            a = calculateA(matrix, h, size);
            h = calculateH(matrix, a, size);


            //выводим результат на экран
            System.out.println(i + ": " + "a: " + Arrays.toString(a));
            System.out.println(i + ": " + "h: " + Arrays.toString(h));
        }
        System.out.print("\t\t\t\t");
        for(int i = 0; i < keys.size(); i++){
            System.out.print(keys.get(i) + "\t" + "\t" + "\t" + "\t");
        }
    }

    //методы братья для вычисления а и h
    private static double[] calculateH(double[][] matrix, double[] a, int size) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                sum += matrix[i][j] * a[j];
            }
            result[i] = sum;
        }
        return normalize(result);
    }

    private static double[] calculateA(double[][] transp, double[] h, int size) {
        double[] result = new double[size];
        for (int i = 0; i < size; i++) {
            double sum = 0;
            for (int j = 0; j < size; j++) {
                sum += transp[j][i] * h[j];
            }
            result[i] = sum;
        }
        return normalize(result);
    }

    private static double[] normalize(double[] result) {
        double max = result[0];
        for(double x: result){
            if(x - max > 1e-6 ){
                max = x;
            }
        }
        for(int i = 0; i < result.length; i++){
            result[i] = result[i]/max;
        }
        return result;
    }
}
