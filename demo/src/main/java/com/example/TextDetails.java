package com.example;

public class TextDetails {
    private String text;
    private String[] autores;

public TextDetails(String text, String[] autores){
    this.text = text;
    this.autores = autores;
}

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String[] getautores() {
        return autores;
    }
    public void setautores(String[] autores) {
        this.autores = autores;
    }

    
}
