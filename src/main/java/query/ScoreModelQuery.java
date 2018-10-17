package query;

import domain.DocumentHandler;
import indexer.Indexer;
import model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreModelQuery implements QueryModel {
    DocumentHandler documentHandler;
    Indexer indexer = new Indexer();
    HashMap<Document,Number> documentRankMap = new HashMap<>();

    //Score(q,d) = Summation_(t_element of q && d)tf*idf_t,d

    public ScoreModelQuery(DocumentHandler dh) {
    this.documentHandler = dh;

    }

    public HashMap<Document,Number> getDocumentScores(ArrayList<Document> documents, List<String> terms){
        List<String> queryWords = new ArrayList<>();
        HashMap<Document,Number> result = new HashMap<>();

        //Stem query words
        terms.forEach((s -> queryWords.add(documentHandler.stemWord(s))));

        for (Document d : documents){
            for (String term : queryWords) {
               result.put(d,indexer.tdIdf(d.getDocumentWords(), documents.stream().map(Document::getDocumentWords).collect(Collectors.toList()), term));
            }
        }
        return result;
    }

}
