package main;

import edu.princeton.cs.algs4.In;


import java.util.*;

public class HypoGraph {

    IDMapping childIDMap = new IDMapping();

    TreeMap<Integer, ArrayList<String>> IdToWord = new TreeMap<>();
    // ID -> words in the synset

    TreeMap<String, ArrayList<Integer>> wordToID = new TreeMap<>();
    // word -> to all its IDs

    public HypoGraph(String synsetFilename, String hyponymsFilename) {

        // map each ID to a synset > which can contain more than one word
        In wordFile = new In(synsetFilename);
        int i = 0;
        while (!wordFile.isEmpty()) {
            i += 1;
            String nextLine = wordFile.readLine();
            String[] split = nextLine.split(",");

            // initiating ArrayList for each ID
            IdToWord.put(Integer.valueOf(split[0]), new ArrayList<>());

            // getting each individual word for synset
            String[] synset = split[1].split(" ");
            for (String item : synset) {
                // mapping each ID to all of its synset words
                IdToWord.get(Integer.valueOf(split[0])).add(item);

                // map each word to all of its IDs
                if (!wordToID.containsKey(item)) {
                    wordToID.put(item, new ArrayList<>());
                    wordToID.get(item).add(Integer.valueOf(split[0]));
                } else {
                    wordToID.get(item).add(Integer.valueOf(split[0]));
                }
            }
        }

        // map each ID to its hyponym's ID
        In countFile = new In(hyponymsFilename);
        int j = 0;
        while (!countFile.isEmpty()) {
            j += 1;
            String nextL = countFile.readLine();
            String[] splitL = nextL.split(",");

            // if first instance > create new hyponym list
            if (!childIDMap.containsKey(Integer.valueOf(splitL[0]))) {
                childIDMap.put(Integer.valueOf(splitL[0]), new ArrayList<>());
                for (int k = 1; k < splitL.length; k++) {
                    childIDMap.get(Integer.valueOf(splitL[0])).add(Integer.valueOf(splitL[k]));

                }
            } else {
                // adding to existing hyponym list
                for (int k = 1; k < splitL.length; k++) {
                    childIDMap.get(Integer.valueOf(splitL[0])).add(Integer.valueOf(splitL[k]));
                }
            }

        }

    }

    public ArrayList<Integer> getMainID(String word) {
        return wordToID.get(word);
    }


    public Set<String> getHyponyms(String word) {
        ArrayList<Integer> IDs = getMainID(word);

        Set<Integer> allIDs = new TreeSet<Integer>();
        if (IDs != null) {
            for (int i : IDs) {
                Set<Integer> currentID = childIDMap.getAllHypoIDs(i, new TreeSet<Integer>());
                allIDs.addAll(currentID);
            }

            Set<String> allHypo = new TreeSet<String>();
            for (int j : allIDs) {
                allHypo.addAll(IdToWord.get(j));
            }

            return allHypo;

        } else {
            return null;
        }
    }


}
