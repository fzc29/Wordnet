package main;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class IDMapping extends TreeMap<Integer, ArrayList<Integer>> {

    // public TreeMap<Integer, ArrayList<Integer>> IDMap;

    public IDMapping() {
        super();
    }



    public Set<Integer> getAllHypoIDs(int main, Set<Integer> k) {
        if (this.get(main).isEmpty()) {
            k.add(main);
            return k;
        } else {
            ArrayList<Integer> current = new ArrayList<>();
            current.add(main);
            for (int id : this.get(main)) {
                current.addAll(getAllHypoIDs(id, k));
            }
            k.addAll(current);
            return k;
        }

    }


}
