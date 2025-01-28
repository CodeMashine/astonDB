package org.example;

public enum OutputText {
    HELLO("Hello. Enter comand type. Create ,Read , Update or Delete. "),
    FULLNAME("Enter full name : "),
    AGE("Enter age : "),
    ROLE("Enter role : "),
    CONTINUEWORK("continue work ? y/n : "),
    NO("n"),
    YES("n");


    private String OutputText;

    private OutputText(String OutputText) {
        this.OutputText = OutputText;
    }
}
