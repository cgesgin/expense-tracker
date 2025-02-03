package com.cgesgin.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.cgesgin.model.Expense;
import com.cgesgin.model.enums.BudgetSeverity;
import com.cgesgin.model.repository.IGenericRepository;
import com.cgesgin.model.service.IExpenseService;
import com.cgesgin.repository.ExpenseRepository;

public class ExpenseService implements IExpenseService {

    private IGenericRepository<Expense> repository;

    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Expense save(Expense value) {
        if (repository.save(value)) {
            return this.repository.get(value.getId());
        }
        return new Expense();
    }

    @Override
    public Boolean remove(Expense value) {
        return repository.remove(value);
    }

    @Override
    public Expense update(Expense value) {
        if (repository.update(value)) {
            return this.repository.get(value.getId());
        }
        return new Expense();
    }

    @Override
    public List<Expense> getAll() {
        return this.repository.getAll();
    }

    @Override
    public Expense get(int id) {
        return this.repository.get(id);
    }

    @Override
    public double showMonthSummary(int month) {
        double totalAmount = repository.getAll().stream()
                .filter(exp -> exp.getMonth().getValue() == month)
                .mapToDouble(Expense::getAmount)
                .sum();
        return totalAmount;
    }

    @Override
    public double showSummary() {
        double totalAmount = repository.getAll().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
        return totalAmount;
    }

    @Override
    public double showCategoySummary(String category) {
        double totalAmount = repository.getAll().stream()
                .filter(exp -> Objects.equals(category, exp.getCategory()))
                .mapToDouble(Expense::getAmount)
                .sum();
        return totalAmount;
    }

    @Override
    public BudgetSeverity getBudgetSeverity(double budgetLimit,int month) {

        double monthSummary = showMonthSummary(month);

        if (monthSummary <= budgetLimit*0.9) {
            return BudgetSeverity.NORMAL;
        }
        else if (monthSummary <= budgetLimit * 1.0) {
            return BudgetSeverity.WARNING;
        }
        else if (monthSummary <= budgetLimit * 1.1) {
            return BudgetSeverity.CRITICAL;
        }
        return BudgetSeverity.OVERSPENT;
    }

    public Boolean exportCVS(){
        List<Expense> expenseList = this.repository.getAll(); 

        String os = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");

        String fileName;
        if (os.contains("win")) {
            fileName = userHome + "\\Desktop\\expenses.csv";
        } else {
            fileName = userHome + "/Desktop/expenses.csv";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Description,Amount,Category,Month\n");
            if(!expenseList.isEmpty()){
                for (Expense expense : expenseList) {
                    writer.write(expense.getDescription() + "," +
                            expense.getAmount() + "," +
                            expense.getCategory() + "," +
                            expense.getMonth() + "\n");
                }
                writer.close();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
