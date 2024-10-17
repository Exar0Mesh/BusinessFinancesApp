package com.pluralsight;

public class Transactions {
    public int year;
    public int month;
    private String calendar;
    private String currently;
    private String description;
    private String vendor;
    private float amount;

    // Constructor
    public Transactions(int year, int month) {
        this.year = year;
        this.month = month;
    }
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }

    public Transactions(String calendar, String currently, String description, String vendor, float amount) {
        this.calendar = calendar;
        this.currently = currently;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }
    public String getCalendar() {
        return calendar;
    }
    public String getCurrently() {
        return currently;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }
    public float getAmount() {
        return amount;
    }

}
