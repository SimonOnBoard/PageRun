package starter;

import javax.swing.plaf.FileChooserUI;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

public class Runer {
    public static Map<String, ArrayList<String>> listMap = new HashMap<>();

    public static void main(String[] args) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://database-2-instance-1.cdm5bcwbgp5i.us-east-1.rds.amazonaws.com:5432/graph", "postgres", "qscwerdf");
        //language=sql
        String sql = "SELECT DISTINCT source from graph";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            listMap.put(resultSet.getString("source"), new ArrayList<>());
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
        BigDecimal matrix[][] = new BigDecimal[size][size];
        BigDecimal bigDecimalSize = new BigDecimal((double) size);
        for (int i = 0; i < size; i++) {
            ArrayList<String> curr = listMap.get(keys.get(i));
            for (int j = 0; j < size; j++) {
                if (curr.contains(keys.get(j))) {
                    matrix[j][i] = BigDecimal.ONE.divide(new BigDecimal(curr.size()), 50, RoundingMode.DOWN);
                } else {
                    matrix[j][i] = BigDecimal.ZERO;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            System.out.print(keys.get(i) + " ");
        }
        System.out.print("\n");

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix[i][j] + "    ");
            }
            System.out.print(keys.get(i) + " ");
            System.out.println();
        }
        BigDecimal[] vector = new BigDecimal[size];
        for (int i = 0; i < size; i++) {
            vector[i] = BigDecimal.ONE.divide(bigDecimalSize, 50, RoundingMode.DOWN);
        }
        System.out.println(Arrays.toString(vector));
        PrintWriter printWriter;
        for (int i = 0; i < 100; i++) {
            vector = doPageRun(matrix, vector, size);
            printWriter = new PrintWriter(new FileWriter(i + ".txt"));
            System.out.println(i);
            for(int j = 0;j < size; j++){
                printWriter.println(keys.get(j) + ": " + vector[j]);
                System.out.println(keys.get(j) + ": " + vector[j]);
                System.out.println("____________________________________________________________________________________");
            }
            printWriter.close();
        }
    }

    private static BigDecimal[] doPageRun(BigDecimal[][] matrix, BigDecimal[] vector, int size) {
        BigDecimal result[] = new BigDecimal[size];
        BigDecimal current;
        for (int i = 0; i < size; i++) {
            current = BigDecimal.valueOf(0d);
            for (int j = 0; j < size; j++) {
                //System.out.println(matrix[i][j] + " " + vector[j]);
                //System.out.println(matrix[i][j].multiply(vector[j]));
                current = current.add(matrix[i][j].multiply(vector[j]));
            }
            current = current.round(new MathContext(20));
            //System.out.println(current);
            result[i] = current;
        }
        return result;
    }
}
