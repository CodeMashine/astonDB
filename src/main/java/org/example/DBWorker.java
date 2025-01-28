package org.example;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;


public class DBWorker {
    public static void createTables() throws SQLException {
        Connection conn = getConnection();
        try (conn) {
            Statement stmt = conn.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS person (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "fullName VARCHAR(50)," +
                    "age INT" + ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS roles  (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "role VARCHAR(30)" + ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS person_roles  (" +
                    "person_id INT," +
                    "role_id INT," +
                    "FOREIGN KEY (person_id) REFERENCES person(id)," +
                    "FOREIGN KEY (role_id) REFERENCES roles(id)," +
                    "PRIMARY KEY (person_id, role_id)" +
                    ")");


            System.out.println("Таблицы созданы.");
        }
        conn.close();
    }

    public static void addPerson(String fullName, int age) throws SQLException {
        Connection conn = getConnection();
        String query = "insert into person (fullName , age) values (?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setString(1, fullName);
        pstmt.setInt(2, age);

        pstmt.execute();
        conn.close();
    }


    public static void testing() throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from cars");

        while (rs.next()) {
            System.out.println(rs.getString("id") + " " + rs.getString("brand") + " "
                    + rs.getString("model") + " " + rs.getDate("date_of_Manufactor") + " "
                    + rs.getInt("power"));
        }

//        ResultSetMetaData rsmd = rs.getMetaData();
//
//        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
//            System.out.println(rsmd.getColumnName(i));
//            System.out.println(rsmd.getColumnClassName(i));
//
//        }


        conn.close();
    }

    public static void deletePerson(int id) throws SQLException {
        Connection conn = getConnection();
        String query = "delete from person where id=?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, id);

        pstmt.execute();
        pstmt.close();
        conn.close();
        System.out.println("car " + id + " deleted.");
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:~/test", "peter", "");
    }


}
