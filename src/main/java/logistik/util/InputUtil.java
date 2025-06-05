package logistik.util;

import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);

    public static int inputInt(String ingfo) {
        System.out.println(ingfo + " : ");
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            }catch (NumberFormatException e) {
                System.out.println("Input harus angka, ulang!");
            }
        }
    }

    public static String inputString(String ingfo) {
        System.out.println(ingfo + " : ");
        return scanner.nextLine();
    }

    public static boolean inputBoolean(String ingfo) {
        System.out.println(ingfo + " (y/n)  : ");
        String input = scanner.nextLine().toLowerCase();
        return input.equals("y") || input.equals("yes") || input.equals("ya");
//        return input.equals("y") ? true : false;
    }
}
