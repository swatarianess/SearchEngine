package model;

import java.util.HashMap;
import java.util.HashSet;

public interface IRModel {
    String[] queryString = {};
    HashMap<String,HashSet<String>> invertedIndex = new HashMap<>();

}
