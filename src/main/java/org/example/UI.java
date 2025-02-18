package org.example;

import javax.management.relation.RoleStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class UI {
    public static Scanner scanner = new Scanner(System.in);
    private static HashMap<String, String> PersonData = new HashMap<>();

    static {
        PersonData.put("fullname", null);
        PersonData.put("age", null);
        PersonData.put("role", null);
    }

    // Прослушивание консоли
    // в зависимости от ответа запускаем тот или иной обработчик
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
            e.printStackTrace();
        }
    }


    // обновляем запись в Person , Roles , Person_Roles
    private static void updateHandler() throws SQLException {
        System.out.print(OutputText.ID.getText());
        int id = Integer.parseInt(scanner.nextLine());
        personCreator();
        anotherRoleQuestion();
        DBWorker.updatePerson(id, PersonData.get("fullName"), PersonData.get("age"), PersonData.get("role"));
        resetPersonData();
    }

    // Чтение записи по Id
    private static void readHandler() {
        System.out.print(OutputText.ID.getText());
        try {
            String input = scanner.nextLine();
            if (input.equals(Comands.EXIT.getText())) {
                return;
            }

            int id = Integer.parseInt(input);
            Person person = HiberWorker.getPersonForId(id);
            printFromHiber(person);
            listen();

        } catch (NumberFormatException e) {
            System.out.println(OutputText.ENTERNUMBER.getText());
            deleteHandler();
        }
    }


    // Чтение всех записеи
    private static void showHandler() throws SQLException {
        List<Person> personList = HiberWorker.getAllPersons();
        personList.forEach(person -> {
            printFromHiber(person);
        });
    }


    // Вспомогательный метод вывод на печать
    private static void printFromHiber(Person person) {
        System.out.println(person.toString() + ", roles : " + person.getRoles());
    }

    // Удаление записи по id
    private static void deleteHandler() {
        System.out.print(OutputText.IDDELETE.getText());
        try {
            String input = scanner.nextLine();
            if (input.equals(Comands.EXIT.getText())) {
                return;
            }
            int id = Integer.parseInt(input);
            HiberWorker.deletePerson(id);

        } catch (NumberFormatException e) {
            System.out.println(OutputText.ENTERNUMBER.getText());
            deleteHandler();
        }
    }


    // Создание записи в Person , Roles , Person_Roles
    private static void createHandler() throws SQLException {
        personCreator();
        anotherRoleQuestion();
        HiberWorker.addPerson(PersonData.get("fullname"), PersonData.get("age"), PersonData.get("role"));
        resetPersonData();
    }

    // Вопросы и запись в хранилище Имени , Возраста и Роли
    private static void personCreator() throws SQLException {
        System.out.print(OutputText.FULLNAME.getText());
        PersonData.put("fullname", scanner.nextLine());

        System.out.print(OutputText.AGE.getText());
        PersonData.put("age", scanner.nextLine());

        System.out.print(OutputText.ROLEQESTION.getText());
        PersonData.put("role", scanner.nextLine());
    }

    // Серия рекурсивных вопросов случае необходимости нескольких ролей
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

    // Сброс хранища данных
    private static void resetPersonData() {
        PersonData.put("fullname", null);
        PersonData.put("age", null);
        PersonData.put("role", null);
    }
}
