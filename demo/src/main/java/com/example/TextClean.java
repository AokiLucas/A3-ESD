package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lemport.lemma.Lemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class TextClean {

    public TextClean(String path) throws IOException {
        // String text = LoadText(path);
        // text = Regex(text);
        // text = StopWords(text);
        // Lematizacao(Tokenizacao(text), POSTagger(text));
    }

    static String LoadText(String path) throws IOException {
        Path filePath = Path.of(path);

        String text = Files.readString(filePath);

        text = text.toLowerCase();
        // Remover URL's
        String pattern = ("((http|https)://)(www.)?[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)");
        text = Pattern.compile(pattern, Pattern.MULTILINE).matcher(text).replaceAll("");

        return text;
    }

    static String Regex(String text) {

        String pattern;

        // Tira pontuações menos "." para ser possível fazer a separação por frase
        pattern = "([\\p{P}\\p{S}&&[^-]])";
        text = Pattern.compile(pattern, Pattern.MULTILINE).matcher(text).replaceAll("");

        // Tira digitos
        pattern = "\\d";
        text = Pattern.compile(pattern, Pattern.MULTILINE).matcher(text).replaceAll("");

        return text;
    }

    static String[] Tokenizacao(String rawText) {
        try (InputStream modelInputStream = new FileInputStream(new File("demo\\models\\pt-token.bin"))) {
            TokenizerModel tokenizerModel = new TokenizerModel(modelInputStream);
            TokenizerME tokenizer = new TokenizerME(tokenizerModel);

            String[] tokens = tokenizer.tokenize(rawText);
            return tokens;

        } catch (FileNotFoundException e) {
            // Handle exception
            return null;
        } catch (IOException e) {
            // Handle exception
            return null;
        }
    }

    static String StopWords(String text) throws IOException {
        Path filePath = Path.of("demo\\models\\stopwords.txt");
        List<String> tokenizedText = Arrays.asList(Tokenizacao(text));

        List<String> sotpWords = Files.readAllLines(filePath);

        // Converte o texto para lowerCase
        tokenizedText = tokenizedText.stream().map(line -> line).collect(Collectors.toList());

        // Remove todas as palavras em stopWords
        tokenizedText.removeAll(sotpWords);

        String result = tokenizedText.stream().map(n -> String.valueOf(n))
                .collect(Collectors.joining(" ", "", ""));

        return result;
    }

    static String[] POSTagger(String[] tokens) {
        try (InputStream modelInputStream = new FileInputStream(new File("demo\\models\\pt-pos-maxent.bin"));) {
            POSModel posModel = new POSModel(modelInputStream);
            POSTaggerME posTaggerME = new POSTaggerME(posModel);

            String tags[] = posTaggerME.tag(tokens);

            return tags;
        } catch (IOException e) {
            // Handle exceptions
            return null;
        }
    }

    static String[] Lematizacao(String[] tokenizedText, String[] tagedText) {
        String[] tokens = tokenizedText;
        String[] tags = tagedText;

        List<String> tokensL = new ArrayList<>(Arrays.asList(tokens));
        List<String> tagsL = new ArrayList<>(Arrays.asList(tags));

        final Lemmatizer lemmatizer;
        final String[] lemmas;
        try {

            for (int i = tagsL.size() - 1; i >= 0; i--) {

                if (tagsL.get(i).startsWith("v")) {
                    tokensL.remove(i);
                    tagsL.remove(i);
                }
            }

            tokens = tokensL.toArray(new String[0]);
            tags = tagsL.toArray(new String[0]);

            lemmatizer = new Lemmatizer();
            lemmas = lemmatizer.lemmatize(tokens, tags);

            // Retorna as palavras ascentuadas para uma forma sem elas
            for (int i = 0; i < lemmas.length; i++) {
                lemmas[i] = Normalizer.normalize(lemmas[i], Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                        .toLowerCase();
            }

            return lemmas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
