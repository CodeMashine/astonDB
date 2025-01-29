package org.example;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.ArrayList;


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
                    "role  VARCHAR(30)" + ")");

            stmt.execute("CREATE TABLE IF NOT EXISTS person_roles  (" +
                    "person_id INT," +
                    "role_id INT," +
                    "FOREIGN KEY (person_id) REFERENCES person(id)," +
                    "FOREIGN KEY (role_id) REFERENCES roles(id)," +
                    "PRIMARY KEY (person_id, role_id)" +
                    ")");

            stmt.close();
            System.out.println("Таблицы созданы.");
        }
        conn.close();
    }

    public static void addPerson(String fullName, String age, String roles) {
        try {
            int personId = createPerson(fullName, age);
            ArrayList<Integer> rolesIds = createRole(roles);
            chainPersonRole(personId, rolesIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static int createPerson(String fullName, String age) throws SQLException {

        String query = "insert into person (fullName , age) values (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);) {
            pstmt.setString(1, fullName);
            pstmt.setInt(2, Integer.valueOf(age));
            int res = getId(pstmt);
            return res;
        }

    }


    public static ArrayList<Integer> createRole(String rolesIn) throws SQLException {
        String query = "insert into roles (role) values (?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);) {
            ArrayList<Integer> rolesIds = new ArrayList<>();

            String[] roles = rolesParser(rolesIn);

            for (String role : roles) {
                int checkRoleResult = checkRoleInDB(conn, role);
                if (checkRoleResult != 0) {
                    rolesIds.add(checkRoleResult);
                } else {
                    pstmt.setString(1, role);
                    int res = getId(pstmt);
                    rolesIds.add(res);
                }
            }
            return rolesIds;
        }
    }

    private static int checkRoleInDB(Connection conn, String role) throws SQLException {
        String query = "select id from roles WHERE role = ?";
        int id = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            id = rs.getInt("id");
        } finally {
            return id;
        }
    }

    private static String[] rolesParser(String roles) {
        return roles.split(" ");
    }


    private static int getId(PreparedStatement pstmt) throws SQLException {
        pstmt.executeUpdate();
//        int id = 0;
        ResultSet rs = pstmt.getGeneratedKeys();
//        if (rs.next()) {
//            id = rs.getInt(1);
//        }
        rs.next();
        return pstmt.getGeneratedKeys().getInt("id");
    }

    private static void chainPersonRole(int personId, ArrayList<Integer> roleId) throws SQLException {
        Connection conn = getConnection();
        String query = "insert into person_roles (person_Id , role_Id) values (? , ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(query);) {
            pstmt.setInt(1, personId);

            for (Integer integer : roleId) {
                pstmt.setInt(2, integer);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
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
