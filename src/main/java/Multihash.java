import com.opencsv.CSVReader;
import starter.Pair;

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
        Adler32 adler32 = new Adler32();
        CRC32 crc32 = new CRC32();
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
            System.out.println(strings);
            size = strings.size();
            List<Pair<Integer,Integer>> pairs = new ArrayList<>();
            bucketPairs.put(buck, new ArrayList<>());
            for(int s = 0; s < size - 1; s++){
                for(int end = s; end < size; end++){
                    String first = strings.get(s);
                    String last = strings.get(end);
                    pairs.add(new Pair<>(numberMap.get(first), numberMap.get(last)));
                }
            }
            System.out.println();
            System.out.println("Bucket size: " + size);
            System.out.println("Pairs size: " + pairs.size());
            break;
        }

    }
}
