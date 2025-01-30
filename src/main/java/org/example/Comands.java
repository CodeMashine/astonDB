package org.example;

public enum Comands {
    CREATE("create"),
    PERSON("person"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete"),
    EXIT("exit"),
    //    SHOWALL("show all"),
    SHOW("show"),
    NO("n"),
    YES("y");

    private String text;

    private Comands(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    ;

}
