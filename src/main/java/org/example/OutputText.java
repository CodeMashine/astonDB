package org.example;

public enum OutputText {
    HELLO("Enter comand type. Create ,Read , Update , Delete , Exit. "),
    FULLNAME("Enter full name : "),
    AGE("Enter age : "),
    ROLEQESTION("Enter role : "),
    NEEDROLE("Need another role ? y/n : "),
    ANOTHERPERSON("Create another person ? y/n : ");


    private String text;

    private OutputText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
