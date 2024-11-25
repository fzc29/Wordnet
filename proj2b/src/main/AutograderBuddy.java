package main;

import browser.NgordnetQueryHandler;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {

//        HypoGraph NG = new HypoGraph(synsetFile, hyponymFile);
        // hns.register("hyponyms", new HyponymsHandler(NG));

        SynsetGraph SG = new SynsetGraph(synsetFile, hyponymFile);
        WordNet WN = new WordNet(synsetFile, hyponymFile);

        return new HyponymsHandler(WN);
        // throw new RuntimeException("Please fill out AutograderBuddy.java!");
    }
}
