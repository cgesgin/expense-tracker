package com.cgesgin;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import com.cgesgin.model.Expense;
import com.cgesgin.model.service.IExpenseService;
import com.cgesgin.repository.ExpenseRepository;
import com.cgesgin.service.ExpenseService;

public class App {
    private static IExpenseService expenseService = new ExpenseService(new ExpenseRepository());
    private static int counter = 0;
    private static boolean budgetLimitActived=false;
    private static double budgetLimit=0.0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'help' for available commands.");

        while (true) {
            System.out.print("\nEnter command: ");
            String commandLine = scanner.nextLine();
            String[] commandArgs = commandLine.split(" ");

            if (commandArgs.length == 0) {
                System.out.println("No command provided. Type 'help' for usage.");
                continue;
            }

            String command = commandArgs[0].toLowerCase();

            try {
                switch (command) {
                    case "cls":
                    case "clear":
                        clearScreen();
                        break;

                    case "add":
                        if (commandArgs.length < 4) {
                            System.out.println("Description , amount and categoy are required.");
                            break;
                        }
                        String description = commandArgs[1];
                        double amount = Double.parseDouble(commandArgs[2]);
                        String category = commandArgs[3].toLowerCase().trim();
                        addExpense(description, amount, category);
                        break;

                    case "list":
                        listExpenses();
                        break;
                    
                    case "update":
                        if (commandArgs.length < 2) {
                            System.out.println("Expense ID is required.");
                            break;
                        }
                        try {
                            String idStr = commandArgs[1].trim();
                            int id = Integer.parseInt(idStr);
                            System.out.print("Enter new amount: ");
                            String amountStr = scanner.nextLine().trim();
                            double newAmount = Double.parseDouble(amountStr);
                            updateExpense(id, newAmount);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid number format. Please enter valid numbers.");
                        }
                        break;

                    case "delete":
                        if (commandArgs.length < 2) {
                            System.out.println("Expense ID is required.");
                            break;
                        }
                        try {
                            String idStr = commandArgs[1].trim();
                            int id = Integer.parseInt(idStr);
                            deleteExpense(id);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid ID format. Please enter a number.");
                        }
                        break;

                    case "summary":
                        if (commandArgs.length == 1) {
                            showSummary();
                        } else if (commandArgs.length == 2) {
                            int month = Integer.parseInt(commandArgs[1]);
                            showMonthSummary(month);
                        }
                        else {
                            System.out.println("Invalid command format. Use 'summary' or 'summary <month>'");
                        }
                        break;
                    case "summary-c":
                        if (commandArgs.length == 2) {
                            String categorySummery = commandArgs[1].toLowerCase().trim();
                            showCategorySummary(categorySummery);
                        }
                        break;
                    case "add-b":
                        if (commandArgs.length < 2) {
                            System.out.println("Expense budget is required.");
                            break;
                        }
                        budgetLimitActived=true;
                        budgetLimit=Double.parseDouble(commandArgs[1].trim());
                        System.out.println("Budget info is opened.");
                        break;
                    case "add-b-c":
                        if (commandArgs.length==1) {
                            budgetLimitActived=false;
                            budgetLimit=0.0;
                            System.out.println("Budget info is closed.");
                            break;
                        }
                        break;
                    case "export":
                        if(commandArgs.length==1){
                            exportCVS();
                        }
                        break;
                    case "help":
                        showHelp();
                        break;
                    case "exit":
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Unknown command. Type 'help' for usage.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        }
    }

    private static void clearScreen() {
        try {
            String operatingSystem = System.getProperty("os.name").toLowerCase();

            if (operatingSystem.contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showHelp() {
        System.out.println("\nExpense Tracker Usage:");
        System.out.println("add <description> <amount> <category>    - Add a new expense");
        System.out.println("list                                     - List all expenses");
        System.out.println("delete <id>                              - Delete an expense");
        System.out.println("update <id>                              - update an expense");
        System.out.println("summary                                  - Show total expenses");
        System.out.println("summary <month>                          - Show expenses for specific month");
        System.out.println("summary-c <category>                     - Show expenses for specific category");
        System.out.println("add-b <budget>                           - add budget limit for curret month");
        System.out.println("add-b-c                                  - budget stimulator shutdown");
        System.out.println("export                                   - export cvs");
        System.out.println("cls/clear                                - Clear screen");
        System.out.println("exit                                     - Exit the application");
    }

    private static void addExpense(String description, double amount,String category) {
        Expense expense = new Expense(++counter, description, amount, LocalDate.now().getMonth(),category);
        if(budgetLimitActived){
            System.out.println("Budget İnfo : " +expenseService.getBudgetSeverity(budgetLimit,LocalDate.now().getMonthValue()));
        }
        expenseService.save(expense);
        System.out.println("Expense added successfully (ID: " + expense.getId() + ")");
    }

    private static void listExpenses() {
        List<Expense> expenses = expenseService.getAll();
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
            return;
        }
        System.out.println("\nID  Description  Amount   Month   Category");
        System.out.println("--------------------------------------------");
        for (Expense exp : expenses) {
            System.out.printf("%d   %-10s  $%.2f   %-10s   %-10s",
                    exp.getId(),
                    exp.getDescription(),
                    exp.getAmount(),
                    exp.getMonth(),
                    exp.getCategory());
            System.out.println();
        }
    }

    private static void deleteExpense(int id) {
        try {
            Expense data = expenseService.get(id);
            expenseService.remove(data);
            System.out.println("Expense deleted successfully");
        } catch (Exception e) {
            System.out.println("Error deleting expense: " + e.getMessage());
        }
    }

    private static void updateExpense(int id, double amount) {
        try {
            Expense data = expenseService.get(id);
            if (data == null || data.getId() == 0) {
                System.out.println("Expense not found with ID: " + id);
                return;
            }
            data.setAmount(amount);
            expenseService.update(data);
            System.out.println("Expense updated successfully");
        } catch (Exception e) {
            System.out.println("Error updating expense: " + e.getMessage());
        }
    }

    private static void showSummary() {
        System.out.printf("Total expenses: $%.2f%n",expenseService.showSummary());
    }

    private static void showMonthSummary(int month) {
        System.out.printf("Total expenses: $%.2f%n",expenseService.showMonthSummary(month));
    }

    private static void showCategorySummary(String category) {
        System.out.printf("Total expenses: $%.2f%n",expenseService.showCategoySummary(category));
    }

    private static void exportCVS(){
        if(!expenseService.exportCVS()){
            System.out.println("failed to create file");
            return;
        }
        System.out.println("Successfully");
    }
}
