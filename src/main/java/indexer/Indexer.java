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
    public double logTermFrequency(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return Math.log10(result / doc.size());
    }

    /**
     * Number of occurrences of a term in a document
     *
     * @param doc  A list of words in a document
     * @param term The term to look for in the list of words
     * @return Returns the term frequency for the word
     */
    public double termFrequency(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }

    public double tfCount(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result;
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
        return count > 0 ? (Math.log10(docWordList.size() / count)) : 0;
    }

    /**
     * @param docWordList The entire word list from every document
     * @param term        The term to look for in the collection of words from every document
     * @return Returns the inverse document frequency
     */
    public double idf(List<String> docWordList, int corpusSize, String term) {
        double count = 0;
        if (docWordList.stream().anyMatch(term::equalsIgnoreCase)) {
            count++;
        }
        return count > 0 ? (Math.log10(corpusSize / count)) : 0;
    }

    public double tfIdf(List<String> doc, Collection<List<String>> wordsInCorpus, String term) {
        return (logTermFrequency(doc, term) * idf(wordsInCorpus, term));
    }

    public double tfIdf(List<String> doc, List<String> wordsInCorpus, String term) {
        return (logTermFrequency(doc, term) * idf(Collections.singleton(wordsInCorpus), term));
    }

    public double tfIdfQuery(List<String> wordList, List<String> wordsInCorpus, int corpusSize , String term) {
        return (logTermFrequency(wordList, term) * idf(wordsInCorpus, corpusSize, term));
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

    public double termWeightDocument(Double termFrequency, Double documentFrequency, int totalDocuments) {
        return (1 + Math.log10(termFrequency)) * Math.log10(totalDocuments / documentFrequency);
    }

    public double termWeightDocument(String term, Document d, int totalDocuments) {
        if (d.getDocumentTermFrequencies().get(term) != null) {
            return (1 + Math.log10(d.getDocumentTermFrequencies().get(term).doubleValue()) * Math.log10((double) totalDocuments / invertedIndex.get(term).size()));
        } else {
            return 0;
        }
    }

    public double scoreQueryDocument(List<String> query, Document d) {
        double score = 0d;
            for (String s : query){
                if (d.getDocumentWords().contains(s)){
                    score += logTermFrequency(d.getDocumentWords(),s) * invertedIndex.get(s).size();
                }
            }
        return score;
    }

}