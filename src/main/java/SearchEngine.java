import domain.DocumentHandler;
import indexer.Indexer;

import java.io.IOException;

public class SearchEngine {

    private String DOCUMENT_DIRECTORY = "./data/Selma/";
    private DocumentHandler dh = new DocumentHandler();

    public static void main(String[] args) throws IOException {
        SearchEngine se = new SearchEngine();
        Indexer indexer = new Indexer();
        DocumentHandler documentHandler = new DocumentHandler();

    }
}