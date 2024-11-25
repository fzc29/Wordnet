package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {

    NGramMap map;
    TimeSeries counts;

    public HistoryTextHandler(NGramMap map) {
        // NGramMap ngm = new NGramMap("top_14377_words.csv", "total_counts.csv");
        this.map = map;
        this.counts = map.totalCountHistory();
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        String response = "";
        for (String w : words) {
            response += w + ": " + map.weightHistory(w, startYear, endYear).toString() + "\n";
        }
        return response;
    }
}
