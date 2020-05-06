import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class Multihash {
    public static void main(String[] args) throws IOException {
        List<List<String>> records = new ArrayList<List<String>>();
        try (CSVReader csvReader = new CSVReader(new FileReader("transactions.csv"))) {
            String[] values = null;
            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
        }
        Adler32 adler32 = new Adler32();
        CRC32 crc32 = new CRC32();
        System.out.println(records.size());
        Map<String, Integer> countMap = new HashMap<>();
        Map<String, List<String>> bucketMap = new HashMap<>();
        for(int i = 0; i < records.size(); i++){
        }
    }
}
