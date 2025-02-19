package org.example.Taxonomy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Node {

    private int level;
    private String value;
    private Map<Node, List<Double>> children;

    private List<Node> parents;

    private List<String> terms;

    public Node(String value, List<String> l){
        this.value = value;
        this.terms = l;
        this.level = 1;
        children = new HashMap<>();
        parents = new ArrayList<>();
    }

    public void addChild(Node node, Double confidence, Double support){
        List<Double> metrics = new ArrayList<>();
        metrics.add(confidence);
        metrics.add(support);
        children.put(node, metrics);
    }

    public void addParent(Node node){
        parents.add(node);
    }

    public List<Node> getParents(){
        return parents;
    }

    public String getValue() {
        return value;
    }

    public List<String> getTerms() {
        return terms;
    }

    public Map<Node, List<Double>> getchildren() {
        return children;
    }

    public int getLevel() {return level;}

    public void setLevel(double Alpha, double Beta) {
        int inDegree = this.parents.size();
        int outDegree = this.children.size();
        int tmp = (int) Math.round(Alpha * inDegree - Beta * outDegree);
        this.level = tmp;
    }

    public void setLevel(int level) { this.level = level; }
}
