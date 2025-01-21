package org.example.View;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphDt {
    @JsonProperty("nodes")
    private Map<String, Map<String, Integer>> nodes;
    @JsonProperty("edges")
    private List<Edge> edges;

    public GraphDt(){
        this.nodes = new HashMap<>();
        this.edges = new ArrayList<>();
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public Map<String, Map<String, Integer>> getNodes() {
        return nodes;
    }

    public void addNode(String id, String label, Integer level){
        Map<String, Integer> tmp = new HashMap<>();
        tmp.put(label, level);
        nodes.put(id, tmp);
    }
}