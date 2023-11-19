package com.example;

public class FileDetails {
    private String fileName;
    private String filePath;
    private String graphPath;

    public FileDetails(String fileName, String filePath, String graphPath) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.graphPath = graphPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getGraphPath() {
        return graphPath;
    }

    public void setGraphPath(String graphPath) {
        this.graphPath = graphPath;
    }
}
