package domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

class FileHandlerTest {

    DocumentHandler dh = new DocumentHandler();

    @BeforeEach
    void setup() throws IOException {
        dh.addDocument("./data/Stories/");
    }

    @Test
    void existingInvertedIndexFileTest(){
        Assertions.assertFalse(FileHandler.invertedIndexExists("./data/Stories/index.ofn"));
        Assertions.assertTrue(FileHandler.invertedIndexExists("./data/index/invertedIndex.json"));
    }

    @Test
    void saveInvertedIndexFile() {
        FileHandler.saveInvertedIndexFile(dh.getIndexer().getInvertedIndex());
    }

    @Test
    void loadDataTest(){
       Map<String, HashSet<String>> result = FileHandler.loadInvertedIndex("./data/index/invertedIndex.json");
        System.out.println("HashMap loaded = " + result);
    }
}