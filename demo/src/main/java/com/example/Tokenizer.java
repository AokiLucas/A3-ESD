package com.example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokenizer {
    public static void main(String[] args) {
        String sampleText = "Este é um teste de tokenização de pavras usando-se o opneNLP";
        try (InputStream modelInputStream = new FileInputStream(new File("demo\\models\\pt-token.bin"))) {
            TokenizerModel tokenizerModel = new TokenizerModel(modelInputStream);
            TokenizerME tokenizer = new TokenizerME(tokenizerModel);

            String tokenList[] = tokenizer.tokenize(sampleText);
            for (String token : tokenList) {
                System.out.println(token);
            }

        } catch (FileNotFoundException e) {
            // Handle exception
        } catch (IOException e) {
            // Handle exception
        }

    }

}
