import com.sun.org.glassfish.gmbal.Description;
import domain.DocumentHandler;
import domain.FileHandler;
import indexer.Indexer;
import javafx.util.Pair;
import model.Document;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.*;

class IndexerTest {

    private DocumentHandler dh = new DocumentHandler();
    private Indexer indexer = dh.getIndexer();

    @BeforeEach
    @DisplayName("Reinitialize documentHandler")
    void setUp() throws IOException {
        //Initialize stuff
        System.out.println("@BeforeAll - Executes once before all test methods");
        dh.addDocument("./data/Test/");
    }

    @AfterEach
    @DisplayName("Save inverted index!")
    void setDown(){
        FileHandler.saveInvertedIndexFile(indexer.getInvertedIndex());
    }

    @Test
    void tf() {
        String term = "i";
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
        String term = "game";
        Collection<List<String>> collectionOfDocuments = new ArrayList<>();
        dh.getDocuments().forEach(document -> collectionOfDocuments.add(document.getDocumentWords()));
        double idfOfHad = dh.getIndexer().idf(collectionOfDocuments,term);
        System.out.printf("idf of '%s': %s \n", term,  idfOfHad);
    }

    @Test
    void tfidf() {
        String term = "life";

        Collection<List<String>> collectionOfDocuments = new ArrayList<>();
        dh.getDocuments().forEach(document -> collectionOfDocuments.add(document.getDocumentWords()));
        HashMap<String, Pair<String,Number>> idfMap = new HashMap<>();

        for (Document d : dh.getDocuments())
        idfMap.put(d.getDocumentName(),new Pair<>(term,dh.getIndexer().tfIdf(d.getDocumentWords(),collectionOfDocuments,term)));

        System.out.println("idfMap = " + idfMap);

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
        HashMap<String, List<String>> invertedHashmap = new HashMap<>();
        dh.getDocuments().forEach(document -> invertedHashmap.put(document.getDocumentName(), document.getDocumentWords()));
        indexer.generateInvertedIndex(invertedHashmap);
        System.out.println("indexer = " + indexer.getInvertedIndex());
    }

    @Test
    @Description("Get weight of term in a document")
    void getTermWeight() {
        String term = "this";
        float[] termWeights = new float[dh.getDocuments().size()];

        List<Document> documents = dh.getDocuments();
        for (int i = 0; i < documents.size(); i++) {
            Document d = documents.get(i);
            termWeights[i] = (float) dh.getIndexer().termWeight(term, d, dh.getDocuments().size());
        }

        System.out.println("dh = " + dh.getDocuments());
        System.out.println("termWeights = " + Arrays.toString(termWeights));
    }

}