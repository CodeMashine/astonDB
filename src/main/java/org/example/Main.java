package org.example;

import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        work();
    }

    static void work() {
        HiberWorker.init();
        UI.listen();
    }
}