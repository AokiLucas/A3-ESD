package com.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

class Graph<T> {
    // Grafo
    private Map<T, Map<T, Integer>> graph = new HashMap<>();
    // Peso dos vertices
    private Map<T, Integer> vertexWeights = new HashMap<>();

    // Adicionar Aresta
    public void addEdge(T source, T destination, boolean bidirectional) {
        // Se nao existir o vertice ele ira criar
        if (!graph.containsKey(source)) {
            addVertex(source);
        }
        if (!graph.containsKey(destination)) {
            addVertex(destination);
        }
        // Lista das Arestas
        Map<T, Integer> edges = graph.get(source);
        edges.put(destination, edges.getOrDefault(destination, 0) + 1);

        // Aumenta o peso interno da palavras
        vertexWeights.put(source, vertexWeights.get(source) + 1);
        vertexWeights.put(destination, vertexWeights.get(destination) + 1);

        if (bidirectional == true) {
            Map<T, Integer> reverseEdges = graph.get(destination);
            reverseEdges.put(source, reverseEdges.getOrDefault(source, 0) + 1);
        }
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

    // Adicionar Vertice
    private void addVertex(T vertex) {
        graph.put(vertex, new HashMap<>());
        vertexWeights.put(vertex, 0);
    }

    public void printGraph(String fileName, String graphPath, String filePoint) throws IOException {
        StringBuilder builder = new StringBuilder();

        // folderCreater(filePath);
        folderCreater(graphPath + "\\" + fileName);

        FileWriter csvFileWriter = new FileWriter(graphPath + "\\" + fileName + "\\" + fileName + filePoint + ".csv");
        csvFileWriter.append("Vertice, peso (V), Aresta, peso (A)\n");

        if (filePoint.equals("_topics")) {
            // Pega os 10 primeiros vertices
            List<T> mostImportant = getImportantVertices();
            List<T> top10 = mostImportant.subList(0, Math.min(mostImportant.size(), 10));

            // Gera o csv do grafo usando apenas os 10 primeiros vertices
            csvGraphConstructor(top10, builder, csvFileWriter);
        } else if (filePoint.equals("_graph")) {
            // Gera o csv do grafo utilizando todo o conjunto de vertices em 'graph'
            csvGraphConstructor(graph.keySet(), builder, csvFileWriter);
        } else if (filePoint.equals("_autoresGraph")) {
            // Gera o csv do grafo utilizando todo o conjunto de vertices em 'graph'

            csvGraphConstructor(graph.keySet(), builder, csvFileWriter);
        }

        csvFileWriter.flush();
        csvFileWriter.close();

        // Gera a imagem do grafo
        visualizeGraph(fileName, graphPath, filePoint);
    }

    // Constroi o csv do grafo com base na quantidade de vertices
    public void csvGraphConstructor(Collection<T> vertexKeySet, StringBuilder builder, FileWriter csvFileWriter)
            throws IOException {
        for (T vertex : vertexKeySet) {
            builder.append(vertex.toString()).append(" (").append(vertexWeights.get(vertex)).append("), ");
            // Arestas
            for (T node : graph.get(vertex).keySet()) {
                builder.append(node.toString()).append(" (").append(graph.get(vertex).get(node)).append("), ");
                csvWriter(csvFileWriter, vertex.toString() + ", (" + vertexWeights.get(vertex) + "), ",
                        node.toString() + ", (" + graph.get(vertex).get(node) + ") ");
            }

            builder.append("\n");
        }

    }

    public List<T> getImportantVertices() {
        // Cria um mapa que salva os valores dos scores
        Map<T, Integer> scores = new HashMap<>();

        // Calcula o score para cada vertice
        for (T vertex : graph.keySet()) {
            int score = vertexWeights.get(vertex);
            for (int edgeWeight : graph.get(vertex).values()) {
                score += edgeWeight;
            }
            scores.put(vertex, score);
        }

        // Ordena os vertices do maior para o menor com base nos scores
        List<T> vertices = new ArrayList<>(scores.keySet());
        vertices.sort((v1, v2) -> scores.get(v2) - scores.get(v1));

        return vertices;
    }

    public void folderCreater(String newDirPath) {
        // Cria uma pasta utilizando o path name do arquivo '.txt'
        Path dirPath = Paths.get(newDirPath);
        if (!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public void visualizeGraph(String fileName, String graphPath, String filePoint) {
        mxGraph jgxAdapter = new mxGraph();
        Object parent = jgxAdapter.getDefaultParent();

        jgxAdapter.getModel().beginUpdate();
        try {
            if (filePoint.equals("_topics")) {
                // Pega os 10 primeiros vertices
                List<T> mostImportant = getImportantVertices();
                List<T> top10 = mostImportant.subList(0, Math.min(mostImportant.size(), 10));

                // Gera o construtor do png do Grafo usando apenas os 10 primeiros vertices
                pngGraphConstructor(top10, jgxAdapter, parent);
            } else if (filePoint.equals("_graph")) {
                // Gera o comstrutor do png do Grafo utilizando todos os vertices de 'graph'
                // pngGraphConstructor(graph.keySet(), jgxAdapter, parent);
                return;
            } else if (filePoint.equals("_autoresGraph")) {
                pngGraphConstructor(graph.keySet(), jgxAdapter, parent);
            }
        } finally {
            jgxAdapter.getModel().endUpdate();
        }

        // Cria um grafo em um design hierarquico
        mxHierarchicalLayout layout = new mxHierarchicalLayout(jgxAdapter);
        layout.execute(jgxAdapter.getDefaultParent());

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
        File imgFile = new File(graphPath + "\\" + fileName + "\\" + fileName + filePoint + ".png");
        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gera o construtor do png do Grafo com base na quantidade de vertices
    public void pngGraphConstructor(Collection<T> vertexKeySet, mxGraph jgxAdapter, Object parent) {
        Map<T, Object> vertexMap = new HashMap<>();
        for (T vertex : vertexKeySet) {
            // Calcula o comprimento do vertice com base no tamanho do texto
            String label = vertex.toString() + " (" + vertexWeights.get(vertex) + ")";
            double width = Math.max(80, label.length() * 10);
            vertexMap.put(vertex, jgxAdapter.insertVertex(parent, null, label, 20, 20, width, 30));
        }
        // Vertice
        for (T source : vertexKeySet) {
            // Arestas
            for (T destination : graph.get(source).keySet()) {
                Object edge = jgxAdapter.insertEdge(parent, null, graph.get(source).get(destination),
                        vertexMap.get(source),
                        vertexMap.get(destination), "labelBackgroundColor=white");
                String style = jgxAdapter.getModel().getStyle(edge);
                style += ";endArrow=classic";
                jgxAdapter.getModel().setStyle(edge, style);
            }
        }
    }
}
