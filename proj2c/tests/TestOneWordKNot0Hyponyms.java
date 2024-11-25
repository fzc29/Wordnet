import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import browser.NgordnetQueryType;
import main.AutograderBuddy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TestOneWordKNot0Hyponyms {
    public static final String WORDS_FILE = "data/ngrams/frequency-EECS.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets-EECS.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms-EECS.txt";

    public static final String VERY_SHORT_WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String TOTAL_COUNT_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILEs = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILEs = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms.txt";

    private static final String SMALL_WORDS_FILE = "data/ngrams/top_14377_words.csv";
    private static final String WORD_FILE = "data/ngrams/top_49887_words.csv";
    private static final String HYPONYMS_FILE_SUBSET = "data/wordnet/hyponyms1000-subgraph.txt";
    private static final String SYNSETS_FILE_SUBSET = "data/wordnet/synsets1000-subgraph.txt";

    @Test
    public void testActKNot0() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("CS61A");

        NgordnetQuery nq = new NgordnetQuery(words, 2010, 2020, 4, NgordnetQueryType.HYPONYMS);
        String actual = studentHandler.handle(nq);
        String expected = "[CS170, CS61A, CS61B, CS61C]";
        assertThat(actual).isEqualTo(expected);
    }

    // TODO: Add more unit tests (including edge case tests) here.

    @Test
    public void testNull() {
        List<String> empty = new ArrayList<>();
        assertThat(empty.toString()).isEqualTo("[]");

        List<String> nullList = new ArrayList<>();
//        nullList.add(null);
//        nullList.add(null);
        nullList.add("happy");

        assertThat(nullList.toString()).isEqualTo("[happy]");
    }

    @Test
    public void testWithMultiple() {

        String try1File = "./data/wordnet/try1";
        String try1hypoFile = "./data/wordnet/try1hypo";

        NgordnetQueryHandler try1 = AutograderBuddy.getHyponymsHandler(VERY_SHORT_WORDS_FILE,
                TOTAL_COUNTS_FILE, try1File, try1hypoFile);

        List<String> wordsSingle = List.of("animal");
        List<String> wordsSecond = List.of("land");

        List<String> words = List.of("animal", "land");

        NgordnetQuery nq = new NgordnetQuery(wordsSingle, 0, 0, 0, NgordnetQueryType.HYPONYMS);
        String actual = try1.handle(nq);
        String expected = "[animal, ape, bird, cat, dolphin, feline, hare, kitty, monkey, orangutan, rabbit]";
        assertThat(actual).isEqualTo(expected);

        NgordnetQuery nq2 = new NgordnetQuery(wordsSecond, 0, 0, 0, NgordnetQueryType.HYPONYMS);
        String actual2 = try1.handle(nq2);
        String expected2 = "[cat, dog, dolphin, feline, hare, kitty, land, rabbit]";
        assertThat(actual2).isEqualTo(expected2);

        NgordnetQuery nq3 = new NgordnetQuery(words, 0, 0, 0, NgordnetQueryType.HYPONYMS);
        String actual3 = try1.handle(nq3);
        String expected3 = "[cat, dolphin, feline, hare, kitty, rabbit]";
        assertThat(actual3).isEqualTo(expected3);

    }
}
