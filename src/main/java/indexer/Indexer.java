package indexer;


import com.sun.istack.internal.NotNull;
import model.Document;

import java.util.*;

/**
 * @author Stephen Adu
 * @since 26/09/2018
 */
public class Indexer {

    //Maybe use a pair for storing data?
    private static Map<String, HashSet<String>> invertedIndex = new TreeMap<>();
    private static Map<String, Number> idfMap = new TreeMap<>();

    /**
     * Number of occurrences of a term in a document
     *
     * @param doc  A list of words in a document
     * @param term The term to look for in the list of words
     * @return Returns the term frequency for the word
     */
    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result;
//        return result / doc.size();
    }

    /**
     * @param docWordList The entire word list from every document
     * @param term        The term to look for in the collection of words from every document
     * @return Returns the inverse document frequency
     */
    public double idf(Collection<List<String>> docWordList, String term) {
        double count = 0;
        for (List<String> doc : docWordList) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    count++;
                    break;
                }
            }
        }
        System.out.println("Document count: " + count);
        return count > 0 ? (Math.log(docWordList.size() / count)) : 1;
    }

    public double tfIdf(List<String> doc, Collection<List<String>> wordsInCorpus, String term) {
        return (tf(doc, term) * idf(wordsInCorpus, term));
    }

    public void generateInvertedIndex(Map<String, List<String>> documentWordList) {
        for (Map.Entry<String, List<String>> entry : documentWordList.entrySet()) {
            String key = entry.getKey();
            HashSet<String> value = new HashSet<>(entry.getValue());
            addToInvertedIndex(key, value);
        }
    }


    /**
     * Takes words and adds them to the invertedIndex
     */
    private void addToInvertedIndex(String documentName, @NotNull HashSet<String> strings) {
        for (String keyword : strings) {
            if (invertedIndex.containsKey(keyword)) {
                invertedIndex.get(keyword).add(documentName);
            } else {
                HashSet<String> documentList = new HashSet<>();
                documentList.add(documentName);
                invertedIndex.put(keyword, documentList);
            }
        }
    }

    public Map<String, HashSet<String>> getInvertedIndex() {
        return invertedIndex;
    }

    public double termWeight(Double termFrequency, Double documentFrequency, int totalDocuments) {
        return (1 + Math.log(termFrequency)) * Math.log(totalDocuments / documentFrequency);
    }

    public double termWeight(String term, Document d, int totalDocuments) {
        return (1 + Math.log(d.getDocumentTermFrequencies().get(term).doubleValue()) * Math.log((float) (totalDocuments / invertedIndex.get(term).size())));
    }

    public double scoreQueryDocument(List<String> query, Document d) {




        return 0d;
    }

}