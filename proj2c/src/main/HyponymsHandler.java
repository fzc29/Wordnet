package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import ngrams.NGramMap;
import ngrams.TimeSeries;


import java.util.*;


public class HyponymsHandler extends NgordnetQueryHandler {

    WordNet graph;
    NGramMap map;

    public HyponymsHandler(WordNet graph, NGramMap map) {
        this.graph = graph;
        this.map = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int maxNumOfWords = q.k();
        int start = q.startYear();
        int end = q.endYear();
        NgordnetQueryType type = q.ngordnetQueryType();

        TreeSet<String> allWords = new TreeSet<>();

        if (type == NgordnetQueryType.HYPONYMS) {
            allWords = handleHelper(words, true);
        } else if (type == NgordnetQueryType.ANCESTORS) {
            allWords = handleHelper(words, false);
        }

        if (maxNumOfWords != 0) {
            PriorityQueue<FreqNode> maxFrequency = createQueue(allWords, start, end);
            TreeSet<String> topKwords = new TreeSet<>();

            while (maxNumOfWords != 0) {
                FreqNode val = maxFrequency.poll();
                if (val != null) {
                    topKwords.add(val.word);
                }
                maxNumOfWords -= 1;
            }

            return topKwords.toString();
        }

        return allWords.toString();
    }


    public TreeSet<String> handleHelper(List<String> words, boolean t) {
        // should return me a list of all the words that are viable

        // List<String> words = wordsList;
        int length = words.size();

        TreeSet<String> initialHypo = new TreeSet<>();

        initialHypo = graph.getAllWords(graph.getRelevantIDs(words.get(0)), t);

        if (length > 1) {
            for (String word : words) {
                TreeSet<String> nextWordSet = graph.getAllWords(graph.getRelevantIDs(word), t);
                initialHypo.retainAll(nextWordSet);
            }
        }

        return initialHypo;
    }

    public static class FreqNode {
        String word;
        Double totalFrequency;
        public FreqNode(String word, Double total) {
            this.word = word;
            this.totalFrequency = total;
        }
    }

    static class FreqNodeComparator implements Comparator<FreqNode> {
        @Override
        public int compare(FreqNode a, FreqNode b) {
            int compare = Double.compare(b.totalFrequency, a.totalFrequency);
            if (compare != 0) {
                return compare;
            }
            return Double.compare(a.totalFrequency, b.totalFrequency);
        }
    }


    public double getCount(String word, int start, int end) {
        TimeSeries yearsWanted = map.countHistory(word, start, end);
        return map.totalCount(yearsWanted);
    }

    public PriorityQueue<FreqNode> createQueue(TreeSet<String> words, int start, int end) {
        PriorityQueue<FreqNode> maxFrequency = new PriorityQueue<>(new FreqNodeComparator());
        for (String word : words) {
            double freq = getCount(word, start, end);
            if (freq != 0.0) {
                maxFrequency.add(new FreqNode(word, freq));
            }
        }
        return maxFrequency;
    }

}




