package org.example;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class UI {
    public static Scanner scanner = new Scanner(System.in);
    private static HashMap<String, String> PersonData = new HashMap<>();
    //    private String age = "age";
//    private String role = "role";
//    private String FULLNAME = "fullname";
//    private String AGE = "age";
//    private String ROLE = "role";

    static {

        PersonData.put("fullname", null);
        PersonData.put("age", null);
        PersonData.put("role", null);

    }

    public static void listen() {
        System.out.println(OutputText.HELLO.getText());

//        Scanner scanner = new Scanner(System.in);

//        while (true) {

        try {
            String input = scanner.nextLine();

            if (input.toLowerCase().equals(Comands.EXIT.getText())) {
                return;
            } else if (input.toLowerCase().equals(Comands.CREATE.getText())) {
                personRoleSwitcher();
            }
//            System.out.println(input);
//            ComandExecutor.execute(input);
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
//        }
    }

    private static void personRoleSwitcher() throws SQLException {
        System.out.println(OutputText.WHONEED.getText());
        if (scanner.nextLine().equals(Comands.PERSON.getText())) {
            personCreator();
        }
    }

    private static void personCreator() throws SQLException {

        System.out.print(OutputText.FULLNAME.getText());
        PersonData.put("fullname", scanner.nextLine());
        System.out.print(OutputText.AGE.getText());
        PersonData.put("age", scanner.nextLine());
        System.out.print(OutputText.NEEDROLE.getText());

        if (scanner.nextLine().toLowerCase().equals(Comands.YES.getText())) {
            roleCreator();
        }

        Boolean res = DBWorker.addPerson(PersonData.get("fullname"), PersonData.get("age"));

        System.out.println(res ? "something goes wrong" : ("Person added: full name " + PersonData.get("fullname") + ", age :" + PersonData.get("age")));
        
        listen();

    }

    private static void roleCreator() {
        System.out.print(OutputText.ROLEQESTION.getText());
        if (scanner.nextLine().toLowerCase().equals(Comands.YES.getText())) {
            System.out.println(OutputText.ROLEQESTION.getText());

        }

    }


}
