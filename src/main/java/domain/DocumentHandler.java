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
    private String defaultFolderDirectory = "./resources/data/Stories/";


    /**
     * Adds a single document file
     *
     * @param documentFile The file to add to the document list
     */
    public void addDocument(File documentFile) {
        addDocument("", documentFile);
        documents.forEach(document -> indexer.generateInvertedIndex(Collections.singletonMap(document.getDocumentName(),document.getDocumentWords())));
    }

    /**
     * Adds a document and adds prefix to document name
     *
     * @param documentName The reference name to the document
     * @param documentFile The file of the document
     */
    public void addDocument(String documentName, File documentFile) {
        String documentPrefix = documentName.equalsIgnoreCase("") ? documentFile.getName(): documentName + (documents.size() + 1);
        try {
            Document tempDocument = new Document(documentPrefix, documentFile, parseFile(documentFile));
            HashMap<String,Number> tfHashMap = new HashMap<>();
            tempDocument.getDocumentWords().stream().distinct().forEach(s -> tfHashMap.put(s,indexer.tf(tempDocument.getDocumentWords(),s)));
            tempDocument.setDocumentTermFrequencies(tfHashMap);
            documents.add(tempDocument);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Adds all documents from a folder directory
     * @param folderDirectory The folder directory to find files
     * @throws IOException Thrown if an exception is found
     */
    public void addDocument(String folderDirectory) throws IOException {
        getFolderFiles(folderDirectory).forEach(this::addDocument);
    }

    /**
     * @param documentPrefix The prefix to add to the document name
     * @param folderDirectory The folder directory to find files
     * @throws IOException Thrown if an exception is found
     */
    public void addDocument(String documentPrefix, String folderDirectory) throws IOException {
        getFolderFiles(folderDirectory).forEach(file -> addDocument(documentPrefix,file));
    }

    /**
     * Stems a string
     *
     * @param input The word to stem
     * @return Returns a stemmed word
     */
    public String stemWord(String input){
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
                if (!stopWordResult.equals("")){
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

}
