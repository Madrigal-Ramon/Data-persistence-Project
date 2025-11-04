import java.util.*;
import java.io.*;

public class RestaurantOrderSystem {
    private ArrayList<String> pendingOrders;
    private String[] completedOrders;
    private int completedCount;
    private Scanner scanner;

    public RestaurantOrderSystem() {
        pendingOrders = new ArrayList<>();
        completedOrders = new String[50];
        completedCount = 0;
        scanner = new Scanner(System.in);
        loadData();
    }

    public static void main(String[] args) {
        RestaurantOrderSystem system = new RestaurantOrderSystem();
        system.run();
    }

    public void run() {
        System.out.println("=== Restaurant Order System ===");
        
        while (true) {
            showMenu();
            int choice = getNumberInput("Choose option: ");
            
            switch (choice) {
                case 1: addOrder(); break;
                case 2: viewPendingOrders(); break;
                case 3: viewCompletedOrders(); break;
                case 4: completeOrder(); break;
                case 5: 
                    saveData();
                    System.out.println("Goodbye!");
                    return;
                default: 
                    System.out.println("Invalid choice! Try again.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n1. Add Order");
        System.out.println("2. View Pending Orders");
        System.out.println("3. View Completed Orders");
        System.out.println("4. Complete Order");
        System.out.println("5. Exit");
    }

    // Recursive error checking for numbers
    private int getNumberInput(String message) {
        System.out.print(message);
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number!");
            return getNumberInput(message); // Recursive call
        }
    }

    private void addOrder() {
        System.out.print("Enter order description: ");
        String order = scanner.nextLine();
        
        if (order.trim().isEmpty()) {
            System.out.println("Order cannot be empty!");
            return;
        }
        
        pendingOrders.add(order);
        System.out.println("Order added!");
    }

    private void viewPendingOrders() {
        System.out.println("\n--- Pending Orders ---");
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders");
        } else {
            for (int i = 0; i < pendingOrders.size(); i++) {
                System.out.println((i + 1) + ". " + pendingOrders.get(i));
            }
        }
    }

    private void viewCompletedOrders() {
        System.out.println("\n--- Completed Orders ---");
        if (completedCount == 0) {
            System.out.println("No completed orders");
        } else {
            for (int i = 0; i < completedCount; i++) {
                System.out.println((i + 1) + ". " + completedOrders[i]);
            }
        }
    }

    private void completeOrder() {
        viewPendingOrders();
        
        if (pendingOrders.isEmpty()) {
            return;
        }
        
        int orderNum = getNumberInput("Enter order number to complete: ");
        
        try {
            // Try-catch for array index handling
            String completedOrder = pendingOrders.remove(orderNum - 1);
            completedOrders[completedCount] = completedOrder;
            completedCount++;
            System.out.println("Order completed: " + completedOrder);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid order number!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // File persistence methods
    private void saveData() {
        try {
            // Save pending orders (ArrayList)
            BufferedWriter writer = new BufferedWriter(new FileWriter("pending.txt"));
            for (String order : pendingOrders) {
                writer.write(order);
                writer.newLine();
            }
            writer.close();
            
            // Save completed orders (Array)
            writer = new BufferedWriter(new FileWriter("completed.txt"));
            for (int i = 0; i < completedCount; i++) {
                writer.write(completedOrders[i]);
                writer.newLine();
            }
            writer.close();
            
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void loadData() {
        try {
            // Load pending orders
            BufferedReader reader = new BufferedReader(new FileReader("pending.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                pendingOrders.add(line);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("No previous data found - starting fresh");
        }

        try {
            // Load completed orders  
            BufferedReader reader = new BufferedReader(new FileReader("completed.txt"));
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null && count < completedOrders.length) {
                completedOrders[count] = line;
                count++;
            }
            completedCount = count;
            reader.close();
        } catch (IOException e) {
            // It's okay if no completed orders file exists
        }
    }
}