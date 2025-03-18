package user;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Tiket {
    private static final Map<String, String> users = new HashMap<>();

    public static boolean login(Scanner scanner) {
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        return users.containsKey(username) && users.get(username).equals(password);
    }

    public static void registerUser(Scanner scanner) {
        System.out.print("Enter new Username: ");
        String username = scanner.nextLine();
        if (users.containsKey(username)) {
            System.out.println("Username already exists! Try again.");
            return;
        }
        System.out.print("Enter new Password: ");
        String password = scanner.nextLine();
        users.put(username, password);
        System.out.println("Registration successful! You can now log in.");
    }
}
