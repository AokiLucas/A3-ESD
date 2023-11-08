package com.example;

import java.util.List;

public class teste {
    
    public static void main(String[] args) {
    
        // Ensure readTxtFiles is returning a List<FileDetails>
        List<FileDetails> fileList = ReadFiles.readTxtFiles("demo\\resumes");
    
        // Check if fileList is not null before iterating
        if (fileList != null) {
            for (FileDetails fileDetail : fileList) {
                System.out.println("File Name: " + fileDetail.getFileName());
                System.out.println("File Path: " + fileDetail.getFilePath());
            }
        } else {
            System.out.println("No files found.");
        }
    }
}
