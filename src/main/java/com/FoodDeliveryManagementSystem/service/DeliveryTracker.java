package com.FoodDeliveryManagementSystem.service;

import java.util.HashMap;
import java.util.Map;

public class DeliveryTracker {

    private Map<Integer, String> deliveryStatus = new HashMap<>();

    // Add a new order
    public void addOrder(int orderId) {

        deliveryStatus.put(orderId, "Order Placed");

        System.out.println("Order placed successfully!");
    }

    // Update delivery status
    public void updateStatus(int orderId, String status) {

        if (deliveryStatus.containsKey(orderId)) {

            deliveryStatus.put(orderId, status);

            System.out.println("Delivery status updated!");

        } else {

            System.out.println("Order not found!");
        }
    }

    // Track an order
    public void trackOrder(int orderId) {

        if (deliveryStatus.containsKey(orderId)) {

            System.out.println("Order ID: " + orderId);

            System.out.println(
                    "Status: " + deliveryStatus.get(orderId)
            );

        } else {

            System.out.println("Order not found!");
        }
    }

    // Display all orders
    public void displayAllOrders() {

        System.out.println("\n--- All Orders ---");

        for (Map.Entry<Integer, String> entry :
                deliveryStatus.entrySet()) {

            System.out.println(
                    "Order ID: " + entry.getKey()
                    + " | Status: " + entry.getValue()
            );
        }
    }
}
