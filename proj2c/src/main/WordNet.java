package main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {

    graph IDnodeMap;
    HashMap<String, List<String>> wordToID;

    public WordNet(String synsetFilename, String hyponymsFilename) {

        // create graph instances
        IDnodeMap = new graph();
        wordToID = new HashMap<>();

        // parsing happens

        // synsetFile = contains ID -> word(s)
        In wordFile = new In(synsetFilename);
        int i = 0;
        while (!wordFile.isEmpty()) {
            i += 1;
            String nextLine = wordFile.readLine();
            String[] splitLine = nextLine.split(",");
            String[] synset = splitLine[1].split(" ");

            List<String> synsetList = Arrays.asList(synset);
            IDnodeMap.addNode(splitLine[0], new ArrayList<>(), synsetList, new ArrayList<>());

            for (String word: synset) {
                if (wordToID.containsKey(word)) {
                    wordToID.get(word).add(splitLine[0]);
                } else {
                    wordToID.put(word, new LinkedList<>());
                    wordToID.get(word).add(splitLine[0]);
                }
            }
        }

        // hypoFile = contains ID -> hypoIDs
        In hypoFile = new In(hyponymsFilename);
        int j = 0;
        while (!hypoFile.isEmpty()) {
            j += 1;
            String nextLine2 = hypoFile.readLine();
            String[] splitLine2 = nextLine2.split(",");

            for (int k = 1; k < splitLine2.length; k++) {
                IDnodeMap.addToHypo(splitLine2[0], splitLine2[k]);
                IDnodeMap.addToParent(splitLine2[k], splitLine2[0]);
            }
        }
    }

    // method for getting relevantIDs for a word
    public List<String> getRelevantIDs(String word) {
        return wordToID.get(word);
    }

    public TreeSet<String> getAllWords(List<String> ids, boolean t) {
        return IDnodeMap.traversal(ids, t);
    }

}
