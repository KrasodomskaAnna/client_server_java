package org.example;

import java.io.Serializable;

public class Message implements Serializable {
    private int number;
    private String content;

    public int getNumber() {
        return number;
    }

    public String getContent() {
        return content;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
