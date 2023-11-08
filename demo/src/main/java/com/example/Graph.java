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
    // Grafo
    private Map<T, Map<T, Integer>> graph = new HashMap<>();
    // Peso dos vertices
    private Map<T, Integer> vertexWeights = new HashMap<>();

    // Adicionar Vertice
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

    /*
     * public void hasVertex(T vertex) {
     * if (graph.containsKey(vertex)) {
     * System.out
     * .println("The Graph contains " + vertex + " as a vertex with weight " +
     * vertexWeights.get(vertex));
     * } else {
     * System.out.println("The Graph does not contain " + vertex + " as a vertex");
     * }
     * }
     * 
     * public void hasEdge(T source, T destination) {
     * if (graph.get(source).containsKey(destination)) {
     * System.out.println("The Graph has an edge between " + source + " (" +
     * vertexWeights.get(source) + ") and "
     * + destination + " ( " + vertexWeights.get(destination) +
     * ") with edge weight "
     * + graph.get(source).get(destination));
     * } else {
     * System.out.println("The Graph has no edge between " + source + " and " +
     * destination);
     * }
     * }
     */

    // Adicionar Aresta
    private void addVertex(T vertex) {
        graph.put(vertex, new HashMap<>());
        vertexWeights.put(vertex, 0); // Initialize the vertex weight to 0
    }

    public String printGraph(String filePath, String fileName) throws IOException {
        StringBuilder builder = new StringBuilder();

        FileWriter csvFileWriter = new FileWriter(filePath + "\\" + fileName + ".csv");
        csvFileWriter.append("Vertice, peso (V), Aresta, peso (A)\n");

        // Veritce
        for (T vertex : graph.keySet()) {
            builder.append(vertex.toString() + " (" + vertexWeights.get(vertex) + "): ");
            // Arestas
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

    // Gerador de arquivo csv com o grafo
    public void csvWriter(FileWriter csvWriter, String vertex, String node) {
        try {
            csvWriter.append(vertex + node + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gerador do grafo
    public void visualizeGraph(String filePath, String fileName) {
        mxGraph jgxAdapter = new mxGraph();
        Object parent = jgxAdapter.getDefaultParent();

        jgxAdapter.getModel().beginUpdate();
        try {
            Map<T, Object> vertexMap = new HashMap<>();
            for (T vertex : graph.keySet()) {
                // Calcula o comprimento do vertice com base no tamanho do texto
                String label = vertex.toString() + " (" + vertexWeights.get(vertex) + ")";
                double width = Math.max(80, label.length() * 10);
                vertexMap.put(vertex, jgxAdapter.insertVertex(parent, null, label, 20, 20, width, 30));
            }
            // Vertice
            for (T source : graph.keySet()) {
                // Arestas
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

        // Cria um grafo em um design hierarquico
        mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

        // Abre um programa para visualização que possibilita edição
        mxGraphComponent graphComponent = new mxGraphComponent(jgxAdapter);
        JFrame frame = new JFrame();
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Cria um imagem do grafo
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

        // Salva a imagem como png
        File imgFile = new File(filePath + "\\" + fileName + ".png");
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
