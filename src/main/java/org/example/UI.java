package org.example;

import java.util.Scanner;

public class UI {

    public static void listen() {
        System.out.printf(OutputText.HELLO);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try (String input = scanner.nextLine();) {
                String flag = input.split(" ")[0];
                String command = input.split(" ")[1];

                if (input.equals(Comands.EXIT)) {
                    return;
                }else if (input.equals(Comands.CREATE)) {
                    ComandExecutor.create(input)
                }
                System.out.println(input);
                ComandExecutor.execute(input);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
