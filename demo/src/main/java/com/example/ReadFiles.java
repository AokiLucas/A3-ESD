package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadFiles {
    public static List<FileDetails> readTxtFiles(String folderPath) {
        List<FileDetails> fileList = new ArrayList<>();
        try {

            // Percorre a pasta e filtra por todos os arquivos .txt
            Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(p -> {
                        // Salva o nome dos arquivos sem a extensao
                        String fileNameWithoutExtension = p.getFileName().toString();
                        if (fileNameWithoutExtension.indexOf(".") > 0)
                            fileNameWithoutExtension = fileNameWithoutExtension.substring(0,
                                    fileNameWithoutExtension.lastIndexOf("."));

                        // Salva o path sem a extensao extensao
                        String filePathWithoutExtension = p.toString();
                        if (filePathWithoutExtension.indexOf(".") > 0)
                            filePathWithoutExtension = filePathWithoutExtension.substring(0,
                                    filePathWithoutExtension.lastIndexOf("."));

                        fileList.add(new FileDetails(fileNameWithoutExtension, filePathWithoutExtension));
                    });

            // Vai ordenar em ordem crescente com base no nome dos arquivos
            fileList.sort((fd1, fd2) -> {
                int num1 = Integer.parseInt(fd1.getFileName().replaceAll("\\D+", ""));
                int num2 = Integer.parseInt(fd2.getFileName().replaceAll("\\D+", ""));
                return Integer.compare(num1, num2);
            });

            return fileList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    public static void printFiles(List<FileDetails> fileList){
        for (FileDetails fileDetails : fileList) {
            System.out.println(fileDetails.getFileName());
        }
    }
}
