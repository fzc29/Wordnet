import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import com.sun.source.tree.AssertTree;
import main.AutograderBuddy;
import main.HypoGraph;
import main.IDMapping;
import main.WordNet;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.google.common.truth.Truth.assertThat;

public class TestCommonAncestors {
    public static final String WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String LARGE_WORDS_FILE = "data/ngrams/top_14377_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms.txt";

    public static final String FREQUENCY_EECS_FILE = "data/ngrams/frequency-EECS.csv";
    public static final String SYNSET_EECS_FILE = "data/wordnet/synsets-EECS.csv";
    public static final String HYPONYMS_EECS_FILE = "data/wordnet/hyponyms-EECS.csv";

    /** This is an example from the spec for a common-ancestors query on the word "adjustment".
     * You should add more tests for the other spec examples! */
    @Test
    public void testSpecAdjustment() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("adjustment");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = studentHandler.handle(nq);
        String expected = "[adjustment, alteration, event, happening, modification, natural_event, occurrence, occurrent]";
        assertThat(actual).isEqualTo(expected);
    }

    // TODO: Add more unit tests (including edge case tests) here.

    // TODO: Create similar unit test files for the k != 0 cases.

    @Test
    public void test4_3CommonAncestors() {
        NgordnetQueryHandler Handler = AutograderBuddy.getHyponymsHandler(
                LARGE_WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = List.of("mortgage");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 3, NgordnetQueryType.ANCESTORS);
        String actual = Handler.handle(nq);
        String expected = "[interest, part, share]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void test4_5CommonAncestors() {
        NgordnetQueryHandler Handler = AutograderBuddy.getHyponymsHandler(
                LARGE_WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = List.of("substituting", "union");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 1, NgordnetQueryType.ANCESTORS);
        String actual = Handler.handle(nq);
        String expected = "[activity]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void wordNotInList() {
        NgordnetQueryHandler Handler = AutograderBuddy.getHyponymsHandler(
                LARGE_WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = List.of("hgjdi", "union");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 1, NgordnetQueryType.ANCESTORS);
        String actual = Handler.handle(nq);
        String expected = "[]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void checkparsing() {
        HypoGraph hypo = new HypoGraph(SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        ArrayList<Integer> IDs = hypo.getMainID("change");
        assertThat(IDs.toString()).isEqualTo("[2, 8]");

        ArrayList<Integer> ID2 = hypo.getMainID("apple");
        assertThat(ID2.toString()).isEqualTo("[]");

        assertThat(hypo.parentIDMap.get(13).toString()).isEqualTo("[11]");
    }

    @Test
    public void gethypoIDs() {
        HypoGraph hypo = new HypoGraph(SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        IDMapping child = hypo.childIDMap;
        Set<Integer> try1 = child.getAllHypoIDs(3, new TreeSet<Integer>());
        assertThat(try1.toString()).isEqualTo("[3, 5]");

        IDMapping parent = hypo.parentIDMap;
        Set<Integer> try2 = parent.getAllHypoIDs(3, new TreeSet<Integer>());
        assertThat(try2.toString()).isEqualTo("[0, 1, 2, 3]");
    }

    @Test
    public void multipleSynsetTest() {
        HypoGraph hypo = new HypoGraph("data/wordnet/try1", "data/wordnet/try1hypo");
        IDMapping child = hypo.childIDMap;
        Set<Integer> try1 = child.getAllHypoIDs(99, new TreeSet<Integer>());
        assertThat(try1.toString()).isEqualTo("[0, 72, 99]");

        Set<Integer> try3 = child.getAllHypoIDs(6, new TreeSet<Integer>());
        assertThat(try3.toString()).isEqualTo("[0, 3, 4, 6, 10, 72, 99, 100]");

        IDMapping parent = hypo.parentIDMap;
        Set<Integer> try2 = parent.getAllHypoIDs(100, new TreeSet<Integer>());
        assertThat(try2.toString()).isEqualTo("[5, 6, 100]");

        TreeMap<Integer, ArrayList<String>> IdToWord = hypo.IdToWord;
        Set<String> ancestors = hypo.getWords("dolphin", parent);
        assertThat(ancestors.toString()).isEqualTo("[animal, cat, dog, dolphin, feline, land]");

    }

    @Test
    public void multipleSynsetWord2() {
        HypoGraph hypo = new HypoGraph("data/wordnet/wordlisteasy", "data/wordnet/hyponymeasy");
        IDMapping child = hypo.childIDMap;
        IDMapping parent = hypo.parentIDMap;
        TreeMap<Integer, ArrayList<String>> IdToWord = hypo.IdToWord;
        TreeMap<String, ArrayList<Integer>> wordToId = hypo.wordToID;

        Set<Integer> try1 = child.getAllHypoIDs(0, new TreeSet<Integer>());
        assertThat(try1.toString()).isEqualTo("[0, 1, 2, 3]");

        Set<Integer> try2 = parent.getAllHypoIDs(6, new TreeSet<Integer>());
        assertThat(try2.toString()).isEqualTo("[4, 5, 6]");


        Set<String> ancestors = hypo.getWords("same", parent);
        assertThat(ancestors.toString()).isEqualTo("[a, b, c, e, f, g, same]");
    }

    @Test
    public void testAnc() {
        WordNet wn = new WordNet("data/wordnet/try1", "data/wordnet/try1hypo");
        List<String> words = List.of("orangutan");
        NgordnetQueryHandler Handler = AutograderBuddy.getHyponymsHandler(
                LARGE_WORDS_FILE, TOTAL_COUNTS_FILE, "data/wordnet/try1", "data/wordnet/try1hypo");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 0, NgordnetQueryType.ANCESTORS);
        String actual = Handler.handle(nq);
        String expected = "[animal, monkey, orangutan]";
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    public void testCount() {
        NGramMap ngm = new NGramMap(WORDS_FILE, TOTAL_COUNTS_FILE);
        double total = ngm.totalCount(new TimeSeries());
        assertThat(total).isEqualTo(0.0);
    }

    @Test
    public void testHypo2_2() {
        List<String> words = List.of("transition");
        NgordnetQueryHandler Handler = AutograderBuddy.getHyponymsHandler(
                LARGE_WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);

        NgordnetQuery nq = new NgordnetQuery(words, 1470, 2019, 8, NgordnetQueryType.HYPONYMS);
        String actual = Handler.handle(nq);
        String expected = "[conversion, cut, dissolve, jump, leap, modulation, passage, transition]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testEECSfile2_7() {
        NgordnetQueryHandler Handler = AutograderBuddy.getHyponymsHandler(
               FREQUENCY_EECS_FILE, TOTAL_COUNTS_FILE, SYNSET_EECS_FILE, HYPONYMS_EECS_FILE);

        List<String> words = List.of("transition");
        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 5, NgordnetQueryType.HYPONYMS);
        String actual = Handler.handle(nq);

        String expected = "[CS162, CS164, CS169, CS186]";
        assertThat(actual).isEqualTo(expected);

    }

}
