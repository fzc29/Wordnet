package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.TreeMap;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    TimeSeries countAll = new TimeSeries();
    TreeMap<String, TimeSeries> wordList = new TreeMap<>();


    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {

        // represent wordsFilename with timeSeries
        // countsFilenname use
        In wordFile = new In(wordsFilename);
        int i = 0;
        while (!wordFile.isEmpty()) {
            i += 1;
            String nextLine = wordFile.readLine();
            String[] split = nextLine.split("\t");
            if (wordList.containsKey(split[0])) {
                wordList.get(split[0]).put(Integer.parseInt(split[1]), Double.valueOf(split[2]));
            } else {
                wordList.put(split[0], new TimeSeries());
                wordList.get(split[0]).put(Integer.parseInt(split[1]), Double.valueOf(split[2]));
            }
        }

        In countFile = new In(countsFilename);
        int j = 0;
        while (!countFile.isEmpty()) {
            j += 1;
            String nextL = countFile.readLine();
            String[] splitL = nextL.split(",");
            countAll.put(Integer.parseInt(splitL[0]), Double.valueOf(splitL[1]));
        }

    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {

        if (wordList.containsKey(word)) {
            return new TimeSeries(wordList.get(word), startYear, endYear);
        }
        return new TimeSeries();
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {

        if (wordList.containsKey(word)) {
            return new TimeSeries(wordList.get(word), MIN_YEAR, MAX_YEAR);
        }
        return new TimeSeries();

    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {

        return new TimeSeries(countAll, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {

        // relative frequency = this year amount / total amount frequency of this word
        if (!wordList.containsKey(word)) {
            return new TimeSeries();
        }

        TimeSeries weighted = new TimeSeries();
        for (int i = startYear; i <= endYear; i++) {
            if (wordList.get(word).containsKey(i)) {
                weighted.put(i, wordList.get(word).get(i) / countAll.get(i));
            }
        }
        return weighted;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {

        // amount of this word / total count
        if (!wordList.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries weightedOverallFreq = new TimeSeries();
        for (int i : wordList.get(word).years()) {
            weightedOverallFreq.put(i, wordList.get(word).get(i) / countAll.get(i));
        }
        return weightedOverallFreq;
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {

        TimeSeries general = new TimeSeries();
        for (String w : words) {
            general = general.plus(weightHistory(w, startYear, endYear));
        }
        return general;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {

        TimeSeries general = new TimeSeries();
        for (String w : words) {
            general = general.plus(weightHistory(w));
        }
        return general;
    }

}
