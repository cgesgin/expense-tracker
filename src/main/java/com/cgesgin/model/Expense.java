package com.cgesgin.model;

import java.time.Month;

public class Expense {
    
    private int id;
    private String description;
    private Double amount;
    private Month month;
    private String category;

    public Expense() {
    }

    public Expense( int id, String description , Double amount,Month month,String category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.month = month;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id : "+this.id+" description : "+this.description+" amount : "+this.amount;
    }
}
