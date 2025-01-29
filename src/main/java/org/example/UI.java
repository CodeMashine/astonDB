package org.example;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
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
            String input = scanner.nextLine();

            if (input.toLowerCase().equals(Comands.EXIT.getText())) {
                return;
            } else if (input.toLowerCase().equals(Comands.CREATE.getText())) {
                personCreator();
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
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


    private static void anotherRoleQuestion() throws SQLException {
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
