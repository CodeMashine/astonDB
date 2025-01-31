package org.example;

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

    public static void updatePerson(int id, String fullName, String age, String roles) throws SQLException {

        String personUpdateQuery = "UPDATE person " +
                "SET fullName = ?, age = ? " +
                "WHERE id = ?;";

        try (Connection conn = getConnection();
             PreparedStatement upadatePstmt = conn.prepareStatement(personUpdateQuery, PreparedStatement.RETURN_GENERATED_KEYS);) {

            resetPerson(id);
            upadatePstmt.setString(1, fullName);
            upadatePstmt.setInt(2, Integer.parseInt(age));
            upadatePstmt.setInt(3, id);


            upadatePstmt.executeUpdate();
//             createRole(roles);
            chainPersonRole(id, createRole(roles));
            System.out.println(id + "  updated");
        }
    }

    private static void resetPerson(int id) throws SQLException {
        try (Connection conn = getConnection();
        ) {
            String deleteQuery = "DELETE FROM person_roles WHERE person_id=?";
            PreparedStatement deletePstmt = conn.prepareStatement(deleteQuery);
            deletePstmt.setInt(1, id);
            deletePstmt.execute();
            deletePstmt.close();
        }
    }


    public static ResultSet getPerson(int id) throws SQLException {
        String query = "SELECT p.id, p.fullName, p.age, r.role " +
                "FROM person_roles pr " +
                "JOIN person p ON pr.person_id =p.id " +
                "JOIN roles r ON pr.role_id =r.id " +
                "WHERE p.id=" + id + ";";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
//       Если закрыть conn и stmt тут то получаетель результата метода ничего не получит,
//        так же если использовать try with resourses
//        conn.close();
//        stmt.close();
        return rs;
    }

    public static ResultSet getPersonWithRoles() throws SQLException {
        String query = "SELECT p.id, p.fullName, p.age, r.role " +
                "FROM person_roles pr " +
                "JOIN person p ON pr.person_id =p.id " +
                "JOIN roles r ON pr.role_id =r.id;";

        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
//         Если закрыть conn и stmt тут то получаетель результата метода ничего не получит,
//        так же если использовать try with resourses
//        conn.close();
//        stmt.close();
        return rs;
    }

    public static void addPerson(String fullName, String age, String roles) {
        try {
            int personId = createPerson(fullName, age);
            ArrayList<Integer> rolesIds = createRole(roles);
            chainPersonRole(personId, rolesIds);
            System.out.println("Person " + fullName + " successfully created");
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
        if (rolesIn == null) {
            return null;
        }
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
        ResultSet rs = pstmt.getGeneratedKeys();
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

    public static void deletePerson(int id) throws SQLException {
        Connection conn = getConnection();
        String deleteQueryFromPersonRoleTable = "delete from person_roles where person_id=?";
        String deleteQueryFromPersonTable = "delete from person where id=?";
        PreparedStatement pstmtPersonRoles = conn.prepareStatement(deleteQueryFromPersonRoleTable);
        PreparedStatement pstmtPerson = conn.prepareStatement(deleteQueryFromPersonTable);
        pstmtPersonRoles.setInt(1, id);
        pstmtPerson.setInt(1, id);
        pstmtPersonRoles.execute();
        pstmtPerson.execute();
        pstmtPersonRoles.close();
        pstmtPerson.close();
        conn.close();
        System.out.println("person " + id + " deleted.");
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:~/test", "peter", "");
    }


}
