package org.example;

import java.sql.*;
import java.util.Scanner;

public class ComputerShopApp{
    static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    static final String USER = "postgres";
    static final String PASS = "bibushka25A";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;
            while (!exit) {
                printMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (choice == 1) {
                    createComputer(conn, scanner);
                } else if (choice == 2) {
                    showComputers(conn);
                } else if (choice == 3) {
                    updateComputer(conn, scanner);
                } else if (choice == 4) {
                    deleteComputer(conn, scanner);
                } else if (choice == 5) {
                    exit = true;
                } else {
                    System.out.println("Invalid input. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("1. Create a computer");
        System.out.println("2. Show all computers");
        System.out.println("3. Update computer");
        System.out.println("4. Delete computer");
        System.out.println("5. Exit");
        System.out.print("selected action: ");
    }

    private static void createComputer(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter the computer brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter the computer model: ");
        String model = scanner.nextLine();
        System.out.print("Enter the price of the computer: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        String sql = "INSERT INTO computer_shop (brand, model, price) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, brand);
            preparedStatement.setString(2, model);
            preparedStatement.setDouble(3, price);
            preparedStatement.executeUpdate();
            System.out.println("The computer was successfully added.");
        }
    }

    private static void showComputers(Connection conn) throws SQLException {
        String sql = "SELECT id, brand, model, price FROM computer_shop";
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String brand = resultSet.getString("brand");
                String model = resultSet.getString("model");
                double price = resultSet.getDouble("price");
                System.out.println("ID: " + id + ", brand: " + brand + ", model: " + model + ", price: " + price);
            }
        }
    }

    private static void updateComputer(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter the computer ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter a new computer brand: ");
        String brand = scanner.nextLine();
        System.out.print("Enter a new computer model: ");
        String model = scanner.nextLine();
        System.out.print("Enter the new computer price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        String sql = "UPDATE computer_shop SET brand = ?, model = ?, price = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, brand);
            preparedStatement.setString(2, model);
            preparedStatement.setDouble(3, price);
            preparedStatement.setInt(4, id);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("The computer has been successfully updated.");
            } else {
                System.out.println("The computer with the specified ID was not found.");
            }
        }
    }

    private static void deleteComputer(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter the computer ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        String sql = "DELETE FROM computer_shop WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows > 0) {
                System.out.println("The computer was successfully deleted.");
            } else {
                System.out.println("The computer with the specified ID was not found.");
            }
        }
    }
}
