package com.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

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

    private void addVertex(T vertex) {
        graph.put(vertex, new HashMap<>());
        vertexWeights.put(vertex, 0); // Initialize the vertex weight to 0
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
                        (node.toString() + "," + " (" + graph.get(vertex).get(node) + ") "));
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

    public void visualizeGraph() {
        mxGraph jgxAdapter = new mxGraph();
        Object parent = jgxAdapter.getDefaultParent();

        jgxAdapter.getModel().beginUpdate();
        try {
            Map<T, Object> vertexMap = new HashMap<>();
            for (T vertex : graph.keySet()) {
                // Calculate the width of the vertex based on the label
                String label = vertex.toString() + " (" + vertexWeights.get(vertex) + ")";
                double width = Math.max(80, label.length() * 10); // Estimate the width based on the number of
                                                                  // characters in the label
                vertexMap.put(vertex, jgxAdapter.insertVertex(parent, null, label, 20, 20, width, 30));
            }
            for (T source : graph.keySet()) {
                for (T destination : graph.get(source).keySet()) {
                    Object edge = jgxAdapter.insertEdge(parent, null, graph.get(source).get(destination),
                            vertexMap.get(source),
                            vertexMap.get(destination), "labelBackgroundColor=white");
                    String style = jgxAdapter.getModel().getStyle(edge);
                    style += ";endArrow=classic"; // Add an arrow at the end of the edge
                    jgxAdapter.getModel().setStyle(edge, style);
                }
            }
        } finally {
            jgxAdapter.getModel().endUpdate();
        }

        // Apply a hierarchical layout to the vertices
        mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

        // Create a graph component and add it to a frame for visualization
        mxGraphComponent graphComponent = new mxGraphComponent(jgxAdapter);
        JFrame frame = new JFrame();
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Create a buffered image and use the mxCellRenderer to draw the graph into the
        // image
        BufferedImage image = new BufferedImage((int) jgxAdapter.getGraphBounds().getWidth(),
                (int) jgxAdapter.getGraphBounds().getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        mxCellRenderer.drawCells(jgxAdapter, null, 1, null, new mxCellRenderer.CanvasFactory() {
            @Override
            public mxICanvas createCanvas(int width, int height) {
                mxGraphics2DCanvas canvas = new mxGraphics2DCanvas(graphics);
                canvas.setScale(1);
                return canvas;
            }
        });

        // Write the image to a PNG file
        File imgFile = new File("graph.png");
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
