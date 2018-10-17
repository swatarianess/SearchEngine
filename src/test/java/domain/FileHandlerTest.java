package domain;

import indexer.Indexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

class FileHandlerTest {

    DocumentHandler dh = new DocumentHandler();
    Indexer indexer = new Indexer();
    FileHandler fh = new FileHandler();

    @BeforeEach
    void setup() throws IOException {
        dh.addDocument("./data/Stories/");
        HashMap<String, List<String>> invertedHashmap = new HashMap<>();
        dh.getProperDocumentList().forEach(document -> invertedHashmap.put(document.getDocumentName(),document.getDocumentWords()));
        indexer.generateInvertedIndex(invertedHashmap);
    }

    @Test
    void saveInvertedIndexFile() {
        FileHandler.saveInvertedIndexFile(indexer.getInvertedIndex());

    }
}