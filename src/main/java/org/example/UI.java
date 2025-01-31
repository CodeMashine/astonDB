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
                createHandler();
                listen();
            } else if (input.equals(Comands.DELETE.getText())) {
                deleteHandler();
                showHandler();
                listen();
            } else if (input.equals(Comands.SHOW.getText())) {
                showHandler();
                listen();
            } else if (input.equals(Comands.READ.getText())) {
                readHandler();
                listen();
            } else if (input.equals(Comands.UPDATE.getText())) {
                updateHandler();
                listen();
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static void updateHandler() throws SQLException {
        System.out.print(OutputText.ID.getText());
        int id = Integer.parseInt(scanner.nextLine());
        personCreator();
        anotherRoleQuestion();
        DBWorker.updatePerson(id, PersonData.get("fullName"), PersonData.get("age"), PersonData.get("role"));
        resetPersonData();
    }


    private static void readHandler() {
        System.out.print(OutputText.ID.getText());
        try {
            String input = scanner.nextLine();
            if (input.equals(Comands.EXIT.getText())) {
                return;
            }

            int id = Integer.parseInt(input);
            ResultSet rs = DBWorker.getPerson(id);
            printFromRS(rs);

        } catch (NumberFormatException e) {
            System.out.println(OutputText.ENTERNUMBER.getText());
            deleteHandler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void showHandler() throws SQLException {
        ResultSet rs = DBWorker.getPersonWithRoles();
        printFromRS(rs);
        rs.close();
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
        System.out.println(OutputText.IDDELETE.getText());
        try {
            String input = scanner.nextLine();
            if (input.equals(Comands.EXIT.getText())) {
                return;
            }
            int id = Integer.parseInt(input);
            DBWorker.deletePerson(id);

        } catch (NumberFormatException e) {
            System.out.println(OutputText.ENTERNUMBER.getText());
            deleteHandler();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void createHandler() throws SQLException {
        personCreator();
        anotherRoleQuestion();
        DBWorker.addPerson(PersonData.get("fullname"), PersonData.get("age"), PersonData.get("role"));
        resetPersonData();
    }


    private static void personCreator() throws SQLException {

        System.out.print(OutputText.FULLNAME.getText());
        PersonData.put("fullname", scanner.nextLine());

        System.out.print(OutputText.AGE.getText());
        PersonData.put("age", scanner.nextLine());

        System.out.print(OutputText.ROLEQESTION.getText());
        PersonData.put("role", scanner.nextLine());
    }


    private static void anotherRoleQuestion() {
        System.out.print(OutputText.NEEDROLE.getText());

        String input = scanner.nextLine().toLowerCase();

        if (input.equals(Comands.YES.getText())) {
            System.out.print(OutputText.ROLEQESTION.getText());
            PersonData.compute("role", (_, roles) -> roles + " " + scanner.nextLine());
            anotherRoleQuestion();
        } else if (input.equals(Comands.NO.getText())) {
            return;
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
