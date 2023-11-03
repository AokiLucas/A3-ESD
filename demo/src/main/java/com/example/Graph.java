package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Graph<T> {
    private Map<T, Map<T, Integer>> graph = new HashMap<>();
    private Map<T, Integer> vertexWeights = new HashMap<>();

    public void addEdge(T source, T destination) {
        if (!graph.containsKey(source)) {
            addVertex(source);
        }
        if (!graph.containsKey(destination)) {
            addVertex(destination);
        }
        Map<T, Integer> edges = graph.get(source);
        edges.put(destination, edges.getOrDefault(destination, 0) + 1);

        // Increase the weight of the source and destination vertices
        vertexWeights.put(source, vertexWeights.get(source) + 1);
        vertexWeights.put(destination, vertexWeights.get(destination) + 1);
    }

    public void hasVertex(T vertex) {
        if (graph.containsKey(vertex)) {
            System.out
                    .println("The Graph contains " + vertex + " as a vertex with weight " + vertexWeights.get(vertex));
        } else {
            System.out.println("The Graph does not contain " + vertex + " as a vertex");
        }
    }

    public void hasEdge(T source, T destination) {
        if (graph.get(source).containsKey(destination)) {
            System.out.println("The Graph has an edge between " + source + " (" + vertexWeights.get(source) + ") and "
                    + destination + " ( " + vertexWeights.get(destination) + ") with edge weight "
                    + graph.get(source).get(destination));
        } else {
            System.out.println("The Graph has no edge between " + source + " and " + destination);
        }
    }

    public String printGraph() throws IOException {
        StringBuilder builder = new StringBuilder(); 

        FileWriter csvFileWriter = new FileWriter("Graph.csv");
        csvFileWriter.append("Vertice, peso (V), Aresta, peso (A)\n");

        for (T vertex : graph.keySet()) {
            builder.append(vertex.toString() + " (" + vertexWeights.get(vertex) + "): ");
            for (T node : graph.get(vertex).keySet()) {
                builder.append(node.toString() + " (" + graph.get(vertex).get(node) + ") ");

                csvWriter(csvFileWriter, (vertex.toString() + "," + " (" + vertexWeights.get(vertex) + ")" + ", "),
                        (node.toString() + "," +" (" + graph.get(vertex).get(node) + ") "));
            }
            builder.append("\n");

        }

        csvFileWriter.flush();
        csvFileWriter.close();
        return builder.toString();
    }

    public void csvWriter(FileWriter csvWriter, String vertex, String node) {
        try {
            csvWriter.append(vertex + node + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addVertex(T vertex) {
        graph.put(vertex, new HashMap<>());
        vertexWeights.put(vertex, 0); // Initialize the vertex weight to 0
    }
}
