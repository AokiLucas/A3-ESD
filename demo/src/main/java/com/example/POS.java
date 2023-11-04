package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class POS {
    public static void main(String[] args) throws IOException {
        String[] tokens = Tokenizacao(LoadText("demo\\models\\textoTeste.txt"));
        String[] posTags = POSTag(tokens);

        List<String> tokensL = new ArrayList<>(Arrays.asList(tokens));
        List<String> posL = new ArrayList<>(Arrays.asList(posTags));

        for (int i = posTags.length - 1; i >= 0; i--) {

            if (posL.get(i).startsWith("v")) {
                tokensL.remove(i);
                posL.remove(i);
            }
        }

        tokens = tokensL.toArray(new String[0]);
        posTags = posL.toArray(new String[0]);

        for (String string : posTags) {
            System.out.println(string);
        }
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

    static String[] POSTag(String[] tokenizedText) {
        String[] sampleSentence = tokenizedText;

        try (InputStream modelInputStream = new FileInputStream(new File("demo\\models\\pt-pos-maxent.bin"));) {
            POSModel posModel = new POSModel(modelInputStream);
            POSTaggerME posTaggerME = new POSTaggerME(posModel);

            String tags[] = posTaggerME.tag(sampleSentence);
            
            return tags;
        } catch (IOException e) {
            // Handle exceptions
            return null;
        }
    }
}
