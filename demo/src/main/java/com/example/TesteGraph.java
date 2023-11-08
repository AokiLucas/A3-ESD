package com.example;

import java.io.IOException;
import java.util.List;

public class TesteGraph {
    public static void main(String[] args) throws IOException {
        Graph<String> graphObject = new Graph<>();
        List<FileDetails> fileList = ReadFiles.readTxtFiles("demo\\resumes");

        // A forma de como os arquivos vão ser tratados vai ser melhorada para poder ler
        // mais arquivos de uma vez
        // e poder gerar de forma organizado os arquivos
        // String text = TextClean.LoadText("demo\\models\\textoTeste.txt");
        // String text = TextClean.LoadText("demo\\resumes\\arq_1.txt");

        for (FileDetails fileDetails : fileList) {
            String text = TextClean.LoadText(fileDetails.getFilePath()+".txt");
            text = TextClean.Regex(text);
            text = TextClean.StopWords(text);

            String[] tokens = TextClean.Tokenizacao(text);

            String[] cleanText = TextClean.Lematizacao(TextClean.Tokenizacao(text), TextClean.POSTagger(tokens));

            // Vai adicionar a palavra atual a proxima como aresta
            for (int i = 0; i < cleanText.length - 1; i++) {
                graphObject.addEdge(cleanText[i], cleanText[i + 1]);
            }

            System.out.println("Graph:\n" + graphObject.printGraph(fileDetails.getFilePath(), fileDetails.getFileName()));

            graphObject.visualizeGraph(fileDetails.getFilePath(), fileDetails.getFileName());
        }

    }
}
