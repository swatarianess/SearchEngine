import com.sun.org.glassfish.gmbal.Description;
import domain.DocumentHandler;
import indexer.Indexer;
import model.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

class IndexerTest {

    private Indexer indexer = new Indexer();
    private DocumentHandler dh = new DocumentHandler();

    @BeforeEach
    @DisplayName("Reinitialize documentHandler")
    void setUp() throws IOException {
        //Initialize stuff
        System.out.println("@BeforeAll - Executes once before all test methods");
        dh.addDocument("./data/Stories/");
    }

    @Test
    void tf() {
        String term = "kill";
        double[] results = new double[dh.getDocuments().size()];
        List<Document> documents = dh.getDocuments();
        for (int i = 0; i < documents.size(); i++) {
            Document d = documents.get(i);
            results[i] = indexer.tf(d.getDocumentWords(), term);
        }
        System.out.println("Term frequency of '" + term + "': " + Arrays.toString(results));
        Assertions.assertEquals(results[0], 7.0);
    }

    @Test
    void idf() {
        String term = "sign";
        Collection<List<String>> collectionOfDocuments = new ArrayList<>();
        dh.getDocuments().forEach(document -> collectionOfDocuments.add(document.getDocumentWords()));
        double idfOfHad = dh.getIndexer().idf(collectionOfDocuments,term);
        System.out.printf("idf of '%s': %s ", term,  idfOfHad);
    }

    @Test
    void tdIdf() {


    }

    @Test
    void generateIndex() {
        Assertions.assertEquals(0, indexer.getInvertedIndex().size());

        HashMap<String, List<String>> invertedHashmap = new HashMap<>();
        dh.getDocuments().forEach(document -> invertedHashmap.put(document.getDocumentName(), document.getDocumentWords()));

        indexer.generateInvertedIndex(invertedHashmap);
        Assertions.assertNotEquals(0, indexer.getInvertedIndex().size());
        System.out.println("invertedHashmap = " + indexer.getInvertedIndex());
        System.out.println("Most common term: " + indexer.getInvertedIndex().entrySet().stream().max(Comparator.comparingInt(value -> value.getValue().size())).get().getKey());
    }

    @Test
    void addToInvertedIndex() {
//        indexer.generateInvertedIndex(dh.getDocuments());
        HashMap<String, List<String>> invertedHashmap = new HashMap<>();
        dh.getDocuments().forEach(document -> invertedHashmap.put(document.getDocumentName(), document.getDocumentWords()));
        indexer.generateInvertedIndex(invertedHashmap);
        System.out.println("indexer = " + indexer.getInvertedIndex());
    }

    @Test
    @Description("Get weight of term in a document")
    void getTermWeight() {

    }

}