package main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class SynsetGraph {

    TreeMap<Integer, IDnode> IDmap;
    TreeMap<String, List<Integer>> wordMap;

    public SynsetGraph(String synsetFilename, String hyponymsFilename) {
        IDmap = new TreeMap<>();
        wordMap = new TreeMap<>();

        In wordFile = new In(synsetFilename);
        int i = 0;
        while (!wordFile.isEmpty()) {
            i += 1;
            String nextLine = wordFile.readLine();
            String[] splitLine = nextLine.split(",");
            String[] synset = splitLine[1].split(" ");

            IDmap.put(Integer.valueOf(splitLine[0]), new IDnode(Integer.parseInt(splitLine[0]), synset));

            for (String word: synset) {
                if (wordMap.containsKey(word)) {
                    wordMap.get(word).add(Integer.parseInt(splitLine[0]));
                } else {
                    wordMap.put(word, new LinkedList<>());
                    wordMap.get(word).add(Integer.parseInt(splitLine[0]));
                }
            }
        }

        In hypoFile = new In(hyponymsFilename);
        int j = 0;
        while (!hypoFile.isEmpty()) {
            j += 1;
            String nextLine2 = hypoFile.readLine();
            String[] splitLine2 = nextLine2.split(",");

            for (int k = 1; k < splitLine2.length; k++) {
                IDmap.get(Integer.parseInt(splitLine2[0])).addHypo(Integer.parseInt(splitLine2[k]));
            }
        }
    }


    public List<Integer> getIDforWord(String word){
        if (wordMap.containsKey(word)) {
            return wordMap.get(word);
        } else {
            return new LinkedList<>();
        }
    }

    public Set<Integer> getallHypo(int startID, Set<Integer> lst) {
        IDnode n = IDmap.get(startID);
        if (n.hypoIDs.isEmpty()) {
            lst.add(startID);
            return lst;
        } else {
            lst.add(startID);
            for (int i : n.hypoIDs) {
                if (!lst.contains(i)) {
                    lst.addAll(getallHypo(i, lst));
                }
            }
            return lst;
        }
    }

    public Set<Integer> getallHypo2(int startID, Set<Integer> lst, HashMap<Integer, Boolean> visited) {
        if (!visited.get(startID)) {
            visited.put(startID, true);
            lst.add(startID);
            if (!IDmap.get(startID).hypoIDs.isEmpty()) {
                for (int i : IDmap.get(startID).hypoIDs) {
                    if (!visited.get(i)) {
                        Set<Integer> subIds = getallHypo2(i, lst, visited);
                        lst.add(i);
                        lst.addAll(subIds);
                    }
                }
            }
        }
        return lst;
    }


    public Set<String> getAllWords(Set<Integer> allIDs) {
        Set<String> Words = new TreeSet<>();
        for (int i : allIDs) {
            IDnode n = IDmap.get(i);
            Words.addAll(n.getWordsforID());
        }
        return Words;
    }


}
