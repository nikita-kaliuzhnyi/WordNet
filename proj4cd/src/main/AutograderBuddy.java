package main;

import browser.NgordnetQueryHandler;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordHistoryFile, String yearHistoryFile,
            String synsetFile, String hyponymFile) {

        WordNet wordNet = new WordNet(synsetFile, hyponymFile);
        NGramMap ngm = new NGramMap(wordHistoryFile, yearHistoryFile);

        return new HyponymsHandler(wordNet, ngm);
    }
}
