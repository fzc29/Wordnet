package main;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class IDnode {

    int ID;
    List<Integer> hypoIDs;
    List<String> words;


    public IDnode(int ID, String[] synset) {
        this.ID = ID;
        this.hypoIDs = new ArrayList<>();
        this.words = new ArrayList<>();
        for (String word : synset) {
            this.words.add(word);
        }
    }



    private void addIDs(List<Integer> lst, int idToAdd) {
        lst.add(idToAdd);
    }

    public void addHypo(int idtoAdd) {
        addIDs(hypoIDs, idtoAdd);
    }


    public List<String> getWordsforID() {
        return words;
    }


}
