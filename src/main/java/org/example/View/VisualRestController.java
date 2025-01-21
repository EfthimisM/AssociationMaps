package org.example.View;

import org.example.Taxonomy.*;
import org.example.Taxonomy.Node;
import org.example.Taxonomy.Rule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VisualRestController {

    private static GraphDt graphDt;
    private static String path;
    private static double support = 0;
    private static double confidence;
    private static int phrase_length = 0;
    private static List<Rule> rules;

    private static Index index;

    private static Map<String, Node> nodes;

    private static int I;
    public static String readFileAsString(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
        return content;
    }

    public static void process(int i){
        rules = new ArrayList<>();
        index.setTHRESHOLD(support, phrase_length);
        Map<String, Word> subSets = index.Findsubsets(index.getTerms(), 2, i);
        if(subSets == null){
            System.err.println("TOO MANY SUBSETS, PLEASE TRY A HIGHER THRESHOLD");
            return;
        }
        for(Map.Entry<String, Word> entry : subSets.entrySet()){

            //System.out.println("Set : " + entry.getValue().getValues());
            List<List<String>> subsets = entry.getValue().getAllSubsets();
            for(List<String> list : subsets){
                double conf = index.getConfidence(list,entry.getValue().calculateSupport());

                List<String> opposite = new ArrayList<>();

                for(String string : entry.getValue().getValues()){
                    if(!list.contains(string)){
                        opposite.add(string);
                    }
                }


                if(conf >= confidence){
                    //System.out.println("RULE: " + list + " -> " + opposite + " = " + conf);
                    if(!opposite.isEmpty()){
                        Rule rule = new Rule(list,opposite,conf);
                        rules.add(rule);
                    }
                }
            }
            // Add a threshold to the maximum number of rules we can return 
            if(rules.size() > 10000){
                System.out.println("TOO MANY RULES");
                return;
            }
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
        for(Rule rule : rules){
            System.out.println(rule.getList() + "->" + rule.getOpposite() + "=" + rule.getConfidence());
            for(String A : rule.getList()) {
                Node tmp;
                if (!nodes.containsKey(A)) {
                    ArrayList<String> c = new ArrayList<>();
                    c.add(A);
                    tmp = new Node(A, c);
                    nodes.put(A, tmp);
                } else {
                    tmp = nodes.get(A);
                }
                for (String B : rule.getOpposite()) {
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
                    tmp.addChild(tmp2, rule.getConfidence());
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

        File file = new File(path);
        int i = 0 ;
        System.out.println("Tokenizing and Stemming: ");
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

        index.setTHRESHOLD(support, phrase_length);
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
            for(Map.Entry<Node, Double> node : entry.getValue().getchildren().entrySet()){
                Edge edge = new Edge(entry.getKey(), node.getKey().getValue(), node.getValue());
                graphDt.addEdge(edge);
            }
        }
        // todo: Put graphDT to the response
        System.out.println("DONE FOR: " + I);
        return ResponseEntity.ok(graphDt);

    }

}
