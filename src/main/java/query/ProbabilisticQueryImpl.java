package query;

import domain.DocumentHandler;
import indexer.Indexer;
import model.Document;

import java.util.HashMap;

public class ProbabilisticQueryImpl implements QueryModel {
    DocumentHandler documentHandler;
    Indexer indexer = new Indexer();
    HashMap<Document,Number> documentRank = new HashMap<>();

    //Score(q,d) = Summation_(t_element of q && d)tf*idf_t,d

    public ProbabilisticQueryImpl(DocumentHandler dh) {
    this.documentHandler = dh;

    }

    public double getDocumentScore(Document document){

        return 0.0d;
    }

    public double calulateDocumentScore(Document document){
        return 0.0d;
    }




}
