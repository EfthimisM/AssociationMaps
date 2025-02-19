package org.example.View;

import org.example.Taxonomy.*;
import org.example.Taxonomy.Node;
import org.example.Taxonomy.Rule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@RestController
public class VisualRestController {

    private static GraphDt graphDt;
    private static String path;
    private static double support = 0;
    private static double confidence;
    private static int phrase_length = 0;
    private static HashMap<String,Rule> rules;

    private static Index index;

    private static Map<String, Node> nodes;

    private static int I;
    public static String readFileAsString(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        return content;
    }


    // todo: remove duplicates
    public static void process(int i){
        rules = new HashMap<>();

        try{
            FileWriter fileWriter = new FileWriter("Metrics.txt", true);
            long startTime = System.currentTimeMillis();

            fileWriter.write("- START APRIORI ALGORITHM\n");
            index.setTHRESHOLD(support, phrase_length);
            // try with top scored terms
            //Map<String, Word> subSets = index.Findsubsets(index.getTopTerms(), 2, i);
            Map<String, Word> subSets = index.Findsubsets(index.getTerms(), 2, i);
            long endTime = System.currentTimeMillis();
            fileWriter.write("- END APRIORI ALGORITHM\n");
            fileWriter.write("\t - TOTAL NUMBER OF SUBSETS: " + subSets.size() + "\n");
            fileWriter.write("\t - TIME: " + (endTime - startTime) + "\n");
            fileWriter.write("- START RULE EXTRACTION\n");

            startTime = System.currentTimeMillis();

            if(subSets == null){
                System.err.println("TOO MANY SUBSETS, PLEASE TRY A HIGHER THRESHOLD");
                return;
            }
            for(Map.Entry<String, Word> entry : subSets.entrySet()){

                System.out.println("_______________");
                System.out.println(entry.getKey());

                System.out.println("_______________");

                List<String> subset = entry.getValue().getValues();
                for(int k = 0; k< subset.size(); k++){

                    List<String> first = new ArrayList<>();
                    first.add(subset.get(k));
                    int support1 = index.getSuppoort(first);

                    for(int l = 0; l< subset.size(); l++){
                        List<String> second = new ArrayList<>();
                        if(l!=k){

                            second.add(subset.get(l));
                            second.add(subset.get(k));
                            int support2 = index.getSuppoort(second);
                            double conf =  ((double)support2 / (double)support1);
                            if(conf >= confidence){
                                second.remove(second.size()-1);
                                try{
                                    String key = first.get(0) + second.get(0);
                                    Rule rule = new Rule(first,second,conf,support2);
                                    rules.put(key, rule);
                                    System.out.println("New rule: " + first + " -> " + subset.get(l) + " CONFIDENCE: " + conf);
                                }catch(Exception e){}
                            }
                        }
                    }
                }


                // Add a threshold to the maximum number of rules we can return
                if(rules.size() > 10000){
                    System.out.println("TOO MANY RULES");
                    return;
                }
            }
            endTime = System.currentTimeMillis();
            fileWriter.write("- END RULE EXTRACTION\n");
            fileWriter.write("\t - TIME: " + (long) (endTime - startTime) + "\n");
            fileWriter.write("\t - TOTAL RULES:" + rules.size() + "\n");
            fileWriter.close();

        }catch(Exception e){
            e.printStackTrace();
        }


    }


    private static void setLevels(){

        int max = 0;
        for(Node node : nodes.values()){
            node.setLevel(10,5);
            if(max < node.getLevel()){
                max = node.getLevel();
            }
        }

        if(max == 0){
            return;
        }
        // todo: normalize the values
        List<Integer> uniqueLevels = new ArrayList<>();
        int size = nodes.size();
        for(Node node : nodes.values()){
            int level_norm = node.getLevel() * (size+1) / max;

            if(!uniqueLevels.contains(level_norm)){
                uniqueLevels.add(level_norm);
            }

            node.setLevel(level_norm);
        }

        uniqueLevels.sort(null);
        List<Integer> newLevels = new ArrayList<>();
        for(int i =0; i< uniqueLevels.size(); i++){
            newLevels.add(i+1);
        }
        // todo: ditribute correctly
        for(Node node: nodes.values()){
            int level2 = uniqueLevels.indexOf(node.getLevel());
            node.setLevel(level2);
        }


    }
    private static void initFrame(){
        nodes = new HashMap<>();
        for(Map.Entry<String, Rule>entry : rules.entrySet()){
            System.out.println(entry.getValue().getList() + "->" + entry.getValue().getOpposite() + "=" + entry.getValue().getConfidence());
            for(String A : entry.getValue().getList()) {
                Node tmp;
                if (!nodes.containsKey(A)) {
                    ArrayList<String> c = new ArrayList<>();
                    c.add(A);
                    tmp = new Node(A, c);
                    nodes.put(A, tmp);
                } else {
                    tmp = nodes.get(A);
                }
                for (String B : entry.getValue().getOpposite()) {
                    Node tmp2;
                    if (!nodes.containsKey(B)) {
                        ArrayList<String> c = new ArrayList<>();
                        c.add(B);
                        tmp2 = new Node(B, c);
                        nodes.put(B, tmp2);
                    } else {
                        tmp2 = nodes.get(B);
                    }
                    tmp2.addParent(tmp);
                    tmp.addChild(tmp2, entry.getValue().getConfidence(), entry.getValue().getSupport());
                }
            }

            setLevels();
        }
    }

    @PostMapping("/api/submit-values")
    public ResponseEntity<GraphDt> EnterValues(@RequestBody ValueRequest request){
        support = request.getSupport();
        confidence = request.getConfidence();
        phrase_length = request.getPhrase_length();
        path = request.getPath();
        index = new Index("null");

        System.out.println("Support: " + support);
        System.out.println("Confidence: " + confidence);
        System.out.println("Phrase Length: " + phrase_length);

        // create a file to put all the time metrics
        try {
            FileWriter output = new FileWriter("Metrics.txt");
            long startTime = System.currentTimeMillis();
            File file = new File(path);
            int i = 0 ;
            System.out.println("Tokenizing and Stemming: ");
            long tokenize = System.currentTimeMillis();
            output.write("TIMING INPUT & INDEXING & TOKENIZATION & STEMMING\n");
            for (File fileEntry : file.listFiles()) {
                if (fileEntry.isDirectory()) {
                    System.out.println(fileEntry.getAbsolutePath());
                } else {
                    try {
                        String content = readFileAsString(fileEntry.getAbsolutePath());
                        String[] tokens = content.split("\\s+");
                        // phrase length determins the number of words that make a term
                        index.addCollection(tokens, i, phrase_length);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    i++;
                }
            }

            I = i;
            System.out.println("DONE FOR: " + I);
            long stopTime = System.currentTimeMillis();
            output.write("- INPUT DONE FOR: " + I + " DOCUMENTS \n");
            output.write(" TOTAL TIME: " +(long) (stopTime - startTime) + " SECONDS \n");

            index.calculateTf_ID();
            index.setTHRESHOLD(support, phrase_length);
            output.write(" FOUND " + index.getTerms().size() + " UNIQUE TERMS\n START APRIORI ALGORITHM\n ");
            output.close();

            process(I);
            initFrame();

            graphDt = new GraphDt();

            for(Map.Entry<String, Node> entry : nodes.entrySet()){
                String label = "";
                for(String str : entry.getValue().getTerms()){
                    if(entry.getValue().getTerms().indexOf(str) == entry.getValue().getTerms().size() - 1){
                        label += str;
                    }else{
                        label += str + ", ";
                    }
                }
                graphDt.addNode(entry.getKey(), label, entry.getValue().getLevel());
            }

            for(Map.Entry<String, Node> entry : nodes.entrySet()){
                for(Map.Entry<Node, List<Double>> node : entry.getValue().getchildren().entrySet()){
                    Edge edge = new Edge(entry.getKey(), node.getKey().getValue(), node.getValue().get(0), node.getValue().get(1));
                    graphDt.addEdge(edge);
                }
            }

            System.out.println("DONE FOR: " + I);
            FileWriter writer = new FileWriter("Metrics.txt", true);
            long endTime = System.currentTimeMillis();
            writer.write("Total time to execute the program: " + (long) (endTime - startTime)/ 1000);
            writer.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(graphDt);

    }

}
