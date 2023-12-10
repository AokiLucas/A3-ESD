package com.example.Class;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.Objects.FileObject;
import com.example.Objects.TextObject;

public class Main {
    static long startTimer;
    static long endTimer;

    public static void main(String[] args) throws IOException {

        // Classe grafo
        Graph<String> fullGraph = new Graph<>();
        Graph<String> autoresGraph = new Graph<>();

        // Classe dos arquivos
        // Incializar dando a pasta onde os arquivos estão
        String folderPath = "demo\\resumes";
        List<FileObject> fileList = ReadFiles.readTxtFiles(folderPath);

        Scanner scanner = new Scanner(System.in);
        int number = -1;
        String commands = ("\nDigite: \n" +
                "1 - Exibir lista de arquivos na pasta.\n" +
                "2 - Para gerar o grafo de todos os arquivos.\n" +
                "3 - Para escolher os arquivos a gerar o grafo.\n" +
                "9 - Exibir as opções novamente.\n" +
                "0 - Para sair. \n");

        System.out.println("\nQuantidade de arquivos '.txt' encontrados na pasta: '" + folderPath + "': ");
        System.out.println("- " + fileList.size());

        System.out.println(commands);

        do { // O(n)
            try {

                number = scanner.nextInt();

                switch (number) {
                    // Print de todos os arquivos '.txt' dentro da pasta
                    case 1:

                        System.out.println(
                                "\nQuantidade de arquivos '.txt' encontrados na pasta: '" + folderPath + "': ");
                        System.out.println("- " + fileList.size() + "\n");

                        ReadFiles.printFiles(fileList); // O(n)
                        break;
                    // O(n)

                    // Vai gerar todos os grafos
                    case 2:
                        startTimer = System.nanoTime();
                        runGraph(fileList, fullGraph, autoresGraph);
                        endTimer = System.nanoTime() - startTimer;

                        clearPrompt();
                        System.out
                                .println(
                                        "\nTodos os grafos foram gerados em uma pasta com o nome do próprio arquivo '.txt'");

                        System.out.println(endTimer / 1000000000 + "s");

                        break;
                    // O(n + mlogm + m^2 + q + r)

                    // Vai gerar os grafos dos arquivos selecionados para o mesmo
                    case 3:
                        scanner.nextLine();
                        chooseFiles(scanner, fileList, fullGraph, autoresGraph);
                        break;
                    /*
                     * Se a lista estiver vazia a complexidade será de O(1),
                     * já se possuir valores será de O(n + mlogm + m^2 + p + q + r) por conta da
                     * geração
                     * dos grafos
                     */

                    // Print nos comandos novamente
                    case 9:
                        clearPrompt();
                        System.out.println(commands);
                        break;
                    // O(1)

                    default:
                        if (number != (0)) {
                            System.out.println("\nDigite uma opção válida.\n");
                        }
                        break;
                }
            } catch (java.util.InputMismatchException e) {
                clearPrompt();
                System.out.println("\nDigite um caractere válido.");
                scanner.nextLine();
            }
        } while (number != 0); // O(n)

        scanner.close();
    } /*
       * Dependendo de cada caso o programa toma complexidades diversar, mas em sua
       * maioria ele vai ser O(n + mlogm + m^2 + q + r)
       */

    public static void chooseFiles(Scanner scanner, List<FileObject> fileList, Graph<String> fullGraph,
            Graph<String> autoresGraph)
            throws IOException {
        String commands = "\n- Digite o nome do arquivo que deseja e aperte 'enter'." +
                "\n- Digite '.files' para vizualizar os arquivos da pasta." +
                "\n- Digite '.lista' para vizualizar os arquivos escolhidos para gerar o Grafo." +
                "\n- Digite '.sair' quando quiser parar e excutar o programa." +
                "\n- Digite '.help' para exibir os comandos novamente.\n";

        clearPrompt();

        System.out.println(commands);

        // Variavel que recebe o nosso scanner
        String i;

        List<FileObject> tempFileList = new ArrayList<>();

        do { // O(n)
            i = scanner.nextLine().toLowerCase();

            FileObject foundFile = null;

            // Retorna o nosso arquivo desejado quando encontrado dentro da lista de
            // elementos da pasta
            for (FileObject fileDetails : fileList) { // O(m)
                if (fileDetails.getFileName().equals(i)) {
                    foundFile = fileDetails;
                    break;
                }
            } // O(m)

            // Se existe o arquivo
            if (foundFile != null) {
                boolean alreadyExists = false;

                // Procura dentro da lista temporaria se o arquivo ja foi adicionado antes ou
                // não
                for (FileObject tempFileDetail : tempFileList) { // O(p)
                    if (tempFileDetail.getFileName().equals(i)) {
                        alreadyExists = true;
                        System.out.println("\n- O arquivo já foi adicionado.\n");
                        break;
                    }
                }

                // Se o arquivo não está dentro da lista temporaria ele vai adicionar o mesmo
                if (!alreadyExists) {
                    tempFileList.add(foundFile);
                    System.out.println("\n- Arquivo adicionado.\n");
                }
            } // Vai mostrar todos os arquivos da pasta
            else if (i.equals(".files")) {
                System.out.println("\nArquivos encotrados: ");
                ReadFiles.printFiles(fileList); // O(m)
                System.out.println("");
            } // Vai mostrar todos os elementos na lista temporaria caso ja tenham algum
            else if (i.equals(".lista")) {
                if (tempFileList.isEmpty()) {
                    System.out.println("\n- A lista ainda está vazia no momento.\n");
                } else {
                    System.out.println("\nArquivos na lista: ");
                    ReadFiles.printFiles(tempFileList); // O(p)
                    System.out.println("");
                }
            } // Exibe a lista de comandos novamente
            else if (i.equals(".help")) {
                System.out.println(commands);
            }
            // Se for diferente da palavra '.sair' e das demais ele entende que deveria ser
            // o nome de uma arquivo e que o nome está errado
            else if (!i.equals(".sair")) {
                System.out.println("\n- Arquivo não encontrado.\n");
            }
        }
        // Executa até o usuário desejar sair
        while (!i.equals(".sair")); // O(n + m + p)

        // Se no final a lista não for vazia vai executar a geração dos grafos dos
        // elementos desejados
        if (!tempFileList.isEmpty()) {

            startTimer = System.nanoTime();
            runGraph(tempFileList, fullGraph, autoresGraph); // O(n + mlogm + m^2 + q + r)
            endTimer = System.nanoTime() - startTimer;

            clearPrompt();

            System.out.println("\n- Grafo(s) gerado com sucesso.");

            System.out.println(endTimer / 1000000000 + "s");

        } // Só limpa o console e volta para o menu inicial
        else {
            clearPrompt(); // O(1)
        }
    }/*
      * Se a lista estiver vazia a complexidade será de O(1),
      * já se possuir valores será de O(n + mlogm + m^2 + p + q + r) por conta da
      * geração
      * dos grafos
      */

    public static void runGraph(List<FileObject> fileList, Graph<String> fullGraph, Graph<String> autoresGraph)
            throws IOException {
        // Vai executar para cada arquivo '.txt' dentro da pasta
        // TODO Criar uma lista para poder acessar os grafos individualmente para fazer
        // as comparações
        for (FileObject fileDetails : fileList) { // O(n)
            Graph<String> graphObject = new Graph<>();

            // Inicializa a LoadText com o arquivo
            TextObject textDetails = TextClean.LoadText(fileDetails.getFilePath() + ".txt"); // O(m)

            String text = textDetails.getText();

            // Limpeza
            text = TextClean.Regex(text); // O(m)
            text = TextClean.StopWords(text); // O(m * p)

            // Tokenizacao
            String[] tokens = TextClean.Tokenizacao(text); // O(m)

            // Texto ja limpo e lematizado
            String[] cleanText = TextClean.Lematizacao(tokens, TextClean.POSTagger(tokens) /* O(m) */); // O(m + q)

            // Vai adicionar a palavra atual a proxima com uma aresta e aumentar seu peso
            // caso ja exista
            // Caso a palavra nao seja um vertice ainda vai criar o mesmo antes e depois
            // realizar o nó
            for (int i = 0; i < cleanText.length - 1; i++) {
                graphObject.addEdge(cleanText[i], cleanText[i + 1], false);

                fullGraph.addEdge(cleanText[i], cleanText[i + 1], false);
            } // O(m + q)

            // Vai fazer o mesmo e adicionar os autores, mas como um grafo bidirecional
            for (int i = 0; i < textDetails.getautores().length - 1; i++) {
                autoresGraph.addEdge(textDetails.getautores()[i], textDetails.getautores()[i + 1], true);
            } // O(m)

            // Se tanto o arqivo '.csv' e '.png' já existam eles são apenas substituidos
            // Da um print no grafo e gera a imagem do grafo como png

            graphObject.printGraph(fileDetails.getFileName(), fileDetails.getGraphPath(),
                    "_graph"); // O(m)

            graphObject.printGraph(fileDetails.getFileName(), fileDetails.getGraphPath(),
                    "_topics"); // O(mlogm + r)

        } // O(n + mlogm + m^2 + q + r)

        // Apos executar todos os grafos na lista vai gerar o grafo completo com todos
        // os dados e o grafo dos autores
        fullGraph.printGraph("fullGraph", "demo\\graphs", "_graph"); // O(n)
        autoresGraph.printGraph("autores", "demo\\graphs", "_autoresGraph");// O(n)
    } // O(n + mlogm + m^2 + q + r)

    // Limpa o console
    public static void clearPrompt() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    } // O(1)
}
