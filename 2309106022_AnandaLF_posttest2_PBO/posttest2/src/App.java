import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App { private static Map<String, String> users = new HashMap<>();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Sistem Login & Registrasi Pembelian Tiket Konser Samarinda ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Pilih menu: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    register(scanner);
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan program ini.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        } while (choice != 3);

        scanner.close();
    }

    private static void register(Scanner scanner) {
        System.out.print("Masukkan username: ");
        String username = scanner.nextLine();
        System.out.print("Masukkan password: ");
        String password = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username sudah terdaftar, silakan gunakan username lain.");
        } else {
            users.put(username, password);
            System.out.println("Registrasi berhasil! Silakan login.");
        }
    }

    private static void login(Scanner scanner) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Masukkan username: ");
            String username = scanner.nextLine();
            System.out.print("Masukkan password: ");
            String password = scanner.nextLine();

            if (users.containsKey(username) && users.get(username).equals(password)) {
                System.out.println("Login berhasil! Selamat datang, " + username + "!");
                return;
            } else {
                attempts++;
                System.out.println("Username atau password salah. Percobaan ke-" + attempts + "/3");
            }
        }
        System.out.println("Anda telah mencoba login 3 kali. Akses ditolak.");
    }
}
