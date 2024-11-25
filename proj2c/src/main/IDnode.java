package main;

import java.util.ArrayList;


public class IDnode {

    int ID;
    ArrayList<Integer> hypoIDs;
    ArrayList<Integer> ancestorIDs;
    String[] words;


    public IDnode(int ID, String[] synset) {
        this.ID = ID;
        this.words = synset;
        this.hypoIDs = new ArrayList<>();
        this.ancestorIDs = new ArrayList<>();
    }


    private void addIDs(ArrayList<Integer> lst, int idToAdd) {
        lst.add(idToAdd);
    }

    public void addHypo(int idtoAdd) {
        addIDs(hypoIDs, idtoAdd);
    }

    public void addAnc(int idtoAdd) {
        addIDs(ancestorIDs, idtoAdd);
    }

    public ArrayList<String> getWordsforID() {
        ArrayList<String> wordList = new ArrayList<>();
        for (String item : words) {
            wordList.add(item);
        }
        return wordList;
    }


}
