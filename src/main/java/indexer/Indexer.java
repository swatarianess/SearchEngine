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
    private Map<String, HashSet<String>> invertedIndex = new TreeMap<>();

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
//        return 1 + Math.log(result);
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

//        return count > 0 ? Math.log(docWordList.size() /  count) : 0;
        return Math.log(docWordList.size() / count);
    }

    public double tdIdf(List<String> doc, Collection<List<String>> docs, String term) {
        return (tf(doc, term) * idf(docs, term));
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

    /**
     * Method to calculate cosine similarity between two documents.
     *
     * @param queryVector1 : document vector 1 (a)
     * @param docVector2   : document vector 2 (b)
     * @return Returns cosine similarity score between two documents
     */
    public double cosineSimilarity(List<Double> queryVector1, List<Double> docVector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double cosineSimilarity;

        for (int i = 0; i < queryVector1.size(); i++) //docVector1 and docVector2 must be of same length
        {
            dotProduct += queryVector1.get(i) * docVector2.get(i);  //a.b
            magnitude1 += Math.pow(queryVector1.get(i), 2);  //(a^2)
            magnitude2 += Math.pow(docVector2.get(i), 2); //(b^2)
        }

        magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
        magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return cosineSimilarity;
    }

    public double documentCosineSimilarity(List<String> query, List<Document> relevantDocuments) {
        return 0d;
    }

    public double termWeight(Double termFrequency, Double documentFrequency, int totalDocuments) {
        return (1 + Math.log(termFrequency)) * Math.log(totalDocuments / documentFrequency);
    }

    public double termWeight(String term, Document d, int totalDocuments) {
//        return (1 + Math.log(d.getDocumentTermFrequencies().get(term).doubleValue()) * Math.log(totalDocuments/ (double) d.getDocumentTermFrequencies().get(term)));
        return (1 + Math.log(d.getDocumentTermFrequencies().get(term).doubleValue()) * Math.log((float) (totalDocuments / invertedIndex.get(term).size())));
    }

    public double scoreQueryDocument(List<String> query, Document d) {
        return 0.0d;
    }

}