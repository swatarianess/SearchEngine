package query;

import domain.DocumentHandler;
import model.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class ScoreModelQueryTest {

    DocumentHandler dh = new DocumentHandler();
    ScoreModelQuery vsmq = new ScoreModelQuery(dh);

    @BeforeEach
    void setUp() throws IOException {
        dh.addDocument("./data/Stories/");
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getDocumentScores() {
        ArrayList<String> queryString = new ArrayList<>(Arrays.asList("kings".split(" +")));
       HashMap<Document,Number> result = vsmq.getDocumentScores(dh.getProperDocumentList(),queryString);
        System.out.println("result = " + result);
    }

    @Test
    void generateDocumentScore() {

    }
}