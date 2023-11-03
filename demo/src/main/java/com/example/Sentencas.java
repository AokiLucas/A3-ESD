package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class Sentencas {
    public static void main(String[] args) {
        try (InputStream inputStream = new FileInputStream(
                new File("demo\\models\\pt-sent.bin"))) {
            SentenceModel sentenceModel = new SentenceModel(inputStream);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
            String sentences[] = sentenceDetector.sentDetect(loadText());
            for (String sentence : sentences) {
                System.out.println("[" + sentence + "]");
            }

        } catch (FileNotFoundException ex) {
            // Handle exceptions
        } catch (IOException ex) {
            // Handle exceptions
        }

    }

    static String loadText() throws IOException {
        Path filePath = Path.of("demo\\models\\essayWords.txt");

        String text = Files.readString(filePath);

        text = text.toLowerCase();

        // Remover URL's
        text = Pattern.compile("https?:\\/\\/.*?[\\s+]", Pattern.MULTILINE).matcher(text).replaceAll("");

        return text;
    }
}
