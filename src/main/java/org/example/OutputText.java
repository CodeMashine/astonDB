package org.example;

public enum OutputText {
    HELLO("Enter comand type. Create ,Read , Update , Delete , Exit. "),
    WHONEED("Enter needed pole. Person or Role : "),
    NEEDROLE("Need role? y/n : "),
    FULLNAME("Enter full name : "),
    AGE("Enter age : "),
    ROLEQESTION("Enter role : "),
    CONTINUEWORK("continue work ? y/n : ");


    private String text;

    private OutputText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
