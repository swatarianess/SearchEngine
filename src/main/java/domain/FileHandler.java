package domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileHandler {


    public static boolean invertedIndexExists(String fileDir){
        return loadInvertedIndex(fileDir) != null;
    }

    /**
     * @param fileDir File directory
     * @return Returns the InvertedIndex
     */
    public static Map<String, HashSet<String>> loadInvertedIndex(String fileDir){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        List<String> output;
        StringBuilder result = new StringBuilder();
        try {
            output = Files.readAllLines(Paths.get(fileDir));
        } catch (IOException e) {
            return null;
        }
        output.forEach(result::append);
        return gson.fromJson(result.toString(), TreeMap.class);
    }


    public static void saveInvertedIndexFile(Map<String, HashSet<String>> invertedIndex){
        Gson gson = new Gson();
        String jsonIndex = gson.toJson(invertedIndex);
        try {
            File temp = new File("./data/index/invertedIndex.json");
            temp.getParentFile().mkdirs();

            if (!temp.exists()) temp.createNewFile();

            System.out.println("File exists: " + temp.exists());
            FileWriter writer = new FileWriter(temp);
            writer.write(jsonIndex);
            writer.flush();
            writer.close();
            System.out.println("temp = " + temp.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
