package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    private Map<Integer, Set<Integer>> adjList;

    // I don't see a need in the constructor for now
    public Graph() {
        adjList = new HashMap<>();
    }

    public void addVertex(int v) {
        if (!adjList.containsKey(v)) {
            adjList.put(v, new HashSet<Integer>());
        }
    }

    public void addEdge(int parent, int child) {
        addVertex(parent);
        addVertex(child);

        adjList.get(parent).add(child);
    }

    public Set<Integer> adj(int v) {
        return adjList.get(v);
    }

    // the reason I called it dfs is because this type of
    // traversal explores a graph by following one path
    // as deeply as possible before backtracking
    // could've called it traverse also
    private void traverse(int v, Set<Integer> marked) {
        marked.add(v);
        for (Integer neighbor : adj(v)) {
            if (!marked.contains(neighbor)) {
                traverse(neighbor, marked);
            }
        }
    }

    public Set<Integer> reachable(int v) {
        Set<Integer> marked = new HashSet<>();
        traverse(v, marked);
        return marked;
    }
}
