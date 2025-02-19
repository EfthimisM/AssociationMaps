package org.example.Taxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word {

    int support;
    private int totalFreq;
    private List<String> values;
    private Map<Integer, Integer> frequencies;

    private double tf_idf;


    public Word(List<String> vls){
        this.totalFreq = 0;
        frequencies = new HashMap<>();
        this.values = vls;
    }

    public void addFrequency(int docId){
        if(frequencies.get(docId) == null){
            frequencies.put(docId, 1);
        }else{
            int tmp = frequencies.get(docId) + 1;
            frequencies.put(docId, tmp);
        }
        totalFreq += 1;
    }

    public int calculateSupport(){
        this.support = frequencies.size();
        return frequencies.size();
    }

    public int getTotalFreq() {
        return totalFreq;
    }

    public void setValues(List<String> list){
        this.values = list;
    }

    public List<String> getValues(){
        return values;
    }

    public Map<Integer, Integer> getFrequencies(){
        return frequencies;
    }

    public static List<List<String>> getAllSubsets(List<String> set) {
        List<List<String>> result = new ArrayList<>();
        generateSubsets(set, 0, new ArrayList<>(), result);
        return result;
    }

    private static void generateSubsets(List<String> set, int index, List<String> current, List<List<String>> result) {
        if (index == set.size()) {
            if (!current.isEmpty()) { // To exclude the empty set
                result.add(new ArrayList<>(current));
            }
            return;
        }

        // Include the current element
        current.add(set.get(index));
        generateSubsets(set, index + 1, current, result);

        // Exclude the current element (backtrack)
        current.remove(current.size() - 1);
        generateSubsets(set, index + 1, current, result);
    }
    public List<List<String>> getAllSubsets(){
        return getAllSubsets(values);
    }

    public double getTf_idf() {
        return tf_idf;
    }

    public void setTf_idf(double tf_idf) {
        this.tf_idf = tf_idf;
    }
}
