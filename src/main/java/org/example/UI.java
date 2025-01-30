package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class UI {
    public static Scanner scanner = new Scanner(System.in);
    private static HashMap<String, String> PersonData = new HashMap<>();

    static {
        PersonData.put("fullname", null);
        PersonData.put("age", null);
        PersonData.put("role", null);
    }

    public static void listen() {
        System.out.println(OutputText.HELLO.getText());

        try {
            String input = scanner.nextLine().toLowerCase();

            if (input.equals(Comands.EXIT.getText())) {
                return;
            } else if (input.equals(Comands.CREATE.getText())) {
                personCreator();
            } else if (input.equals(Comands.DELETE.getText())) {
                deleteHandler();
            } else if (input.equals(Comands.SHOW.getText())) {
                showHandler();
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static void showHandler() throws SQLException {
        ResultSet rs = DBWorker.getPersonWithRoles();
        printFromRS(rs);
    }

    private static void printFromRS(ResultSet rs) {
        try {
            while (rs.next()) {
                System.out.println("id : " + rs.getInt("id") + ". " + rs.getString("fullName") + " " + rs.getInt("age") + " age," + " has role " + rs.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteHandler() {
        System.out.println(OutputText.FULLNAME.getText());
        String input = scanner.nextLine();

    }

    private static void personCreator() throws SQLException {

        System.out.print(OutputText.FULLNAME.getText());
        PersonData.put("fullname", scanner.nextLine());

        System.out.print(OutputText.AGE.getText());
        PersonData.put("age", scanner.nextLine());

        System.out.print(OutputText.ROLEQESTION.getText());
        PersonData.put("role", scanner.nextLine());

        anotherRoleQuestion();
    }


    private static void anotherRoleQuestion() {
        System.out.print(OutputText.NEEDROLE.getText());

        String input = scanner.nextLine().toLowerCase();

        if (input.equals(Comands.YES.getText())) {
            System.out.print(OutputText.ROLEQESTION.getText());
            PersonData.compute("role", (_, roles) -> roles + " " + scanner.nextLine());
            anotherRoleQuestion();
        } else if (input.equals(Comands.NO.getText())) {
            DBWorker.addPerson(PersonData.get("fullname"), PersonData.get("age"), PersonData.get("role"));
            resetPersonData();
            listen();
        } else {
            anotherRoleQuestion();
        }
    }

    private static void resetPersonData() {
        PersonData.put("fullname", null);
        PersonData.put("age", null);
        PersonData.put("role", null);
    }


}
