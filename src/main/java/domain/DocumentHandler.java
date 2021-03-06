package domain;

import indexer.Indexer;
import model.Document;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.tartarus.snowball.ext.EnglishStemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentHandler {

    private Indexer indexer = new Indexer();
    private List<Document> documents = new ArrayList<>();
    private List<String> wordsInCorpus = new ArrayList<>();
    private String defaultFolderDirectory = "./resources/data/Stories/";


    /**
     * Adds a single document file
     *
     * @param documentFile The file to add to the document list
     */
    public void addDocument(File documentFile) {
        addDocument("", documentFile);
        documents.forEach(document -> {
            indexer.generateInvertedIndex(Collections.singletonMap(document.getDocumentName(), document.getDocumentWords()));
            wordsInCorpus.addAll(document.getDocumentWords());
        });
    }

    /**
     * Adds a document and adds prefix to document name
     *
     * @param documentName The reference name to the document
     * @param documentFile The file of the document
     */
    public void addDocument(String documentName, File documentFile) {
        String documentPrefix = documentName.equalsIgnoreCase("") ? documentFile.getName() : documentName + (documents.size() + 1);
        try {
            Document tempDocument = new Document(documentPrefix, documentFile, parseFile(documentFile));
            HashMap<String, Number> tfHashMap = new HashMap<>();
            tempDocument.getDocumentWords().stream().distinct().forEach(s -> tfHashMap.put(s, indexer.termFrequency(tempDocument.getDocumentWords(), s)));
            tempDocument.setDocumentTermFrequencies(tfHashMap);
            documents.add(tempDocument);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds all documents from a folder directory
     *
     * @param folderDirectory The folder directory to find files
     * @throws IOException Thrown if an exception is found
     */
    public void addDocument(String folderDirectory) throws IOException {
        getFolderFiles(folderDirectory).forEach(this::addDocument);
    }

    /**
     * @param documentPrefix  The prefix to add to the document name
     * @param folderDirectory The folder directory to find files
     * @throws IOException Thrown if an exception is found
     */
    public void addDocument(String documentPrefix, String folderDirectory) throws IOException {
        getFolderFiles(folderDirectory).forEach(file -> addDocument(documentPrefix, file));
    }

    /**
     * Stems a string
     *
     * @param input The word to stem
     * @return Returns a stemmed word
     */
    public String stemWord(String input) {
        EnglishStemmer englishStemmer = new EnglishStemmer();
        String inputParsed = input.toLowerCase();
        englishStemmer.setCurrent(inputParsed);
        englishStemmer.stem();

        return englishStemmer.getCurrent();
    }

    /**
     * Removes string if it is a stop-word
     *
     * @param textFile The word to check against
     * @return Returns the string if it is not a stop-word, otherwise an empty string.
     */
    public String removeStopWords(String textFile) {
        EnglishAnalyzer sa = new EnglishAnalyzer();
        CharArraySet charArraySet = sa.getStopwordSet();
        String input = textFile.toLowerCase();
        return charArraySet.contains(input) ? "" : input;
    }

    /**
     * Removes a document form the document list
     *
     * @param documentName The name of the document to remove
     */
    public void removeDocument(String documentName) {
        documents.removeIf(document -> document.getDocumentName().equals(documentName));
    }

    /**
     * Extracts words from a document, and returns a list of words
     *
     * @param documentFile File to parse
     * @return Returns an @ArrayList of words from the file.
     * @throws FileNotFoundException Thrown when file not found
     */
    private List<String> parseFile(File documentFile) throws FileNotFoundException {
        List<String> result = new ArrayList<>();
        Scanner sc = new Scanner(documentFile);
        sc.useDelimiter(" +|\\s");

        while (sc.hasNext()) {
            String w = sc.next();
            if (!w.matches("\\s*")) {
                String partialResult = w.replaceAll("[^a-zA-Z]", "").toLowerCase();
                String stopWordResult = removeStopWords(partialResult);
                String stemWordResult;
                if (!stopWordResult.equals("")) {
                    stemWordResult = stemWord(stopWordResult);
                    if (!stemWordResult.equals(""))
                        result.add(stemWordResult);
                }
            }
        }
        return result;
    }

    /**
     * Gets all the files in a folder
     *
     * @param folderPath The path to read files from
     * @return Returns a list of files to read
     * @throws IOException Thrown if folderPath is incorrect
     */
    public List<File> getFolderFiles(String folderPath) throws IOException {
        List<File> result;
        String fileFolder = Paths.get(folderPath).toFile().exists() ? folderPath : defaultFolderDirectory;

        try (Stream<Path> paths = Files.walk(Paths.get(fileFolder))) {
            result = paths.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        }
        return result;
    }

    public Indexer getIndexer() {
        return indexer;
    }

    public List<Document> getDocuments() {
        return documents;
    }


    public String getDefaultFolderDirectory() {
        return defaultFolderDirectory;
    }

    public void setDefaultFolderDirectory(String defaultFolderDirectory) {
        this.defaultFolderDirectory = defaultFolderDirectory;
    }

    public List<String> getWordsInCorpus() {
        return wordsInCorpus;
    }

    public String[] parseQuery(String[] query) {
        List<String> result = new ArrayList<>();
        for (String s : query) {
            String temp1 = stemWord(s);
            String temp2 = removeStopWords(temp1);
            if (!temp2.equals("")) result.add(temp2);
        }
        return result.toArray(new String[]{});
    }

    public double computeMatchingScore(String[] query, Document d) {
        Map<String, Number> idfMap = new TreeMap<>();
        Map<String, Number> documentWeightMap = new TreeMap<>();
        Map<String, Number> queryWeightMap = new TreeMap<>();

        Collection<List<String>> collectionOfDocuments = documents.stream().map(Document::getDocumentWords).collect(Collectors.toList());
        List<String> disinctWordsInDocument = d.getDocumentWords().stream().distinct().collect(Collectors.toList());
        //Populate maps (idfs + weightsDocument + weightsQuery)
        for (String s : disinctWordsInDocument) { //Distinct words in document
            idfMap.put(s, indexer.idf(collectionOfDocuments, s));
            documentWeightMap.put(s, indexer.termWeight(indexer.termFrequency(d.getDocumentWords(), s), idfMap.get(s).doubleValue()));
            if (Arrays.asList(query).contains(s)) {
                queryWeightMap.put(s, indexer.termWeightDocument(indexer.termFrequency(Arrays.asList(query), s), documents.size(), (double) indexer.getInvertedIndex().get(s).size()));
            }
        }


        double numerator = 0d;
        double queryDenominator = 0d;
        double documentDenominator = 0d;

        double summationQueryDenominator = 0d;
        double summationDocumentDenominator = 0d;

        double denomenator = 0d;

        //Calculate Sum of Numerator
        for (Map.Entry<String, Number> queryTerm : queryWeightMap.entrySet()) {
            double documentWeightValue = documentWeightMap.get(queryTerm.getKey()).doubleValue();
            numerator += queryTerm.getValue().doubleValue() * documentWeightValue;
        }

        //Calculate Sum of Query weight sums in denominator
        for (String queryTerm : queryWeightMap.keySet()) {
            queryDenominator += Math.pow(queryWeightMap.get(queryTerm).doubleValue(), 2);
        }

        //Calculate Sum of Document weight sums in denominator
        for (String documentTerm : disinctWordsInDocument) {
            documentDenominator += Math.pow(documentWeightMap.get(documentTerm).doubleValue(), 2);
        }

        queryDenominator = Math.sqrt(queryDenominator);
        documentDenominator = Math.sqrt(documentDenominator);
        denomenator = queryDenominator * documentDenominator;

        System.out.println( d.getDocumentName() + " SCORE => " + (numerator / denomenator));
        return numerator / denomenator;

    }

    /**
     * Computes the vector of a document
     *
     * @param query    The query
     * @param document The document to calculate score for
     * @return Returns cosine computation for a
     */
    public double computeCosineVector(Collection<String> query, Document document) {
        //q_i = tf-idf of term i in query
        //d_i = tf-idf of term i in document
        //|d_i| & |q_i| are

        double productNumerator = 0d;
        double sumDenominatorQuery = 0d;
        double sumDenominatorDocument = 0d;


        for (String q_i : query) {
            double tfidfQuery = 0d, tfidfDocument = 0.00d;
            //Calculate Numerator Product
            tfidfQuery = indexer.tfIdf((List<String>) query, new ArrayList<String>(indexer.getInvertedIndex().keySet()), q_i); //Query
            tfidfDocument = indexer.tfIdf(document.getDocumentWords(), Collections.singleton(wordsInCorpus), q_i); //Document

            productNumerator += (tfidfDocument * tfidfQuery);
            sumDenominatorQuery += Math.pow(tfidfQuery, 2);
            sumDenominatorDocument += Math.pow(tfidfDocument, 2);
        }

        double resultDenominatorQuery = Math.sqrt(sumDenominatorQuery);
        double resultDenominatorDocument = Math.sqrt(sumDenominatorDocument);
        double result = productNumerator / (resultDenominatorDocument * resultDenominatorQuery);

        System.out.printf("%s: numerator: %s, Denominator: %s, result: %s \n", document, productNumerator, sumDenominatorDocument * sumDenominatorQuery, result);
        return result;
    }
}
