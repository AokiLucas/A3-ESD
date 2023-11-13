package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TesteGraph {
    public static void main(String[] args) throws IOException {
        // Classe grafo
        Graph<String> graphObject = new Graph<>();

        // Classe dos arquivos
        // Incializar dando a pasta onde os arquivos estão
        String folderPath = "demo\\resumes";
        List<FileDetails> fileList = ReadFiles.readTxtFiles(folderPath);

        Scanner scanner = new Scanner(System.in);
        int number;

        System.out.println("\nArquivos encotrados na pasta '" + folderPath + "': ");
        ReadFiles.printFiles(fileList);

        do {
            System.out.println("\nDigite: \n" +
                    "1 - Exibir lista de arquivos na pasta.\n" +
                    "2 - Para gerar o grafo de todos os arquivos.\n" +
                    "3 - Para escolher os arquivos a gerar o grafo.\n" +
                    "0 - Para sair. \n");

            number = scanner.nextInt();

            switch (number) {
                case 1:
                    System.out.println("\nArquivos encotrados na pasta '" + folderPath + "': ");
                    ReadFiles.printFiles(fileList);
                    break;

                case 2:
                    runGraph(fileList, graphObject);
                    clearPrompt();
                    System.out
                            .println(
                                    "\nTodos os grafos foram gerados em uma pasta com o nome do próprio arquivo '.txt'");
                    break;
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
                "\nDigite o nome do arquivo que deseja e aperte 'enter'.\nApós digitar escreva 'sair' quando quiser parar.\n");

        String i;
        List<FileDetails> tempFileList = new ArrayList<>();
        do {
            i = scanner.nextLine().toLowerCase();
            FileDetails foundFile = null;

            for (FileDetails fileDetails : fileList) {
                if (fileDetails.getFileName().equals(i)) {
                    foundFile = fileDetails;
                    break;
                }
            }

            if (foundFile != null) {
                boolean alreadyExists = false;

                for (FileDetails tempFileDetail : tempFileList) {
                    if (tempFileDetail.getFileName().equals(i)) {
                        alreadyExists = true;
                        System.out.println("\nArquivo ja adicionado\n");
                        break;
                    }
                }

                if (!alreadyExists) {
                    tempFileList.add(foundFile);
                    System.out.println("\nArquivo adicionado\n");
                }
            }

        } while (!i.equals("sair"));

        runGraph(tempFileList, graphObject);
        clearPrompt();
        System.out.println("\nGrafo(s) gerado com sucesso.");

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
            graphObject.printGraph(fileDetails.getFilePath(), fileDetails.getFileName());
        }
    }

    public static void clearPrompt() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
