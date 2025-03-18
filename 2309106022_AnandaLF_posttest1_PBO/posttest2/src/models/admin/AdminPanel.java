package models.admin;

import java.util.*;
import java.util.Scanner;
import models.admin.konsermanajemen;
import models.admin.transaksimanajemen;

public class AdminPanel {
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    public static boolean login(Scanner scanner) {
        System.out.print("Masukkan Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan Admin Password: ");
        String password = scanner.nextLine();

        return ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scanner scannerStr = new Scanner(System.in);
        konsermanajemen concertManager = new konsermanajemen();
        transaksimanajemen transactionManager = new transaksimanajemen();
        int choice;

        do {
            System.out.println("\n=== Admin Panel ===");
            System.out.println("1. Konser");
            System.out.println("2. Transasksi");
            System.out.println("3. Kembali");
            System.out.print("Pilih Menu: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    concertManager.kelolakonser(scanner, scannerStr);
                    break;
                case 2:
                    transactionManager.viewTransactions();
                    break;
                case 3:
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 3);

        scanner.close();
        scannerStr.close();
    }
}