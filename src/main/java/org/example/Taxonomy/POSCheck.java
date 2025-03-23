package org.example.Taxonomy;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;

// position Check
public class POSCheck {

    private StanfordCoreNLP pipeline;

    public POSCheck() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos");
        this.pipeline = new StanfordCoreNLP(props);
    }

    // Method to get the POS tag of a word
    public String getPOSTag(String word) {

        Annotation document = new Annotation(word);
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                return token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            }
        }
        return null;
    }

    // Method to check if a word is a noun
    public boolean isNoun(String word) {
        String posTag = getPOSTag(word);
        return posTag != null && (posTag.startsWith("NN"));
    }

    // Method to check if a word is a verb

    public boolean isVerb(String word) {
        String posTag = getPOSTag(word);
        return posTag != null && (posTag.startsWith("VB"));
    }

    // Method to check if a word is an adjective
    public boolean isAdjective(String word) {
        String posTag = getPOSTag(word);
        return posTag != null && (posTag.startsWith("JJ"));
    }

    // Method to check if a word is a number
    public boolean isNumber(String word) {
        try{
            Double.parseDouble(word);
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
