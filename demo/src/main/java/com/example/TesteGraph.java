package com.example;

import java.io.IOException;
import java.util.List;

public class TesteGraph {
    public static void main(String[] args) throws IOException {
        // Classe grafo
        Graph<String> graphObject = new Graph<>();

        // Classe dos arquivos
        // Incializar dando a pasta onde os arquivos estão
        List<FileDetails> fileList = ReadFiles.readTxtFiles("demo\\resumes");

        // Vai executar para cada arquivo '.txt' dentro da pasta
        // TODO Criar uma lista para poder acessar os grafos individualmente e ver o que
        // fazer
        // em relacao aos autores, provavelmente uma lista co-relacionado o grafo com
        // autor
        for (FileDetails fileDetails : fileList) {
            // Inicializa a LoadText com o arquivo
            String text = TextClean.LoadText(fileDetails.getFilePath() + ".txt");

            // Limpeza
            text = TextClean.Regex(text);
            text = TextClean.StopWords(text);

            // Tokenizacao
            String[] tokens = TextClean.Tokenizacao(text);

            // Texto ja limpo e lematizado
            String[] cleanText = TextClean.Lematizacao(TextClean.Tokenizacao(text), TextClean.POSTagger(tokens));

            // Vai adicionar a palavra atual a proxima com uma aresta e aumentar seu peso
            // caso ja exista
            // Caso a palavra nao seja um vertice ainda vai criar o mesmo antes e depois
            // realizar o nó
            for (int i = 0; i < cleanText.length - 1; i++) {
                if (!cleanText[i].equals(null) && !cleanText[i + 1].equals(null))
                    graphObject.addEdge(cleanText[i], cleanText[i + 1]);
            }

            // Se tanto o arqivo '.csv' e '.png' já existam eles são apenas substituidos
            // Da um print no grafo
            System.out
                    .println("Graph:\n" + graphObject.printGraph(fileDetails.getFilePath(), fileDetails.getFileName()));

            // Gera a imagem do grafo como png
            graphObject.visualizeGraph(fileDetails.getFilePath(), fileDetails.getFileName());
        }

    }
}
