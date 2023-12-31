package com.example.Class;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.example.Objects.FileObject;

public class ReadFiles {
    public static List<FileObject> readTxtFiles(String folderPath) {
        List<FileObject> fileList = new ArrayList<>();
        try {

            // Percorre a pasta e filtra por todos os arquivos .txt
            Files.walk(Paths.get(folderPath)) // O(n)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".txt"))
                    .forEach(p -> {
                        // Salva o nome dos arquivos sem a extensao O(n)
                        String fileNameWithoutExtension = p.getFileName().toString();
                        if (fileNameWithoutExtension.indexOf(".") > 0)
                            fileNameWithoutExtension = fileNameWithoutExtension.substring(0,
                                    fileNameWithoutExtension.lastIndexOf("."));

                        // Salva o path sem a extensao extensao O(n)
                        String filePathWithoutExtension = p.toString();
                        if (filePathWithoutExtension.indexOf(".") > 0)
                            filePathWithoutExtension = filePathWithoutExtension.substring(0,
                                    filePathWithoutExtension.lastIndexOf("."));

                        fileList.add(
                                new FileObject(fileNameWithoutExtension, filePathWithoutExtension, "demo\\graphs"));
                    }); // O(n)

            // Vai ordenar em ordem crescente com base no nome dos arquivos
            fileList.sort((fd1, fd2) -> {
                int num1 = Integer.parseInt(fd1.getFileName().replaceAll("\\D+", ""));
                int num2 = Integer.parseInt(fd2.getFileName().replaceAll("\\D+", ""));
                return Integer.compare(num1, num2);
            }); // O(nlogn)

            return fileList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    } //O(nlogn)

    // Da um print nos arquivos existentes dentro da pasta
    public static void printFiles(List<FileObject> fileList) {
        for (FileObject fileDetails : fileList) {
            System.out.println(fileDetails.getFileName());
        } // O(n)
    }
}
