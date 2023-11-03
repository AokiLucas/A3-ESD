package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TesteStopW {

    public static void main(String[] args) throws IOException {
        Path filePath = Path.of("demo\\models\\stopwords.txt");
        Path filePath2 = Path.of("demo\\models\\essayWords.txt");

        List<String> sotpWords = Files.readAllLines(filePath);
        List<String> essayLines = Files.readAllLines(filePath2);

        List<String> essayWords = splitLinesToWords(essayLines);

        System.out.println("Antes da limpeza:" + essayWords.size());

        //Converte o texto para lowerCase
        essayWords = essayWords.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());
        
        //Remove todas as palavras em stopWords
        essayWords.removeAll(sotpWords);

        System.out.println("Depois da limpeza:" + essayWords.size());

        System.out.println(essayWords);
    }

    private static List<String> splitLinesToWords(List<String> essayLines) {

        List<String> essayWords = new ArrayList<>();

        for (String line : essayLines) {
            List<String> words = Arrays.asList(line.split(" "));
            essayWords.addAll(words);
        }

        return essayWords;
    }
}
