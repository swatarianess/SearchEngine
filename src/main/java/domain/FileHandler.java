package domain;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class FileHandler {
//
//    public static File loadFile(){
//
//    }

    public static void saveInvertedIndexFile(HashMap<String, HashSet<String>> invertedIndex){
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
