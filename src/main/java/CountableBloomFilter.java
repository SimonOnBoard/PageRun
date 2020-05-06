import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CRC32C;
public class CountableBloomFilter {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("data.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        ArrayList<String> words = new ArrayList<>();
        String[] strings;
        //Фильтруем оригинальный текст, нам нужны только русские слова
        while (line != null) {
            line = line.replaceAll("[^\\p{L}\\p{Z}]", " ");
            strings = line.split(" ");
            for (int i = 0; i < strings.length; i++) {
                if (strings[i].length() != 0) {
                    if ((strings[i].charAt(0) >= 'А' && strings[i].charAt(0) <= 'Я') || (strings[i].charAt(0) >= 'а' && strings[i].charAt(0) <= 'я')) {
                        words.add(strings[i]);
                    }
                }
            }
            line = bufferedReader.readLine();
        }
        //Инициализицируем пустой фильтр
        byte[] filter = new byte[450];
        int mod = 450;
        Adler32 adler32 = new Adler32();
        CRC32 crc32 = new CRC32();
        long firstVal;
        long secondVal;
        long thirdVal;
        int index;
        for(String word : words){
            //Прогоняем наше слово через 3 hash functions
            adler32.update(word.getBytes(),0,word.length());
            firstVal = adler32.getValue();
            crc32.update(word.getBytes(),0,word.length());
            secondVal = crc32.getValue();
            thirdVal = Math.abs(word.hashCode());
            //Прибавлеяем 1 к позициям, которые равны val % mod
            index = (int)(firstVal % mod);
            filter[index] += 1;
            index = (int)(secondVal % mod);
            filter[index] += 1;
            index = (int)(thirdVal % mod);
            filter[index] += 1;
        }
        System.out.println(Arrays.toString(filter));
    }
}
