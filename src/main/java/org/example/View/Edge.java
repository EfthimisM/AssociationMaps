package org.example.View;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Edge {

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("confidence")
    private double confidence;
    public Edge(String from, String to, double conf){
        this.to = to;
        this.from = from;
        this.confidence = conf;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}