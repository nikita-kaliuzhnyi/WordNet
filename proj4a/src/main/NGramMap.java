package main;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import edu.princeton.cs.algs4.In;

import static main.TimeSeries.MAX_YEAR;
import static main.TimeSeries.MIN_YEAR;

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

    private Map<String, TimeSeries> wordHistory; // associates each word with its own TimeSeries
    private TimeSeries totalHistory; // This stores the total number of words recorded in each year, regardless of
    // which word we're looking at

    /**
     * Constructs an NGramMap from WORDHISTORYFILENAME and YEARHISTORYFILENAME.
     */
    public NGramMap(String wordHistoryFilename, String yearHistoryFilename) {
        wordHistory = new HashMap<>();
        totalHistory = new TimeSeries();

        // Read the word history file
        In in = new In(wordHistoryFilename);

        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] parts = line.split("\t");

            String word = parts[0];
            int year = Integer.parseInt(parts[1]);
            double count = Double.parseDouble(parts[2]);

            if (!wordHistory.containsKey(word)) {
                wordHistory.put(word, new TimeSeries());
            }

            wordHistory.get(word).put(year, count);
        }

        // Read the year history file
        In yearIn = new In(yearHistoryFilename);

        while (!yearIn.isEmpty()) {
            String line = yearIn.readLine();
            String[] parts = line.split(",");

            int year = Integer.parseInt(parts[0]);
            double totalCount = Double.parseDouble(parts[1]);

            totalHistory.put(year, totalCount);
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
        if (!wordHistory.containsKey(word)) {
            return new TimeSeries();
        }

        return new TimeSeries(wordHistory.get(word), startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        if (!wordHistory.containsKey(word)) {
            return new TimeSeries();
        }

        return new TimeSeries(wordHistory.get(word), MIN_YEAR, MAX_YEAR);
        // or just implement like "return countHistory(word, 1400, 2100)"
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(totalHistory, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        if (!wordHistory.containsKey(word)) {
            return new TimeSeries();
        }

        return countHistory(word, startYear, endYear).dividedBy(totalCountHistory());
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        if (!wordHistory.containsKey(word)) {
            return new TimeSeries();
        }

        return countHistory(word).dividedBy(totalCountHistory());
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries result = new TimeSeries();
        for (String word : words) {
            TimeSeries current = countHistory(word, startYear, endYear);
            result = result.plus(current);
        }

        return result.dividedBy(totalCountHistory());
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {

        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }

}
