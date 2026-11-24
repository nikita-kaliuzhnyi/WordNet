package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private WordNet wordNet;
    private NGramMap ngm;

    public HyponymsHandler(WordNet wordNet, NGramMap ngm) {
        this.wordNet = wordNet;
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        Set<String> topKWords = new TreeSet<>();

        Set<String> hyponyms = wordNet.hyponyms(words);

        if (k != 0) {
            Map<Integer, Double> countHistoryPerWord;
            Map<String, Double> countTotalPerWord = new HashMap<>();


            for (String word : hyponyms) {
                countHistoryPerWord = ngm.countHistory(word, startYear, endYear);
                double total = 0;

                for (int year : countHistoryPerWord.keySet()) {
                    total += countHistoryPerWord.get(year);
                }

                if (total > 0) {
                    countTotalPerWord.put(word, total);
                }
            }

            PriorityQueue<String> maxHeapPQ = new PriorityQueue<>((a, b) ->
                    Double.compare(countTotalPerWord.get(b), countTotalPerWord.get(a)));

            for (String word : countTotalPerWord.keySet()) {
                maxHeapPQ.add(word);
            }

            for (int i = 0; i < k; i++) {
                if (maxHeapPQ.size() != 0) {
                    topKWords.add(maxHeapPQ.poll());
                }
            }

            return topKWords.toString();
        }



        return hyponyms.toString();
    }
}
