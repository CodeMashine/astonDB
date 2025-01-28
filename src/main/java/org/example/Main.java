package org.example;

import java.sql.Date;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        DBWorker.createTables();
        UI.listen();
//        DBWorker.testing();
    }
}