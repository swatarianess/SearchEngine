package indexer;


import java.util.*;

/**
 * @author Stephen Adu
 * @since 26/09/2018
 */
public class Indexer {

    //Maybe use a pair for storing data?
    private HashMap<String, List<String>> invertedIndex = new HashMap<>();

    /**
     * @param doc A list of words in a document
     * @param term The term to look for in the list of words
     * @return Returns the term frequency for the word
     */
    public double tf(List<String> doc, String term) {
        double result = 0;

        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }

        return result / doc.size();
    }

    /**
     * @param docWordList The entire word list from every document
     * @param term The term to look for in the collection of words from every document
     * @return Returns the inverse document frequency
     */
    public double idf(Collection<List<String>> docWordList, String term) {
        double n = 0;
        for (List<String> doc : docWordList) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docWordList.size() / n);
    }

    public double tdIdf(List<String> doc, Collection<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);
    }

    public void generateInvertedIndex(HashMap<String,List<String>> documentWordList){
        documentWordList.forEach(this::addToInvertedIndex);
    }

    /**
     *  Takes words and adds them to the invertedIndex
     */
    private void addToInvertedIndex(String documentName, List<String> strings){
        for (String keyword : strings){
            if(invertedIndex.containsKey(keyword)){
                invertedIndex.get(keyword).add(documentName);
            }else {
                List<String> documentList = new ArrayList<>();
                documentList.add(documentName);
                invertedIndex.put(keyword, documentList);
            }
        }
    }

    public HashMap<String, List<String>> getInvertedIndex() {
        return invertedIndex;
    }

}