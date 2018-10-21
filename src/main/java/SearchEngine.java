import domain.DocumentHandler;
import domain.FileHandler;

import java.io.IOException;

public class SearchEngine {

    private String DOCUMENT_DIRECTORY = "./data/Stories/";

    public static void main(String[] args) throws IOException {
        SearchEngine se = new SearchEngine();
        DocumentHandler documentHandler = new DocumentHandler();
        FileHandler fileHandler = new FileHandler();



        if (args.length > 0) System.out.println("We don't accept args yet...");

        documentHandler.addDocument(se.DOCUMENT_DIRECTORY);
        System.out.println("Documents: " + documentHandler.getDocuments());
    }
}