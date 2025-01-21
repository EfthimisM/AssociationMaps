package org.example.View;

public class ValueRequest {

    private double support;
    private double confidence;
    private int phrase_length;

    private String path;

    public void setPath(String string){ this.path = string;}

    public String getPath() { return path; }

    public void setPhrase_length(int phrase_length) {
        this.phrase_length = phrase_length;
    }

    public int getPhrase_length() {
        return phrase_length;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getSupport() {
        return support;
    }

    public void setSupport(double support) {
        this.support = support;
    }

}