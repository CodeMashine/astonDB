package org.example;

public enum OutputText {
    HELLO("Enter command type. Create ,Read , Update , Delete , Show , Exit. "),
    FULLNAME("Enter full name : "),
    ID("Enter id  : "),
    IDDELETE("Enter the id of the object to be deleted  : "),
    AGE("Enter age : "),
    ROLEQESTION("Enter role : "),
    NEEDROLE("Need another role ? y/n : "),
    ENTERNUMBER("Please enter number "),
    ANOTHERPERSON("Create another person ? y/n : ");


    private String text;

    private OutputText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
