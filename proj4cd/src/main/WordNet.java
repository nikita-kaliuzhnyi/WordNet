package main;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    private Graph graph;
    private Map<String, Set<Integer>> wordToIds;
    private Map<Integer, Set<String>> idToWords;

    public WordNet(String synsetsFile, String hyponymsFile) {
        graph = new Graph();
        wordToIds = new HashMap<>();
        idToWords = new HashMap<>();

        In synsetsIn = new In(synsetsFile);

        while (!synsetsIn.isEmpty()) {
            String line = synsetsIn.readLine();
            String[] parts = line.split(",");

            int id = Integer.parseInt(parts[0]);
            String[] words = parts[1].split(" ");

            graph.addVertex(id);

            Set<String> wordsSet = new HashSet<>();
            for (String word : words) {
                wordsSet.add(word);
                if (!wordToIds.containsKey(word)) {
                    wordToIds.put(word, new HashSet<Integer>());
                }
                wordToIds.get(word).add(id);
            }
            idToWords.put(id, wordsSet);
        }

        In hyponymsIn = new In(hyponymsFile);

        while (!hyponymsIn.isEmpty()) {
            String line = hyponymsIn.readLine();
            String[] parts = line.split(",");

            int parent = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int child = Integer.parseInt(parts[i]);
                graph.addEdge(parent, child);
            }
        }
    }

    public Set<String> hyponyms(String word) {
        Set<Integer> reachableIds = new HashSet<>();
        Set<String> hyponyms = new TreeSet<>();

        if (!wordToIds.containsKey(word)) {
            return hyponyms;
        }

        for (Integer id : wordToIds.get(word)) {
            reachableIds.addAll(graph.reachable(id));
        }

        for (Integer id : reachableIds) {
            hyponyms.addAll(idToWords.get(id));
        }

        return hyponyms;
    }

    // Overloading
    public Set<String> hyponyms(List<String> words) {
        Set<String> result = new TreeSet<>();

        if (words.isEmpty()) {
            return result;
        }

        // because intersection of {...} and {} is {}, we have to populate empty
        // set before intersecting with the rest
        result.addAll(this.hyponyms(words.get(0)));

        for (int i = 1; i < words.size(); i++) {
            result.retainAll(this.hyponyms(words.get(i)));
        }

        return result;
    }
}
