import domain.DocumentHandler;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("Fast")
public class DocumentHandlerTest {

    private String documentName = "Document_";
    private DocumentHandler dh = new DocumentHandler();

    @BeforeEach
    @DisplayName("Reinitialize documentHandler")
    void setUp() {
        //Initialize stuff
        System.out.println("\n@BeforeAll - Clearing loaded documents ");
        if (dh.getDocumentList().size() > 0) {
            dh.getDocumentList().clear();
        }
    }

    @Test
    @DisplayName("Parse file into a string array of words")
    void parseFileTest() throws IOException {
        dh.addDocument("example_", "./data/Random/");
        assertEquals(4, dh.getDocumentList().size()); //Parsed 1 file
        System.out.println("Stop words: " + EnglishAnalyzer.getDefaultStopSet());
        System.out.println("dh.getDocumentList().values().toString() = " + dh.getDocumentList().values().toString());
    }

    @Test
    @DisplayName("Loading all files in a directory")
    void loadAllFilesTest() throws IOException {
        dh.addDocument("document_", "./data/Random/");
        int fFiles = dh.getDocumentList().size();
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
       String handled = DocumentHandler.removeStopWords("The quick brown fox jumped over the lazy dog and it was stuff and things.");
        System.out.println("handled = " + handled);
    }

    @Test
    @DisplayName("Load all files in folder")
    void addFilesFromFolderTest() throws IOException {
        //4 files in Random
        dh.addDocument("", "./data/Random/");
        System.out.println("Directory: ./data/Random/");
        System.out.println("FileNames: " + dh.getDocumentList().keySet());
        System.out.println("FileCount: " + dh.getDocumentList().size());

        System.out.println();

        //18 files in stories
        dh.addDocument("", "./data/Stories/");
        System.out.println("Directory: ./data/Stories/");
        System.out.println("FileNames: " + dh.getDocumentList().keySet());
        System.out.println("FileCount: " + dh.getDocumentList().size());

        assertEquals(22, dh.getDocumentList().size());
    }
}
