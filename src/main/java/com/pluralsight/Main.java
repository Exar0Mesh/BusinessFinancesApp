package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        boolean running = true;
        HashMap<String, Transactions> finances = loadTransactions();
        Scanner scanner = new Scanner(System.in);

        try {

            FileReader fileReader = new FileReader("src/main/resources/transactions.csv");
            BufferedReader bufReader = new BufferedReader(fileReader);
            String input;

            while ((input = bufReader.readLine()) != null) {
                String[] dash = input.split(Pattern.quote("-"));
                int year = Integer.parseInt(dash[0]);
                int month = Integer.parseInt(dash[1]);

                String[] parts =input.split(Pattern.quote("|"));

                String calendar = parts[0];
                String currently = parts[1];
                String description = parts[2];
                String vendor = parts[3];
                float amount = Float.parseFloat(parts[4]);
                finances.put(calendar, new Transactions(year, month));

                finances.put(description, new Transactions(calendar, currently, description, vendor, amount));
            }
            bufReader.close();

            while (running) {
                System.out.println("D) Add Deposit");
                System.out.println("P) Make Payment (Debit)");
                System.out.println("L) Ledger");
                System.out.println("X) Exit");
                System.out.print("Please enter your choice: ");
                String choice = scanner.nextLine();

                if (choice.equalsIgnoreCase("D")) {         //Working
                    System.out.println("Description: ");
                    String info = scanner.nextLine();
                    System.out.println("Enter your name: ");
                    String user = scanner.nextLine();
                    System.out.print("Deposit amount: ");
                    int putIn = scanner.nextInt();
                    scanner.nextLine();
                    actionsLogger("|" + info + "|" + user + "|" + putIn);

                    break;
                }

                if (choice.equalsIgnoreCase("P")) {         //Working
                    System.out.println("What did you buy? (Description): ");
                    String buy = scanner.nextLine();
                    System.out.println("Enter the vendor bought from: ");
                    String bought = scanner.nextLine();
                    System.out.print("Cost in numbers: ");
                    float number = scanner.nextFloat();
                    scanner.nextLine();
                    actionsLogger("|" + buy + "|" + bought + "|-" + number);

                    break;
                }

                if (choice.equalsIgnoreCase("L")) {
                    while(true) {
                        System.out.println("A) All \nD) Deposits \nP) Payments \nR) Reports \nH) Home \n");
                        System.out.print("What would you like to display?: ");
                        String display = scanner.nextLine();

                        if (display.equalsIgnoreCase("A")) {
                            displayTransactions(finances);
                            break;
                        }
                        if (display.equalsIgnoreCase("D")) {
                            searchByDeposits(finances);
                            break;
                        }
                        if (display.equalsIgnoreCase("P")) {
                            searchByPayments(finances);
                            break;
                        }
                        if (display.equalsIgnoreCase("R")) {
                            while (true) {
                                System.out.println("1) Month to Date \n2) Previous Month \n3) Year to Date \n4) Previous Year \n" +
                                        "5) Search by Vendor");
                                System.out.print("Enter your choice: ");

                                int filterChoice = scanner.nextInt();
                                scanner.nextLine();
                                switch (filterChoice) {
                                    case 1:
                                        System.out.println("Filtering to Month: ");
                                        filterToMonth(finances);
                                        break;
                                    case 2:
                                        System.out.println("Filtering by previous Month: ");
                                        filterByLastMonth(finances);
                                        break;
                                    case 3:
                                        System.out.println("Filtering by Year: ");
                                        filterToYear(finances);
                                        break;
                                    case 4:
                                        System.out.println("Filtering by previous Year: ");
                                        filterByLastYear(finances);
                                        break;
                                    case 5:
                                        System.out.println("Which Vendor would you like to display?: ");
                                        String vendor = scanner.nextLine();
                                        for (Transactions user : finances.values()) {
                                            if (Objects.equals(vendor, user.getVendor())) {
                                                System.out.printf("%s|%s|%s|%s|$%.2f\n",
                                                        user.getCalendar(), user.getCurrently(), user.getDescription(),
                                                        user.getVendor(), user.getAmount());
                                            }
                                        }
                                        System.out.println(" ");
                                        break;
                                    default:
                                        System.out.println("Invalid choice, please try again.");
                                        System.out.println(" ");
                                        break;
                                }
                            }
                        }
                        if (display.equalsIgnoreCase("H")) {
                            System.out.println("Returning to home...");
                        } else {
                            break;
                        }
                    }
                }
                if (choice.equalsIgnoreCase("X")) {
                    System.exit(0);
                }
            }
                scanner.close();
        } catch (IOException e) {
            System.out.println("An unexpected error occurred");
            e.printStackTrace();
        }
    }

    public static HashMap<String, Transactions> loadTransactions() {
        HashMap<String, Transactions> listed = new HashMap<String, Transactions>();
        return listed;
    }

    public static void actionsLogger(String action) {
        try {
            FileWriter fw = new FileWriter("src/main/resources/transactions.csv", true);
            BufferedWriter logger = new BufferedWriter(fw);
            LocalDateTime today = LocalDateTime.now();
            DateTimeFormatter fmt =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");

            String formattedDate = today.format(fmt);
            logger.write(formattedDate + " " + action);
            logger.newLine();

            logger.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayTransactions(HashMap<String, Transactions> finances) {
        for (Transactions outcome : finances.values()) {
            if((outcome.getCalendar()!=null && outcome.getCurrently()!=null && outcome.getDescription()!=null &&
                    outcome.getVendor()!=null)) {
                System.out.printf("%s|%s|%s|%s|$%.2f\n",
                        outcome.getCalendar(), outcome.getCurrently(), outcome.getDescription(), outcome.getVendor(),
                        outcome.getAmount());

            }
        }
        System.out.println(" ");
    }
    private static void searchByDeposits(HashMap<String, Transactions> finances) {
        System.out.println("\nSearch Results:");
        boolean found = false;
        for (Transactions positive : finances.values()) {
            if (positive.getAmount() > 0) {
                System.out.printf("%s|%s|%s|%s|$%.2f\n",
                        positive.getCalendar(), positive.getCurrently(), positive.getDescription(),
                        positive.getVendor(), positive.getAmount());
                found = true;
            }
        }
        if (!found) {
            System.out.println("There are no deposits");
        }
        System.out.println(" ");
    }

    private static void searchByPayments(HashMap<String, Transactions> finances) {
        System.out.println("\nSearch Results:");
        boolean found = false;
        for (Transactions negative : finances.values()) {
            if (negative.getAmount() < 0) {
                System.out.printf("%s|%s|%s|%s|$%.2f\n",
                        negative.getCalendar(), negative.getCurrently(), negative.getDescription(),
                        negative.getVendor(), negative.getAmount());
                found = true;
            }
        }
        if (!found) {
            System.out.println("There are no payments");
        }
        System.out.println(" ");
    }

    private static void filterToMonth(HashMap<String, Transactions> finances) {
        boolean found = false;
        LocalDateTime nowM = LocalDateTime.now();
        for (Transactions dateFrom : finances.values()) {

            String calendar = dateFrom.getCalendar();
            if (calendar!=null == true) {
                String[] dash = calendar.split(Pattern.quote("-"));
                int year = Integer.parseInt(dash[0]);
                int month = Integer.parseInt(dash[1]);
                int day = Integer.parseInt(dash[2]);
                if (month == nowM.getMonthValue()) {
                    System.out.printf("%s|%s|%s|%s|$%.2f\n",
                            dateFrom.getCalendar(), dateFrom.getCurrently(), dateFrom.getDescription(),
                            dateFrom.getVendor(), dateFrom.getAmount());
                    found = true;
                    }
                }
            }
            if (!found) {
                System.out.println("There is no transactions this month: ");
            }
        System.out.println(" ");
    }

    private static void filterByLastMonth(HashMap<String, Transactions> finances) {
        boolean found = false;
        LocalDateTime nowM = LocalDateTime.now();
        LocalDateTime earlierM = nowM.minusMonths(1);
        for (Transactions dateFrom : finances.values()) {
            String calendar = dateFrom.getCalendar();
            if (calendar != null == true) {
                String[] dash = calendar.split(Pattern.quote("-"));
                int year = Integer.parseInt(dash[0]);
                int month = Integer.parseInt(dash[1]);
                int day = Integer.parseInt(dash[2]);
                if (month == earlierM.getMonthValue()) {
                    System.out.printf("%s|%s|%s|%s|$%.2f\n",
                            dateFrom.getCalendar(), dateFrom.getCurrently(), dateFrom.getDescription(),
                            dateFrom.getVendor(), dateFrom.getAmount());
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("There is no finance changes last month: ");
        }
        System.out.println(" ");
    }

    private static void filterToYear(HashMap<String, Transactions> finances) {
        System.out.println("\nYear to Date: ");
        boolean found = false;
        LocalDateTime nowY = LocalDateTime.now();
        for (Transactions dateFrom : finances.values()) {
            String calendar = dateFrom.getCalendar();
            if (calendar != null == true) {
                String[] dash = calendar.split(Pattern.quote("-"));
                int year = Integer.parseInt(dash[0]);
                int month = Integer.parseInt(dash[1]);
                int day = Integer.parseInt(dash[2]);
                if (year == nowY.getYear()) {
                    System.out.printf("%s|%s|%s|%s|$%.2f\n",
                            dateFrom.getCalendar(), dateFrom.getCurrently(), dateFrom.getDescription(),
                            dateFrom.getVendor(), dateFrom.getAmount());
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("There is no finance changes this year: ");
        }
        System.out.println(" ");
    }

    private static void filterByLastYear(HashMap<String, Transactions> finances) {
        System.out.println("\nLast Year: ");
        boolean found = false;
        LocalDateTime nowM = LocalDateTime.now();
        LocalDateTime earlierY = nowM.minusYears(1);
        for (Transactions dateFrom : finances.values()) {
            String calendar = dateFrom.getCalendar();
            if (calendar != null == true) {
                String[] dash = calendar.split(Pattern.quote("-"));
                int year = Integer.parseInt(dash[0]);
                int month = Integer.parseInt(dash[1]);
                int day = Integer.parseInt(dash[2]);
                if (year == earlierY.getYear()) {
                    System.out.printf("%s|%s|%s|%s|$%.2f\n",
                            dateFrom.getCalendar(), dateFrom.getCurrently(), dateFrom.getDescription(),
                            dateFrom.getVendor(), dateFrom.getAmount());
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("There is no finance changes last year: ");
        }
        System.out.println(" ");
    }
}
