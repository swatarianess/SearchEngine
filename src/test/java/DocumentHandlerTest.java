import model.Document;
import domain.DocumentHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Fast")
public class DocumentHandlerTest {

    private DocumentHandler dh = new DocumentHandler();

    @BeforeEach
    @DisplayName("Reinitialize documentHandler")
    void setUp() {
        System.out.println("\n@BeforeAll - Clearing loaded documents ");
        if (dh.getDocuments().size() > 0) {
            dh.getDocuments().clear();
        }
    }

    @Test
    @DisplayName("Parse file into a string array of words")
    void parseFileTest() throws IOException {
        dh.addDocument("example_", "./data/Random/");
        assertEquals(4, dh.getDocuments().size());
        System.out.println("dh.getDocumentList().values().toString() = " + dh.getDocuments().stream().map(Document::getDocumentWords).flatMap(Collection::stream).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Loading all files in a directory")
    void loadAllFilesTest() throws IOException {
        dh.addDocument("document_", "./data/Random/");
        int fFiles = dh.getDocuments().size();
        System.out.println("FileCount: " + fFiles);
        assertEquals(4, fFiles);
    }

    @Test
    void stringPortStemmingTest() {
        String input = "Terrifying";
        String output = dh.stemWord(input);
        String expected = "terrifi";
        System.out.println(String.format("In: %s \nOut: %s\nExpected: %s",input, output, expected));
        assertEquals(expected,output);
    }

    @Test
    void removeStopWordsTest() {
       String handled = dh.removeStopWords("The quick brown fox jumped over the lazy dog and it was stuff and things.");
        System.out.println("handled = " + handled);
    }

    @Test
    @DisplayName("Load all files in folder")
    void addFilesFromFolderTest() throws IOException {
        //4 files in Random
        long startTime = System.currentTimeMillis();
        dh.addDocument("./data/Random/");
        long endTime = System.currentTimeMillis();

        System.out.println("Directory: ./data/Random/");
        System.out.println("FileCount: " + dh.getDocuments().size());
        System.out.printf("TimeTaken: %dms\n", (endTime-startTime));
        System.out.println();

        //24 files in stories
        startTime = System.currentTimeMillis();
        dh.addDocument("./data/Stories/");
        endTime = System.currentTimeMillis();
        System.out.println("Directory: ./data/Stories/");
        System.out.println("Document Names: " + dh.getDocuments().stream().map(Document::getDocumentName).collect(Collectors.toSet()));
        System.out.println("FileCount: " + dh.getDocuments().size());

        System.out.printf("TimeTaken: %dms\n", (endTime-startTime));


        assertEquals(24, dh.getDocuments().size());
    }

    @Test
    @DisplayName("Document test (first loaded document)")
    void documentDetailsTest() throws IOException {
        dh.addDocument("./data/Stories/");
        System.out.println(dh.getDocuments().get(0).toString());
        System.out.println("dh.getDocuments().get(0).getDocumentWords() = " + dh.getDocuments().get(0).getDocumentWords());
    }
}
