package models;

import java.util.*;
import AdminPanel;

public class App {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final Map<String, String> users = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Sistem Pembelian Tiket Konser Kota Samarinda ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Pilih pada menu: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    if (login(scanner)) {
                        System.out.println("Login sukses!...");
                    } else {
                        System.out.println("Login Invalid! Coba Lagi.");
                    }
                    break;
                case 2:
                    registerUser(scanner);
                    break;
                case 3:
                    System.out.println("Keluar dari sistem...");
                    break;
                default:
                    System.out.println("Salah Pilih! Coba Lagi.");
            }
        } while (choice != 3);

        scanner.close();
    }

    private static boolean login(Scanner scanner) {
        System.out.print("Masukkan Username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan Password: ");
        String password = scanner.nextLine();

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            System.out.println("Selamat Datang Admin!");
            return true;
        } else if (users.containsKey(username) && users.get(username).equals(password)) {
            System.out.println("Selamat Datang, " + username + "!");
            return true;
        } else {
            return false;
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Masukkan Username Baru: ");
        String username = scanner.nextLine();
        if (users.containsKey(username)) {
            System.out.println("Username sudah ada! Coba lagi.");
            return;
        }
        System.out.print("Masukkan Password Baru: ");
        String password = scanner.nextLine();
        users.put(username, password);
        System.out.println("Regist sukses! Bisa Login ya.");
    }
}
