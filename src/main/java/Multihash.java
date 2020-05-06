import com.opencsv.CSVReader;
import starter.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class Multihash {
    public static void main(String[] args) throws IOException {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader("transactions.csv"))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values[0].split(";")));
            }
        }
        System.out.println(records.size());
        Map<String, Integer> countMap = new HashMap<>();
        Map<String, List<String>> bucketMap = new HashMap<>();
        String bucket;
        String product;
        for (int i = 1; i < records.size(); i++) {
            bucket = records.get(i).get(1);
            product = records.get(i).get(0);
            if (countMap.get(product) != null) {
                countMap.put(product, countMap.get(product) + 1);
            } else {
                countMap.put(product, 0);
            }

            if (bucketMap.get(bucket) != null) {
                List<String> b = bucketMap.get(bucket);
                b.add(product);
                bucketMap.put(bucket, b);
            } else {
                bucketMap.put(bucket, new ArrayList<>());
            }
        }
        System.out.println("Buckets size: " + bucketMap.keySet().size());
        System.out.println("Product size: " + countMap.keySet().size());
        Map<String, Integer> numberMap = new HashMap<>();
        int i = 0;
        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            numberMap.put(entry.getKey(), i);
            i++;
        }
        System.out.println("Number map: " + numberMap.size());
        System.out.println(i + 1);
        Map<String,List<Pair<Integer,Integer>>> bucketPairs = new HashMap<>();
        int size;
        for(String buck: bucketMap.keySet()){
            List<String> strings = bucketMap.get(buck);
            size = strings.size();
            List<Pair<Integer,Integer>> pairs = new ArrayList<>();
            bucketPairs.put(buck, new ArrayList<>());
            for(int s = 0; s < size - 1; s++){
                for(int end = s + 1; end < size; end++) {
                    String first = strings.get(s);
                    String last = strings.get(end);
                    Pair<Integer, Integer> pair = new Pair<>(numberMap.get(first), numberMap.get(last));
                    pairs.add(pair);
                }
            }
        }
        int mod = numberMap.size();

        Map<Integer, List<Pair<Integer,Integer>>> firstBucket = new HashMap<>();

        for(Map.Entry<String,List<Pair<Integer,Integer>>> entry: bucketPairs.entrySet()){
            for(Pair<Integer,Integer> pair : entry.getValue()){
                long hash = 2* pair.value + 3 * pair.key;
                int index = (int)(hash % mod);
                if(firstBucket.get(index) != null){
                    List<Pair<Integer,Integer>> pairs = firstBucket.get(index);
                    pairs.add(pair);
                    firstBucket.put(index,pairs);
                }
                else{
                    List<Pair<Integer,Integer>> pairs = new ArrayList<>();
                    pairs.add(pair);
                    firstBucket.put(index,pairs);
                }
            }
        }

        Map<Integer, List<Pair<Integer,Integer>>> secondBucket = new HashMap<>();

        for(Map.Entry<String,List<Pair<Integer,Integer>>> entry: bucketPairs.entrySet()){
            for(Pair<Integer,Integer> pair : entry.getValue()){
                long hash = 5 * pair.value + 7 * pair.key;
                int index = (int)(hash % mod);
                if(firstBucket.get(index) != null){
                    List<Pair<Integer,Integer>> pairs = firstBucket.get(index);
                    pairs.add(pair);
                    secondBucket.put(index,pairs);
                }
                else{
                    List<Pair<Integer,Integer>> pairs = new ArrayList<>();
                    pairs.add(pair);
                    secondBucket.put(index,pairs);
                }
            }
        }

        //calculating result added after 3 min after control work ended (19:03)
        List<String> result = new ArrayList<>();
        List<Integer> failed = new ArrayList<>();
        for(Map.Entry<String,Integer> entry: countMap.entrySet()){
            if(entry.getValue() < 3){
                failed.add(numberMap.get(entry.getKey()));
            }
            else{
                result.add(entry.getKey());
            }
        }
        Set<Pair<Integer,Integer>> res = new HashSet<>();
        for(Map.Entry<Integer,List<Pair<Integer,Integer>>> entry: firstBucket.entrySet()){
            if(entry.getValue().size() < 3){
                continue;
            }
            res.addAll(entry.getValue());
        }
        for(Map.Entry<Integer,List<Pair<Integer,Integer>>> entry: secondBucket.entrySet()){
            if(entry.getValue().size() < 3){
                continue;
            }
            res.addAll(entry.getValue());
        }
        System.out.println("Result size:" + result.size() + res.size());
    }
}
