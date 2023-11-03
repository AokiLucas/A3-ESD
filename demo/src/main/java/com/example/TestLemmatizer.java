package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import lemport.lemma.*;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class TestLemmatizer {

    public static void main(final String[] args) {
        final String[] tokens = Tokenizer();
        final String[] tags = POS(tokens);

        final Lemmatizer lemmatizer;
        final String[] lemmas;
        try {
            lemmatizer = new Lemmatizer();
            lemmas = lemmatizer.lemmatize(tokens, tags);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final StringBuilder token = new StringBuilder();
        final StringBuilder lemma = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            token.append(tokens[i]).append("\t");
            lemma.append(lemmas[i]).append("\t");
        }
        System.out.println(token);
        System.out.println(lemma);
    }

    static String[] Tokenizer() {
        /*Path filePath = Path.of("demo\\models\\essayWords.txt");
        String sampleText = Files.readString(filePath);*/
        String tokenList[];
        String sampleText = "revolucao revolução revolucionario revolucionário";
        try (InputStream modelInputStream = new FileInputStream(new File("demo\\models\\pt-token.bin"))) {
            TokenizerModel tokenizerModel = new TokenizerModel(modelInputStream);
            TokenizerME tokenizer = new TokenizerME(tokenizerModel);

            tokenList = tokenizer.tokenize(sampleText);
            return tokenList;

        } catch (FileNotFoundException e) {
            // Handle exception
        } catch (IOException e) {
            // Handle exception
        }
        return null;
    }

    static String[] POS(String[] tokens){
        tokens = Tokenizer();
        String tags[];

        try (InputStream modelInputStream = new FileInputStream(new File("demo\\models\\pt-pos-perceptron.bin"));) {
            POSModel posModel = new POSModel(modelInputStream);
            POSTaggerME posTaggerME = new POSTaggerME(posModel);

            tags = posTaggerME.tag(tokens);
            for (int i = 0; i < tokens.length; i++) {
                //System.out.println(tags[i]);
                System.out.println(tokens[i] + " - " + tags[i] + " ");
            }
            System.out.println();

            return tags;
        } catch (IOException e) {
            // Handle exceptions
        }
return null ;
    }
}