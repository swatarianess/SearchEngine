import domain.DocumentHandler;
import indexer.Indexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class IndexerTest {

    private Indexer indexer = new Indexer();
    private DocumentHandler dh = new DocumentHandler();

    @BeforeEach
    @DisplayName("Reinitialize documentHandler")
    void setUp() throws IOException {
        //Initialize stuff
        System.out.println("@BeforeAll - Executes once before all test methods");
        dh.addDocument("Document_", "./data/Stories/");
    }

    @Test
    void tf() {
        double noDoc1 = indexer.tf(dh.getDocumentList().get("Document_1"), "no");
        double anDoc1 = indexer.tf(dh.getDocumentList().get("Document_1"), "and");

        double noDoc2 = indexer.tf(dh.getDocumentList().get("Document_2"), "no");
        double anDoc2 = indexer.tf(dh.getDocumentList().get("Document_2"), "and");

        double noDoc3 = indexer.tf(dh.getDocumentList().get("Document_3"), "no");
        double anDoc3 = indexer.tf(dh.getDocumentList().get("Document_3"), "and");

        System.out.println("Term Frequencies");

        System.out.println("Document 1");
        System.out.printf("\tno: %.3f%%\n", noDoc1*100);
        System.out.printf("\tand: %.3f%%\n", anDoc1*100);

        System.out.println("Document 2");
        System.out.printf("\tno: %.3f%%\n", noDoc2*100);
        System.out.printf("\tand: %.3f%%\n", anDoc2*100);

        System.out.println("Document 3");
        System.out.printf("\tno: %.3f%%\n", noDoc3*100);
        System.out.printf("\tand: %.3f%%\n", anDoc3*100);

    }

    @Test
    void idf() {

        double noDoc = indexer.idf(dh.getDocumentList().values(), "her");
        double anDoc = indexer.idf(dh.getDocumentList().values(), "she");
        double isDoc = indexer.idf(dh.getDocumentList().values(), "them");

        System.out.println("Inverse Document Frequencies");

        System.out.printf("\tno: %f%%\n", noDoc);
        System.out.printf("\tand: %f%%\n", anDoc);
        System.out.printf("\tis: %f%%\n", isDoc);

    }

    @Test
    void tdIdf() {

        double herDoc = indexer.tdIdf(dh.getDocumentList().get("Document_6"),dh.getDocumentList().values(),"her");
        double sheDoc = indexer.tdIdf(dh.getDocumentList().get("Document_2"),dh.getDocumentList().values(),"she");
        double himDoc = indexer.tdIdf(dh.getDocumentList().get("Document_8"),dh.getDocumentList().values(),"him");

        System.out.printf("her: %s\n", herDoc);
        System.out.printf("she: %s\n", sheDoc);
        System.out.printf("him: %s\n", himDoc);
    }

    @Test
    void generateIndex(){
        indexer.generateInvertedIndex(dh.getDocumentList());
    }

    @Test
    void addToInvertedIndex(){
        indexer.generateInvertedIndex(dh.getDocumentList());
    }

}