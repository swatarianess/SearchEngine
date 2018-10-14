package domain;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.tartarus.snowball.ext.EnglishStemmer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentHandler {

    private HashMap<String, List<String>> documentList = new HashMap<>();
    private final String DEFAULT_FOLDER_DIRECTORY = "./resources/data/Random/";
    private String defaultFilePrefix = "Document_";

    public String getDefaultFilePrefix() {
        return defaultFilePrefix;
    }

    public void setDefaultFilePrefix(String defaultFilePrefix) {
        this.defaultFilePrefix = defaultFilePrefix;
    }

    public HashMap<String, List<String>> getDocumentList() {
        return documentList;
    }

    public void addDocument(String documentName, ArrayList<String> documentWordList) {
        documentList.put(documentName, documentWordList);
    }

    public void addDocument(String documentName, File documentFile) {
        String documentPrefix = documentName.equalsIgnoreCase("") ? documentFile.getName(): documentName + (documentList.size() + 1);

        try {
            documentList.put(documentPrefix, parseFile(documentFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addDocument(String documentPrefix, String folderDirectory) throws IOException {
        getFolderFiles(folderDirectory).forEach(f -> addDocument(documentPrefix, f));
    }

    public String stemWord(String input){
        EnglishStemmer englishStemmer = new EnglishStemmer();
        String inputParsed = input.toLowerCase();
        englishStemmer.setCurrent(inputParsed);
        englishStemmer.stem();

        return englishStemmer.getCurrent();
    }

    public static String removeStopWords(String textFile) {
        EnglishAnalyzer sa = new EnglishAnalyzer();
        CharArraySet charArraySet = sa.getStopwordSet();
        String input = textFile.toLowerCase();
        return charArraySet.contains(input) ? "" : input;
    }

    public void removeDocument(String documentName) {
        documentList.remove(documentName);
    }

    /**
     * @param documentFile File to parse
     * @return Returns an @ArrayList of words from the file.
     * @throws FileNotFoundException Thrown when file not found
     */
    private List<String> parseFile(File documentFile) throws FileNotFoundException {
        ArrayList<String> result = new ArrayList<>();
        Scanner sc = new Scanner(documentFile);
        sc.useDelimiter(" +|\\s");

        while (sc.hasNext()) {
            String w = sc.next();
            if (!w.matches("\\s*")) {
                String partialResult = w.replace("[^a-zA-Z] ", "").replace(".", "").toLowerCase();
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
     * @param folderPath The path to read files from
     * @return Returns a list of files to read
     * @throws IOException Thrown if folderPath is incorrect
     */
    public List<File> getFolderFiles(String folderPath) throws IOException {
        List<File> result;

        String fileFolder = Paths.get(folderPath).toFile().exists() ? folderPath : DEFAULT_FOLDER_DIRECTORY;

        try (Stream<Path> paths = Files.walk(Paths.get(fileFolder))) {
            result = paths.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        }
        return result;
    }

}
