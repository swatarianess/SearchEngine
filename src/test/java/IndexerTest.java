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

    private static DocumentHandler dh = new DocumentHandler();
    private static Indexer indexer = dh.getIndexer();
    private static String term = "about";
    private static Collection<String> query = Arrays.asList(dh.parseQuery("little pigs".split("\\s+")));

    @BeforeAll
    @DisplayName("Reinitialize documentHandler")
    static void setUp() throws IOException {
        //Initialize stuff
        System.out.println("@BeforeAll - Adds all documents to index");
        IndexerTest.dh.addDocument("./data/Stories/");
        System.out.println();
    }

    @AfterEach
    @DisplayName("Save inverted index!")
    void setDown(){
        if (!FileHandler.invertedIndexExists(FileHandler.defaultFileDir))
        FileHandler.saveInvertedIndexFile(indexer.getInvertedIndex());
    }

    @Test
    @Description("Term frequency of a term")
    void tfTest() {
        double[] results = new double[dh.getDocuments().size()];
        List<Document> documents = dh.getDocuments();
        for (int i = 0; i < documents.size(); i++) {
            Document d = documents.get(i);
            results[i] = indexer.termFrequency(d.getDocumentWords(), term);
        }
        System.out.println("Term frequency of '" + term + "': " + Arrays.toString(results));
    }

    @Test
    @Description("Term count of a term in a document")
    void tfCountTest() {
        double[] results = new double[dh.getDocuments().size()];
        double[] results2 = new double[dh.getDocuments().size()];
        List<Document> documents = dh.getDocuments();
        for (int i = 0; i < documents.size(); i++) {
            Document d = documents.get(i);
            results[i] = 1 + Math.log10(indexer.tfCount(d.getDocumentWords(), term));
            results2[i] = (indexer.tfCount(d.getDocumentWords(), term));
        }
        System.out.println("Term frequency of '" + term + "': " + Arrays.toString(results));
        System.out.println("Term frequency of '" + term + "': " + Arrays.toString(results2));
    }

    @Test
    void idf() {
        Collection<List<String>> collectionOfDocuments = new ArrayList<>();
        dh.getDocuments().forEach(document -> collectionOfDocuments.add(document.getDocumentWords()));
        double idfOfHad = dh.getIndexer().idf(collectionOfDocuments,term);

        double idfOfTerm = dh.getIndexer().getInvertedIndex().get(term).size();
        System.out.println("Documents = " + dh.getDocuments().size());
        System.out.println("idfOfTerm = " + idfOfTerm);
        System.out.printf("idf of '%s': %s \n", term,  idfOfHad);
    }

    @Test
    void tfidf() {
        Collection<List<String>> collectionOfDocuments = new ArrayList<>();
        dh.getDocuments().forEach(document -> collectionOfDocuments.add(document.getDocumentWords()));
        HashMap<String, Pair<String,Number>> idfMap = new HashMap<>();

        for (Document d : dh.getDocuments())
        idfMap.put(d.getDocumentName(),new Pair<>(term,dh.getIndexer().tfIdf(d.getDocumentWords(),collectionOfDocuments,term)));

        System.out.println("idfMap = " + idfMap);

    }

    @Test
    void generateIndex() {
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
        Collection<List<String>> collectionOfDocuments = new ArrayList<>();
        dh.getDocuments().forEach(document -> collectionOfDocuments.add(document.getDocumentWords()));
        float[] termWeights = new float[dh.getDocuments().size()];

        List<Document> documents = dh.getDocuments();
        for (int i = 0; i < documents.size(); i++) {
            Document d = documents.get(i);
            termWeights[i] = (float) dh.getIndexer().termWeight(indexer.termFrequency(d.getDocumentWords(), term), indexer.idf(collectionOfDocuments, term));
        }

        System.out.println("dh = " + dh.getDocuments());
        System.out.println("termWeights = " + Arrays.toString(termWeights));
    }


    @Test
    void scoreTest() {
        ArrayList<Double> resultList = new ArrayList<>();
        Map<String,Double> resultMap = new HashMap<>();

        for (Document d : dh.getDocuments()){
            double score = indexer.scoreQueryDocument((List<String>) query,d);
            if (score > 0) resultMap.put(d.getDocumentName(),score);

        }
        System.out.println("dh.getDocuments() = " + dh.getDocuments());
        resultMap.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(System.out::println);
    }

}