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

        List<String> parents;

        public Node(List<String> hypo, List<String> words, List<String> parent) {
            // list of hyponym IDs
            this.hyponyms = hypo;
            // list of all words in the synset
            this.words = words;
            this.parents = parent;
        }
    }

    public graph() {
        IDtoNode = new HashMap<>();
    }

    // when initializing nodes in WordNet -> use new ArrayList for hypo
    public void addNode(String ID, List<String> hypo, List<String> words, List<String> parent){
        IDtoNode.put(ID, new Node(hypo, words, parent));
    }

    public void addToHypo(String ID, String hypo) {
        IDtoNode.get(ID).hyponyms.add(hypo);
    }

    public void addToParent(String ID, String parent) {
        IDtoNode.get(ID).parents.add(parent);
    }


    public TreeSet<String> traversal(List<String> relevantIDs, boolean t) {
        TreeSet<String> wanted = new TreeSet<>();
        // take in a list of IDs for a word
        if (relevantIDs != null) {

            // each ID = synset -> make a boolean hashMap for all IDs, set to false initially
            HashMap<String, Boolean> visited = new HashMap<>();
            for (String id : IDtoNode.keySet()) {
                visited.put(id, false);
            }

            // cycle through all IDs -> get hyponyms
            for (String id : relevantIDs) {
                if (t) {
                    // hyponyms
                    helper(wanted, id, visited);
                } else {
                    // ancestors
                    helperAncestor(wanted, id, visited);
                }
            }
        }
        return wanted;
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

    public void helperAncestor(TreeSet<String> ancList, String startID, HashMap<String, Boolean> visited) {

        ancList.addAll(IDtoNode.get(startID).words);

        visited.put(startID, true);

        List<String> ancId = IDtoNode.get(startID).parents;
        if (!ancId.isEmpty()) {
            for (String subID : ancId) {
                if (!visited.get(subID)) {
                    helperAncestor(ancList, subID, visited);
                }
            }
        }
    }
    // traverse through IDs, hypo IDS, and get all the words in this class

}
