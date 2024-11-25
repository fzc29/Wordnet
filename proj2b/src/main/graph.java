package main;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class graph {

    // mapping each synset ID to a node that contain its synset words and children IDs
    HashMap<String, Node> IDtoNode;


    public class Node {

        List<String> hyponyms;
        List<String> words;

        // List<String> parents; for part C

        public Node(List<String> hypo, List<String> words) {
            // list of hyponym IDs
            this.hyponyms = hypo;
            // list of all words in the synset
            this.words = words;
        }
    }

    public graph() {
        IDtoNode = new HashMap<>();
    }

    // when initializing nodes in WordNet -> use new ArrayList for hypo
    public void addNode(String ID, List<String> hypo, List<String> words){
        IDtoNode.put(ID, new Node(hypo, words));
    }

    public void addToHypo(String ID, String hypo) {
        IDtoNode.get(ID).hyponyms.add(hypo);
    }

    public TreeSet<String> traversal(List<String> relevantIDs) {
        TreeSet<String> wantedHyponyms = new TreeSet<>();
        // take in a list of IDs for a word
        if (relevantIDs != null) {

            // each ID = synset -> make a boolean hashMap for all IDs, set to false initially
            HashMap<String, Boolean> visited = new HashMap<>();
            for (String id : IDtoNode.keySet()) {
                visited.put(id, false);
            }

            // cycle through all IDs -> get hyponyms
            for (String id : relevantIDs) {
                helper(wantedHyponyms, id, visited);
            }
        }
        return wantedHyponyms;
    }

    // use slides from Lecture 22 DFS implementation
    public void helper(TreeSet<String> hypoList, String startID, HashMap<String, Boolean> visited) {
        // where DFS happens
        // first add all words in current node
        hypoList.addAll(IDtoNode.get(startID).words);
        // keep track of visited nodes
        visited.put(startID, true);
        // get hypoIDs
        List<String> hypoId = IDtoNode.get(startID).hyponyms;
        if (!hypoId.isEmpty()) {
            for (String subID : hypoId) {
                if (!visited.get(subID)) {
                    helper(hypoList, subID, visited);
                }
            }
        }
    }

    // traverse through IDs, hypo IDS, and get all the words in this class

}
