import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    static class User {
        private String username;
        private String password;
        protected String role; 

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
        public String getUsername() {
            return username;
        }
        public boolean checkPassword(String pw) {
            return password.equals(pw);
        }
        public void displayInfo() {
            System.out.println("User: " + username);
        }
    }

    static class AppAdmin extends User {
        public AppAdmin(String username, String password) {
            super(username, password, "admin");
        }
        public void accessPanel() throws Exception {
            Admin.main(new String[]{});
        }
        @Override
        public void displayInfo() {
            System.out.println("[ADMIN] Username: " + getUsername());
        }
    }

    static class Customer extends User {
        private int saldo;
        public Customer(String username, String password, int saldo) {
            super(username, password, "user");
            this.saldo = saldo;
        }
        public int getSaldo() { 
            return saldo; 
        }
        public void topUp(int amount) {
            if (amount > 0) {
                saldo += amount;
                System.out.println("Top-up berhasil. Saldo: " + saldo);
            }
        }
        public boolean kurangiSaldo(int amount) {
            if(amount <= saldo) {
                saldo -= amount;
                return true;
            }
            return false;
        }
        @Override
        public void displayInfo() {
            System.out.println("[CUSTOMER] Username: " + getUsername() + ", Saldo: " + saldo);
        }
    }

    private static ArrayList<User> users = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        users.add(new AppAdmin("admin","admin123"));
        users.add(new Customer("user","user123",100_000));

        int choice;
        do {
            System.out.println("\n=== SELAMAT DATANG DI PEMESANAN TIKET KONSER SAMARINDA ===");
            System.out.println("1. Login");
            System.out.println("2. Register (Customer)");
            System.out.println("3. Exit");
            System.out.print("Pilih: ");
            choice = sc.nextInt(); sc.nextLine();

            switch(choice) {
            case 1: loginFlow(sc); break;
            case 2: registerFlow(sc); break;
            case 3: System.out.println("Keluar program."); break;
            default: System.out.println("Pilihan invalid.");
            }
        } while(choice!=3);
        sc.close();
    }

    private static void loginFlow(Scanner sc) throws Exception {
        System.out.print("Username: ");
        String u = sc.nextLine();
        System.out.print("Password: ");
        String p = sc.nextLine();

        for (User usr: users) {
            if (usr.getUsername().equals(u) && usr.checkPassword(p)) {
                System.out.println("Login berhasil sebagai " + usr.role);
                if (usr instanceof AppAdmin) {
                    ((AppAdmin)usr).accessPanel();
                } else {
                    customerMenu(sc, (Customer)usr);
                }
                return;
            }
        }
        System.out.println("Login gagal!");
    }

    private static void registerFlow(Scanner sc) {
        System.out.print("Username baru: ");
        String u = sc.nextLine();
        System.out.print("Password baru: ");
        String p = sc.nextLine();
        for (User usr: users) {
            if (usr.getUsername().equals(u)) {
                System.out.println("Username sudah ada!");
                return;
            }
        }
        Customer c = new Customer(u,p,0);
        users.add(c);
        System.out.println("Registrasi sukses. Saldo awal: " + c.getSaldo());
    }

    private static void customerMenu(Scanner sc, Customer c) {
        int m;
        do {
            System.out.println("\n-- MENU CUSTOMER ("+c.getUsername()+") --");
            System.out.println("1. Top-up Saldo");
            System.out.println("2. Lihat Saldo");
            System.out.println("3. Lihat Konser yang Tersedia");
            System.out.println("4. Pesan Tiket Konser");
            System.out.println("5. Lihat Riwayat Transaksi");
            System.out.println("6. Lihat Tiket yang Sudah Dibeli");
            System.out.println("7. Logout");
            System.out.print("Pilih: ");
            m = sc.nextInt(); sc.nextLine();

            switch(m) {
                case 1:
                    System.out.print("Jumlah top-up: ");
                    int amt = sc.nextInt(); sc.nextLine();
                    c.topUp(amt);
                    break;
                case 2:
                    System.out.println("Saldo Anda: " + c.getSaldo());
                    break;
                case 3:
                    tampilkanKonser();
                    break;
                case 4:
                    pesanTiket(sc, c);
                    break;
                case 5:
                    tampilkanTransaksiUser(c.getUsername());
                    break;
                case 6:
                    tampilkanTiketUser(c.getUsername());
                    break;
                case 7:
                    System.out.println("Logout Customer.");
                    break;
                default:
                    System.out.println("Invalid.");
            }
        } while(m!=7);
    }

    private static void tampilkanKonser() {
        try (BufferedReader br = new BufferedReader(new FileReader("database.txt"))) {
            System.out.println("\n| Hari/Tanggal | Konser | Lokasi | Tiket Tersedia | Deskripsi |");
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",", 6);
                System.out.printf("|    %-12s    | %-10s  | %-10s  |      %-14s     |     %s    |\n", f[1], f[2], f[3], f[4], f[5]);
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca konser.");
        }
    }

    private static void pesanTiket(Scanner sc, Customer c) {
        try (BufferedReader br = new BufferedReader(new FileReader("tiket.txt"))) {
            ArrayList<String> tiketList = new ArrayList<>();
            String line;
            int index = 1;
            System.out.println("\nDaftar Tiket:");
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",");
                if (f[3].equalsIgnoreCase("tersedia")) {
                    tiketList.add(line);
                    System.out.printf("%d. ID: %s | Konser: %s | Harga: %s\n", index++, f[0], f[1], f[2]);
                }
            }
            if (tiketList.isEmpty()) {
                System.out.println("Tidak ada tiket tersedia.");
                return;
            }
            System.out.print("Pilih tiket nomor: ");
            int pilih = sc.nextInt(); sc.nextLine();
            if (pilih < 1 || pilih > tiketList.size()) {
                System.out.println("Pilihan tidak valid.");
                return;
            }
            String[] t = tiketList.get(pilih-1).split(",");
            int harga = Integer.parseInt(t[2]);
            if (!c.kurangiSaldo(harga)) {
                System.out.println("Saldo tidak cukup.");
                return;
            }
            t[3] = c.getUsername();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("transaksi.txt", true))) {
                bw.write(UUID.randomUUID() + "," + c.getUsername() + "," + t[1] + ",1," + harga);
                bw.newLine();
            }
            File inputFile = new File("tiket.txt");
            File tempFile = new File("temp_tiket.txt");
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    if (currentLine.equals(tiketList.get(pilih-1))) {
                        writer.write(String.join(",", t));
                    } else {
                        writer.write(currentLine);
                    }
                    writer.newLine();
                }
            }
            inputFile.delete();
            tempFile.renameTo(inputFile);
            System.out.println("Tiket berhasil dipesan.");
        } catch (IOException e) {
            System.err.println("Gagal memesan tiket.");
        }
    }

    private static void tampilkanTransaksiUser(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("transaksi.txt"))) {
            System.out.println("\nRiwayat Transaksi:");
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",");
                if (f[1].equals(username)) {
                    System.out.printf("ID: %s | Konser: %s | Jumlah: %s | Total: %s\n",
                            f[0], f[2], f[3], f[4]);
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca transaksi.");
        }
    }

    private static void tampilkanTiketUser(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("tiket.txt"))) {
            System.out.println("\nTiket yang Dimiliki:");
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",");
                if (f[3].equals(username)) {
                    System.out.printf("ID: %s | Konser: %s | Harga: %s\n", f[0], f[1], f[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca tiket.");
        }
    }
}
