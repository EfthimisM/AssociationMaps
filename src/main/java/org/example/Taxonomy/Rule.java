package org.example.Taxonomy;


import java.util.List;

public class Rule {

    private List<String> list;
    private List<String> opposite;
    private double confidence;

    public Rule(List<String> l, List<String> op, double con){
        this.list = l;
        this.opposite = op;
        this.confidence = con;
    }

    public double getConfidence() {
        return confidence;
    }

    public List<String> getList() {
        return list;
    }

    public List<String> getOpposite() {
        return opposite;
    }
}
