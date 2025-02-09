package org.example.Taxonomy;

import mitos.stemmer.Stemmer;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
public class Index {

    private Map<String, Word> terms;
    private int phrase_length = 0;
    private double THRESHOLD = 0;
    private List<HashSet<String>> collections;
    private static HashSet<String> tokens;
    private static HashSet<String> vocabulary;
    static Stemmer stemmer;

    private static POSCheck posCheck;

    public static String tokenize(String word) {
        char[] punctuation = { ',', '?', '!', ';', ':', '\'', '\"', ')', ']', '}', '(', '[', '{', '<', '>', '/',
                '\\', '=', '+', '*', '~', '\t','|','^','_','#','$','%','&','\n'};

        if(word.endsWith(".")){
            String tmp = word.replace(Character.toString('.'), "");
            word = tokenize(tmp);
        }

        for (char p : punctuation) {
            if (word.indexOf(p) != -1) {
                String tmp = word.replace(Character.toString(p), "");
                word = tokenize(tmp);
            }
        }
        if(word.isEmpty()){
            return "A";
        }
        return word;
    }

    public double getAvgSupport(){

        int sum = 0;
        for(Map.Entry<String, Word> entry : terms.entrySet()){
            sum += entry.getValue().calculateSupport();
        }
        double result = (double)sum / terms.size();
        return result;

    }

    public void setTHRESHOLD(double th, int phrase_length){
        this.THRESHOLD = th;
        this.phrase_length = phrase_length;
    }

    public Index (String VocabPath){

        collections = new ArrayList<>();
        HashSet<String> stp = new HashSet<>();
        terms = new HashMap<>();
        File stopWords = new File("StopWords");
        stemmer.Initialize();
        this.posCheck = new POSCheck();

        try (BufferedReader br = new BufferedReader(new FileReader(stopWords))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+"); // split line by whitespace
                for (String word : lineWords) {
                    stp.add(tokenize(stemmer.Stem(word)));
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        tokens = stp;

        stp = new HashSet<>();
        // Read vocabulary file
        File vocabFile = new File(VocabPath);
        try (BufferedReader br = new BufferedReader(new FileReader(vocabFile))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split("\\s+");
                for (String word : lineWords) {
                    stp.add(tokenize(stemmer.Stem(word)));
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        vocabulary = stp;


    }

    public List<HashSet<String>> getCollections (){
        return collections;
    }
    public void addCollection(String[] newCollection, int id, int phrase_length){
        HashSet<String> tmp = new HashSet<>();
        this.phrase_length = phrase_length;
        if(phrase_length == 0 || phrase_length == 1){

            // check if we have a specific vocabulary
            if(vocabulary.isEmpty()){
                for(String word: newCollection){
                    String string = tokenize(stemmer.Stem(word));

                    // we can add the position check here if its needed eg.:
                    // posCheck.isAdjective(string) posCheck.isNoun(string) etc.
                    if(!string.endsWith("A") && !tokens.contains(string) && !posCheck.isNumber(string)){
                        String x = stemmer.Stem(string);
                        tmp.add(x);

                        if(!terms.containsKey(x)){
                            List<String> temp = new ArrayList<>();
                            temp.add(x);
                            Word term = new Word(temp);
                            term.addFrequency(id);
                            this.terms.put(x, term);
                        }else{
                            terms.get(x).addFrequency(id);
                        }


                    }
                }
            }else{
                for(String word: newCollection){
                    String string = tokenize(stemmer.Stem(word));

                    // we can add the position check here if its needed eg.:
                    // posCheck.isAdjective(string) posCheck.isNoun(string) etc.
                    if(!string.endsWith("A") && !tokens.contains(string) && vocabulary.contains(string)){
                        String x = stemmer.Stem(string);
                        tmp.add(x);

                        if(!terms.containsKey(x)){
                            List<String> temp = new ArrayList<>();
                            temp.add(x);
                            Word term = new Word(temp);
                            term.addFrequency(id);
                            this.terms.put(x, term);
                        }else{
                            terms.get(x).addFrequency(id);
                        }


                    }
                }
            }

            collections.add(tmp);
        }else{

            // check if we have a specific vocabulary
            if(vocabulary.isEmpty()){
                for(int i =0; i < newCollection.length; i ++){
                    String word = new String("");
                    int k = 0;
                    for(int j = 0; j < phrase_length ; j++){
                        if(j+i+k < newCollection.length) {
                            String string = tokenize(stemmer.Stem(newCollection[j + i + k]));

                            // we can add the position check here if its needed eg.:
                            // posCheck.isAdjective(string) posCheck.isNoun(string) etc.
                            if (!string.endsWith("A") && !tokens.contains(string) ) {
                                String x = stemmer.Stem(string);
                                word += x + " ";
                            } else {
                                k++;
                                j--;

                            }
                        }

                    }
                    tmp.add(word);
                    if(!terms.containsKey(word)){
                        List<String> temp = new ArrayList<>();
                        temp.add(word);
                        Word term = new Word(temp);
                        term.addFrequency(id);
                        this.terms.put(word, term);
                    }else{
                        terms.get(word).addFrequency(id);
                    }
                }
            }else{
                for(int i =0; i < newCollection.length; i ++){
                    String word = new String("");
                    int k = 0;
                    for(int j = 0; j < phrase_length ; j++){
                        if(j+i+k < newCollection.length) {
                            String string = tokenize(stemmer.Stem(newCollection[j + i + k]));

                            // we can add the position check here if its needed eg.:
                            // posCheck.isAdjective(string) posCheck.isNoun(string) etc.
                            if (!string.endsWith("A") && !tokens.contains(string) && vocabulary.contains(string) ) {
                                String x = stemmer.Stem(string);
                                word += x + " ";
                            } else {
                                k++;
                                j--;

                            }
                        }

                    }
                    tmp.add(word);
                    if(!terms.containsKey(word)){
                        List<String> temp = new ArrayList<>();
                        temp.add(word);
                        Word term = new Word(temp);
                        term.addFrequency(id);
                        this.terms.put(word, term);
                    }else{
                        terms.get(word).addFrequency(id);
                    }
                }
            }
            collections.add(tmp);
        }
    }

    public Map<String, Word> getTerms (){
        return terms;
    }

    // Function to calculate factorial
    public static BigInteger factorial(int num) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= num; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    // Function to calculate C(n, N)
    public static BigInteger binomialCoefficient(int n, int N) {
        BigInteger numerator = factorial(N);
        BigInteger denominator = factorial(n).multiply(factorial(N - n));
        return numerator.divide(denominator);
    }

    // dynamic code to implement n choose r
    public static List<List<String>> getCombinations(ArrayList<String> arr, int r) {
        List<List<String>> result = new ArrayList<>();
        List<String> combination = new ArrayList<>();
        generateCombinations(arr, r, 0, combination, result);
        return result;
    }

    private static void generateCombinations(ArrayList<String> arr, int r, int index, List<String> combination, List<List<String>> result) {
        // If the current combination is of the required length, add it to the result list
        if (combination.size() == r) {
            result.add(new ArrayList<>(combination));
            return;
        }

        // Iterate through the list elements starting from 'index'
        for (int i = index; i < arr.size(); i++) {
            combination.add(arr.get(i));
            generateCombinations(arr, r, i + 1, combination, result);
            combination.remove(combination.size() - 1);
        }
    }

    // run aprioris algo to find the subests with the minimum support
    public Map<String, Word> Findsubsets(Map<String, Word> Subset, int depth,int total){
        Map<String, Word> newSet = new HashMap<>();
        if(THRESHOLD < 1){
            THRESHOLD = THRESHOLD * total;
        }

        // Eliminate all the x with support < threshold only on the first step
        if(depth == 2){
            boolean flag = false;
            for(Map.Entry<String, Word> entry : Subset.entrySet()){
                entry.getValue().calculateSupport();
                if(entry.getValue().support >=THRESHOLD && entry.getKey()!=null){
                    newSet.put(entry.getKey(),entry.getValue());
                    flag = true;
                }
            }
            if(!flag){
                return Subset;
            }
        }else{
            newSet = Subset;
        }

        // Create new x with one more element from the set before
        ArrayList<String> tmp = new ArrayList<>();
        for(Map.Entry<String, Word> entry: newSet.entrySet()){
            for(String str : entry.getValue().getValues()){
                if(!tmp.contains(str)){
                    tmp.add(str);
                }
            }
        }
        BigInteger result = binomialCoefficient(depth, tmp.size());
        if(result.compareTo(BigInteger.valueOf((long) 3E6)) > 0){
            System.out.println("TOO MANY SUBSETS STOPPING THE ALGORITHM");
            try{
                FileWriter writer = new FileWriter("Metrics.txt", true);
                writer.write("ENDED PREEPMTIVLY \n");
                writer.write("MAX DEPTH :" + depth + "\n");
                writer.close();
            }catch (Exception e){

            }
            return Subset;
        }

        // time powerset
        System.out.print("Start timing powerset\n");
        long startTime = System.currentTimeMillis();
        List<List<String>> ret = getCombinations(tmp, depth);
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time for powerset: " + (long) (endTime - startTime)/ 1000);
        // Narrow down the x

        Map<String, Word> x = new HashMap<>();
        System.out.print("Start timming search\n");
        startTime = System.currentTimeMillis();
        for(List<String> list : ret){
            String key = "";
            for(String word : list){
                key += word;
            }

            int id = 0;
            for(HashSet<String> doc : collections) {

                boolean f = true;
                for(String word : list){
                    if(!doc.contains(word)){
                        f = false;
                        break;
                    }
                }

                if(f){
                    if(!x.containsKey(key)){
                        Word term = new Word(list);
                        term.addFrequency(id);
                        x.put(key, term);
                    }else{
                        x.get(key).addFrequency(id);
                    }
                }
                id ++;
            }
        }
        endTime = System.currentTimeMillis();
        System.out.println("Total execution time for search: " + (long) (endTime - startTime)/ 1000);


        // check if the new subsets satisfy the support threshold
        x.entrySet().removeIf(entry -> entry.getValue().getTotalFreq() < THRESHOLD);

        if(x.isEmpty()){
            try{
                FileWriter writer = new FileWriter("Metrics.txt", true);
                writer.write("MAX DEPTH :" + depth + "\n");
                writer.close();
            }catch (Exception e){

            }
            System.out.println("MAX DEPTH :" + depth);
            return Subset;

        }else{
            x = Findsubsets(x, depth + 1, total);
        }

        return x;
    }

    public int getSuppoort(List<String> list){
        int support = 0;

        for(HashSet<String> doc : collections){

            boolean flag = true;
            for(String term : list){
                if(!doc.contains(term)){
                    flag = false;
                    break;
                }
            }
            if(flag){
                support += 1;
            }

        }

        return support;
    }
    public double getConfidence(List<String> list, int sup){
        double confidence = 0;

        confidence = (double)  sup / getSuppoort(list) ;
        return confidence;
    }

}