import java.util.ArrayList;
import java.util.Scanner;

public class App {
    // Superclass
    static class User {
        private String username;
        private String password;
        protected String role;  // admin atau user

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
        public String getUsername() { return username; }
        public boolean checkPassword(String pw) { return password.equals(pw); }
    }

    // Subclass Admin
    static class AppAdmin extends User {
        public AppAdmin(String username, String password) {
            super(username, password, "admin");
        }
        public void accessPanel() throws Exception {
            // panggil Admin.main dari file Admin.java
            Admin.main(new String[]{});
        }
    }

    // Subclass Customer
    static class Customer extends User {
        private int saldo;
        public Customer(String username, String password, int saldo) {
            super(username, password, "user");
            this.saldo = saldo;
        }
        public int getSaldo() { return saldo; }
        public void topUp(int amount) {
            if (amount > 0) {
                saldo += amount;
                System.out.println("Top-up berhasil. Saldo: " + saldo);
            }
        }
    }

    private static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        // seed akun
        users.add(new AppAdmin("admin","admin123"));
        users.add(new Customer("user","user123",100_000));

        int choice;
        do {
            System.out.println("\n=== SISTEM TIKET KONSER SAMARINDA ===");
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
            System.out.println("3. Logout");
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
                System.out.println("Logout Customer.");
                break;
            default:
                System.out.println("Invalid.");
            }
        } while(m!=3);
    }
}
