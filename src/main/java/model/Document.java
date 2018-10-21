package model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Document {

    private String documentName;
    private File documentFile;
    private List<String> documentWords;
    private HashMap<String,Number> documentTermFrequencies;

    public Document(String documentName, File documentFileName, List<String> documentWords, HashMap<String, Number> documentTermFrequencies) {
        this.documentName = documentName;
        this.documentFile = documentFileName;
        this.documentWords = documentWords;
        this.documentTermFrequencies = documentTermFrequencies;
    }

    public Document(String documentName, File documentFile, List<String> documentWords) {
        this.documentName = documentName;
        this.documentFile = documentFile;
        this.documentWords = documentWords;
    }

    public Document(File documentFile, List<String> documentWords) {
        this.documentName = documentFile.getName();
        this.documentFile = documentFile;
        this.documentWords = documentWords;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public File getDocumentFile() {
        return documentFile;
    }

    public void setDocumentFile(File documentFile) {
        this.documentFile = documentFile;
    }

    public List<String> getDocumentWords() {
        return documentWords;
    }

    public void setDocumentWords(List<String> documentWords) {
        this.documentWords = documentWords;
    }

    public HashMap<String, Number> getDocumentTermFrequencies() {
        return documentTermFrequencies;
    }

    public void setDocumentTermFrequencies(HashMap<String, Number> documentTermFrequencies) {
        this.documentTermFrequencies = documentTermFrequencies;
    }

    public int getDocumentLength(){
        return documentWords.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(getDocumentName(), document.getDocumentName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDocumentName());
    }

    @Override
    public String toString() {
        return  String.format("%s [%d]",documentName, documentWords.size());

    }
}
