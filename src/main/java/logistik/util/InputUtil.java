package logistik.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputUtil {
    private static Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

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

    public static double inputDouble(String ingfo) {
        System.out.println(ingfo + " : ");
        while (true) {
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
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

    public static LocalDateTime inputLocalDateTime(String ingfo) {
        System.out.println(ingfo + " (format: DD-MM-YYYY HH:mm) : ");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            try {
                return LocalDateTime.parse(input, DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Format tanggal atau waktu salah, Coba lagi ya.");
                System.out.println("Contoh format yang bener: 05-06-2025 17:00");
            }
        }
    }
}
