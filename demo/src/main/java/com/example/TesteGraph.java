package com.example;

import java.io.IOException;

public class TesteGraph {
    public static void main(String[] args) throws IOException {
        Graph<String> graphObject = new Graph<>();

        String text = TextClean.LoadText("demo\\models\\textoTeste.txt");
        //String text = TextClean.LoadText("demo\\models\\essayWord.txt");
        text = TextClean.Regex(text);
        text = TextClean.StopWords(text); 
        
        

        String[] cleanText = TextClean.Lematizacao(TextClean.Tokenizacao(text), TextClean.POSTagger(text));

        //Vai adicionar a palavra atual a proxima como aresta
        for (int i = 0; i < cleanText.length - 1; i++) {
            graphObject.addEdge(cleanText[i], cleanText[i+1]);
        }

        //graphObject.addEdge("0", "1");
        //graphObject.addEdge("0", "teste");

        System.out.println("Graph:\n" + graphObject.printGraph());

        graphObject.hasVertex("4");
        graphObject.hasEdge("teste", "texto");
    }
}
