package main;

import browser.NgordnetQueryHandler;
import ngrams.NGramMap;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {

        // HypoGraph NG = new HypoGraph(synsetFile, hyponymFile);
        // SynsetGraph SG = new SynsetGraph(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNet WN = new WordNet(synsetFile, hyponymFile);

        return new HyponymsHandler(WN, ngm);
    }
}
