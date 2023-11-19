package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TesteGraph {
    static long startTimer;
    static long endTimer;

    public static void main(String[] args) throws IOException {

        // Classe grafo
        Graph<String> graphObject = new Graph<>();

        // Classe dos arquivos
        // Incializar dando a pasta onde os arquivos estão
        String folderPath = "demo\\resumes";
        List<FileDetails> fileList = ReadFiles.readTxtFiles(folderPath);

        Scanner scanner = new Scanner(System.in);
        int number;

        System.out.println("\nQuantidade de arquivos '.txt' encontrados na pasta: '" + folderPath + "': ");
        System.out.println("- " + fileList.size());

        do {
            System.out.println("\nDigite: \n" +
                    "1 - Exibir lista de arquivos na pasta.\n" +
                    "2 - Para gerar o grafo de todos os arquivos.\n" +
                    "3 - Para escolher os arquivos a gerar o grafo.\n" +
                    "0 - Para sair. \n");

            number = scanner.nextInt();

            switch (number) {
                // Print de todos os arquivos '.txt' dentro da pasta
                case 1:
                    System.out.println("\nArquivos encotrados na pasta '" + folderPath + "': ");
                    ReadFiles.printFiles(fileList);
                    break;

                // Vai gerar todos os grafos
                case 2:
                    startTimer = System.nanoTime();
                    runGraph(fileList, graphObject);
                    endTimer = System.nanoTime() - startTimer;

                    clearPrompt();
                    System.out
                            .println(
                                    "\nTodos os grafos foram gerados em uma pasta com o nome do próprio arquivo '.txt'");

                    System.out.println(endTimer / 1000000000 + "s");

                    break;

                // Vai gerar os grafos dos arquivos selecionados para o mesmo
                case 3:
                    scanner.nextLine();
                    chooseFiles(scanner, fileList, graphObject);
                    break;

                default:
                    if (number != 0) {
                        System.out.println("/nDigite uma opção válida./n");
                    }
                    break;
            }
        } while (number != 0);

        scanner.close();
    }

    public static void chooseFiles(Scanner scanner, List<FileDetails> fileList, Graph<String> graphObject)
            throws IOException {
        clearPrompt();

        System.out.println(
                "\n- Digite o nome do arquivo que deseja e aperte 'enter'." +
                        "\n- Digite '.files' para vizualizar os arquivos da pasta." +
                        "\n- Digite '.lista' para vizualizar os arquivos escolhidos para gerar o Grafo." +
                        "\n- Digite '.sair' quando quiser parar.\n");

        String i;
        List<FileDetails> tempFileList = new ArrayList<>();
        do {
            i = scanner.nextLine().toLowerCase();
            FileDetails foundFile = null;

            // Retorna o nosso arquivo desejado quando encontrado dentro da lista de
            // elementos da pasta
            for (FileDetails fileDetails : fileList) {
                if (fileDetails.getFileName().equals(i)) {
                    foundFile = fileDetails;
                    break;
                }
            }

            // Se existe o arquivo
            if (foundFile != null) {
                boolean alreadyExists = false;

                // Procura dentro da lista temporaria se o arquivo ja foi adicionado antes ou
                // não
                for (FileDetails tempFileDetail : tempFileList) {
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
                ReadFiles.printFiles(fileList);
                System.out.println("");
            } // Vai mostrar todos os elementos na lista temporaria caso ja tenham algum
            else if (i.equals(".lista")) {
                if (tempFileList.isEmpty()) {
                    System.out.println("\n- A lista ainda está vazia no momento.\n");
                } else {
                    System.out.println("\nArquivos na lista: ");
                    ReadFiles.printFiles(tempFileList);
                    System.out.println("");
                }
            }
            // Se for diferente da palavra '.sair' e das demais ele entende que deveria ser
            // o nome de uma arquivo e que o nome está errado
            else if (!i.equals(".sair")) {
                System.out.println("\n- Arquivo não encontrado.\n");
            }
        } // Executa até o usuário desejar sair
        while (!i.equals(".sair"));

        // Se no final a lista não for vazia vai executar a geração dos grafos dos
        // elementos desejados
        if (!tempFileList.isEmpty()) {

            startTimer = System.nanoTime();
            runGraph(tempFileList, graphObject);
            endTimer = System.nanoTime() - startTimer;

            clearPrompt();
            System.out.println("\n- Grafo(s) gerado com sucesso.");

            System.out.println(endTimer / 1000000000 + "s");

        } // Só limpa o console e volta para o menu inicial
        else {
            clearPrompt();
        }

    }

    public static void runGraph(List<FileDetails> fileList, Graph<String> graphObject) throws IOException {
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
                graphObject.addEdge(cleanText[i], cleanText[i + 1]);
            }

            // Se tanto o arqivo '.csv' e '.png' já existam eles são apenas substituidos
            // Da um print no grafo e gera a imagem do grafo como png

            graphObject.printGraph(fileDetails.getFilePath(), fileDetails.getFileName(), fileDetails.getGraphPath(),
                    "_graph");
            graphObject.printGraph(fileDetails.getFilePath(), fileDetails.getFileName(), fileDetails.getGraphPath(),
                    "_topics");
        }
    }

    public static void clearPrompt() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
