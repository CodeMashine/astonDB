package org.example;

public enum Comands {
    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete"),
    EXIT("exit"),
    SHOWALL("show all"),
    SHOW("show");

    private String comand;

    private Comands(String comand) {
        this.comand = comand;
    }

}
