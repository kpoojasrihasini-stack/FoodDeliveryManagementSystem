package com.FoodDeliveryManagementSystem.app;

import com.FoodDeliveryManagementSystem.service.DataManager;
import com.FoodDeliveryManagementSystem.service.JsonManager;
import com.FoodDeliveryManagementSystem.service.OrderProcessor;
import com.FoodDeliveryManagementSystem.service.DeliveryTracker;

import com.FoodDeliveryManagementSystem.model.Customer;
import com.FoodDeliveryManagementSystem.model.FoodItem;
import com.FoodDeliveryManagementSystem.model.MenuItem;
import com.FoodDeliveryManagementSystem.model.Order;
import com.FoodDeliveryManagementSystem.model.Restaurant;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Restaurant foodPalace = new Restaurant(1, "Food Palace");

        foodPalace.addMenuItem(new FoodItem(101, "Chicken Biryani", 180.00));
        foodPalace.addMenuItem(new FoodItem(102, "Veg Biryani", 140.00));
        foodPalace.addMenuItem(new FoodItem(103, "Chicken Fried Rice", 160.00));

        Restaurant pizzaHub = new Restaurant(2, "Pizza Hub");

        pizzaHub.addMenuItem(new FoodItem(201, "Veg Pizza", 200.00));
        pizzaHub.addMenuItem(new FoodItem(202, "Chicken Pizza", 250.00));
        pizzaHub.addMenuItem(new FoodItem(203, "Cheese Pizza", 180.00));

        DataManager.addRestaurant(foodPalace);
        DataManager.addRestaurant(pizzaHub);

        try (Scanner scanner = new Scanner(System.in)) {

            System.out.println("==================================");
            System.out.println(" FOOD DELIVERY MANAGEMENT SYSTEM");
            System.out.println("==================================");

            System.out.print("Enter your name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter your email: ");
            String email = scanner.nextLine().trim();

            if (name.isEmpty() || email.isEmpty()) {
                System.out.println("Name and email cannot be empty.");
                return;
            }

            Customer customer = new Customer(1, name, email);

            System.out.println("\nAvailable Restaurants:");
            DataManager.displayRestaurants();

            System.out.print("\nSelect Restaurant ID: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a numeric restaurant ID.");
                return;
            }

            Restaurant selectedRestaurant =
                    DataManager.searchRestaurant(scanner.nextInt());

            if (selectedRestaurant == null) {
                System.out.println("Restaurant not found.");
                return;
            }

            System.out.println("\nMenu of " + selectedRestaurant.getName());

            selectedRestaurant.displayMenu();

            System.out.print("\nSelect Food ID: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a numeric food ID.");
                return;
            }

            MenuItem selectedItem =
                    selectedRestaurant.searchMenuItem(scanner.nextInt());

            if (selectedItem == null) {
                System.out.println("Food item not found.");
                return;
            }

            System.out.print("Enter quantity: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Quantity must be a whole number.");
                return;
            }

            int quantity = scanner.nextInt();

            if (quantity <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return;
            }

            // Create order
            Order order = new Order(
                    1001,
                    customer,
                    selectedRestaurant,
                    selectedItem,
                    quantity
            );

            // Display bill
            System.out.println("\n========== BILL ==========");
            order.displayBill();

            // Payment selection
            System.out.println("\nPayment Types:");
            System.out.println("1. UPI");
            System.out.println("2. Card");
            System.out.println("3. Cash on Delivery");

            System.out.print("Select payment type: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                return;
            }

            int choice = scanner.nextInt();

            String paymentType;

            switch (choice) {

                case 1:
                    paymentType = "UPI";
                    break;

                case 2:
                    paymentType = "Card";
                    break;

                case 3:
                    paymentType = "Cash on Delivery";
                    break;

                default:
                    paymentType = "Cash on Delivery";
                    System.out.println(
                            "Invalid choice; using Cash on Delivery."
                    );
                    break;
            }

            order.setPaymentType(paymentType);

            System.out.println("Payment selected: " + paymentType);

            // Save order
            DataManager.addOrder(order);

            // Process order
            OrderProcessor.processOrder(order);

            // Save order as JSON
            try {
                JsonManager.saveOrder(order);

            } catch (IOException e) {
                System.out.println(
                        "Could not save order.json: " + e.getMessage()
                );
            }

            // DELIVERY TRACKING

            DeliveryTracker tracker = new DeliveryTracker();

            System.out.println("\n==================================");
            System.out.println("       DELIVERY TRACKING");
            System.out.println("==================================");

            int orderId = 1001;

            // Add order to delivery tracker
            tracker.addOrder(orderId);

            System.out.println("\nDelivery Status Options:");
            System.out.println("1. Preparing");
            System.out.println("2. Ready for Pickup");
            System.out.println("3. Out for Delivery");
            System.out.println("4. Delivered");
            System.out.println("0. Keep current status");

            System.out.print("\nSelect delivery status: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Keeping current status.");
            } else {

                int statusChoice = scanner.nextInt();

                switch (statusChoice) {

                    case 1:
                        tracker.updateStatus(orderId, "Preparing");
                        break;

                    case 2:
                        tracker.updateStatus(orderId, "Ready for Pickup");
                        break;

                    case 3:
                        tracker.updateStatus(orderId, "Out for Delivery");
                        break;

                    case 4:
                        tracker.updateStatus(orderId, "Delivered");
                        break;

                    case 0:
                        System.out.println("Current status retained.");
                        break;

                    default:
                        System.out.println("Invalid choice. Status unchanged.");
                        break;
                }
            }

            // Display current order status
            System.out.println("\n========== ORDER TRACKING ==========");

            tracker.trackOrder(orderId);

            // Display all tracked orders
            tracker.displayAllOrders();

            System.out.println("\nThank you for using Food Delivery!");
        }
    }
}