package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    WordNet graph;

    public HyponymsHandler(WordNet graph) {
        this.graph = graph;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int length = words.size();

        TreeSet<String> initialHypo = new TreeSet<>();

        initialHypo = graph.getAllWords(graph.getRelevantIDs(words.get(0)));

        if (length > 1) {
            for (String word : words) {
                TreeSet<String> nextWordSet = graph.getAllWords(graph.getRelevantIDs(word));
                initialHypo.retainAll(nextWordSet);
            }
        }

        return initialHypo.toString();

    }


}


