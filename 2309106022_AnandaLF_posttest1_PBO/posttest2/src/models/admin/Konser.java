package models.admin;
import java.util.*;
import java.util.ArrayList;
import java.util.List;


public class konser {
    private List<Concert> konser = new ArrayList<>();

    public void managekonser(Scanner scanner, Scanner scannerStr) {
        int option;
        do {
            System.out.println("\n=== Manage konser ===");
            System.out.println("1. Add Concert");
            System.out.println("2. View konser");
            System.out.println("3. Remove Concert");
            System.out.println("4. Back to Admin Panel");
            System.out.print("Enter your choice: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.print("Enter Concert Name: ");
                    String name = scannerStr.nextLine();
                    System.out.print("Enter Date: ");
                    String date = scannerStr.nextLine();
                    konser.add(new Concert(name, date));
                    System.out.println("Concert added successfully!");
                    break;
                case 2:
                    System.out.println("\nList of konser:");
                    for (Concert c : konser) {
                        System.out.println(c);
                    }
                    break;
                case 3:
                    System.out.print("Enter Concert Name to Remove: ");
                    String removeName = scannerStr.nextLine();
                    konser.removeIf(concert -> concert.getName().equalsIgnoreCase(removeName));
                    System.out.println("Concert removed successfully!");
                    break;
                case 4:
                    System.out.println("Returning to Admin Panel...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (option != 4);
    }
}