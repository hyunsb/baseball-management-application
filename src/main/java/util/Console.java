package util;

import java.util.Objects;
import java.util.Scanner;

public class Console {

    private static Scanner scanner;

    // Suppresses default constructor, ensuring non-instantiability
    private Console() {
    }

    private static Scanner getInstance() {
        if (Objects.isNull(scanner)) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }

    public static String readLine() {
        return getInstance().nextLine();
    }

    public static void close() {
        getInstance().close();
    }
}
