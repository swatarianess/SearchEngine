package indexer;


import java.util.*;

/**
 * @author Stephen Adu
 * @since 26/09/2018
 */
public class Indexer {

    //Maybe use a pair for storing data?
    private HashMap<String, HashSet<String>> invertedIndex = new HashMap<>();

    public double tf(List<String> doc, String term) {
        double result = 0;

        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }

        return result / doc.size();
    }

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
//        addToInvertedIndex("Document_1", documentWordList.get("Document_1"));

        documentWordList.forEach(this::addToInvertedIndex);

        System.out.println("InvertedIndex size: " + invertedIndex.keySet().size());
        System.out.println("InvertedIndex: " + invertedIndex);
        System.out.println("InvertedIndex keys: " + invertedIndex.keySet().toString());
    }

    /**
     *  Takes words and adds them to the invertedIndex
     */
    private void addToInvertedIndex(String documentName, List<String> strings){
        for (String keyword : strings){
            if(invertedIndex.containsKey(keyword)){
                invertedIndex.get(keyword).add(documentName);
            }else {
                HashSet<String> documentList = new HashSet<>();
                documentList.add(documentName);
                invertedIndex.put(keyword, documentList);
            }
        }
    }

    public HashMap<String, HashSet<String>> getInvertedIndex() {
        return invertedIndex;
    }


    public static void main(String... args) {
        Indexer indexer = new Indexer();
        List<String> doc1 = Arrays.asList("Hello", "world", "I", "am", "a", "test", "of", "words", "and", "things", "and", "stuff");
        List<String> doc2 = Arrays.asList("Alteration", "literature", "to", "or", "an", "sympathize", "mr", "imprudence", "of", "is", "ferrars", "subject", "as", "enjoyed", "or", "tedious", "cottage", "Procuring", "as", "in", "resembled", "by", "in", "agreeable", "Next", "long", "no", "gave", "mr", "eyes", "Admiration", "advantages", "no", "he", "celebrated", "so", "pianoforte", "unreserved", "Not", "its", "herself", "forming", "charmed", "amiable", "Him", "why", "feebly", "expect", "future now");
        List<String> doc3 = Arrays.asList("Yet", "remarkably", "appearance", "get", "him", "his", "projection", "Diverted", "endeavor", "bed", "peculiar", "men", "the", "not", "desirous", "Acuteness", "abilities", "ask", "can", "offending", "furnished", "fulfilled", "sex");
        List<String> doc4 = Arrays.asList("Warrant", "fifteen", "exposed", "ye", "at", "mistake", "Blush", "since", "so", "in", "noisy", "still", "built", "up", "an", "again", "As", "young", "ye", "hopes", "no", "he", "place", "means", "Partiality", "diminution", "gay", "yet", "entreaties", "admiration", "In", "mr", "it", "he", "mention", "perhaps", "attempt", "pointed", "suppose", "Unknown", "ye", "chamber", "of", "warrant", "of", "norland", "arrived");

        List<List<String>> allDocs = Arrays.asList(doc1, doc2, doc3, doc4);

        System.out.println("tdif('I') : " + indexer.tdIdf(doc1, allDocs, "I"));
        System.out.println("tdif('and') : " + indexer.tdIdf(doc1, allDocs, "and"));
        System.out.println("tdif('no') : " + indexer.tdIdf(doc4, allDocs, "no"));

        System.out.println();

        System.out.println("InvertedIndex size: " + indexer.invertedIndex.size());

    }
}