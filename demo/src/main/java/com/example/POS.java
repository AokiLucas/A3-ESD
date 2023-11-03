package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class POS {
    public static void main(String[] args) {
        String[] sampleSentence = Tokenizer();

        try (InputStream modelInputStream = new FileInputStream(new File("demo\\models\\pt-pos-maxent.bin"));) {
            POSModel posModel = new POSModel(modelInputStream);
            POSTaggerME posTaggerME = new POSTaggerME(posModel);

            String tags[] = posTaggerME.tag(sampleSentence);
            for (int i = 0; i < sampleSentence.length; i++) {
                // System.out.println(tags[i]);
                System.out.println(sampleSentence[i] + " - " + tags[i] + " ");
            }
            System.out.println();
        } catch (IOException e) {
            // Handle exceptions
        }

    }

    static String[] Tokenizer() {
        String tokenList[];
        String sampleText = "recomponha-se , recomponhase . falo falarei falasse falou ";

        String pattern = "(\\s+)|([\\p{P}\\p{S}&&[^.-]])";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(sampleText);

        sampleText = m.replaceAll("");

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
}
