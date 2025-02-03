package com.cgesgin.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.cgesgin.model.Expense;
import com.cgesgin.model.repository.IGenericRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExpenseRepository implements IGenericRepository<Expense> {

    private static final String FILE_DIRECTORY = System.getProperty("user.dir") + "/data/";
    private static final String FILE_PATH = "expenses.json";

    private File file;

    private ObjectMapper mapper;

    List<Expense> expenses;

    public ExpenseRepository() {
        this.expenses = new ArrayList<>();
        this.mapper = new ObjectMapper();
        this.file = new File(FILE_DIRECTORY + FILE_PATH);
    }

    @Override
    public Boolean save(Expense value) {
        try {
            File directory = new File(FILE_DIRECTORY);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            if (file.length() != 0) {
                expenses = mapper.readValue(file, new TypeReference<List<Expense>>() {
                });
            }

            expenses.add(value);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, expenses);

            return true;

        } catch (Exception e) {
            System.out.println("Hata: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean remove(Expense value) {
        try {
            if (file.length() == 0) {
                return false;
            }
            expenses = mapper.readValue(file, new TypeReference<List<Expense>>() {
            });
            expenses.removeIf(expense -> expense.getId() == value.getId());
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, expenses);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean update(Expense value) {
        try {

            if (file.length() == 0) {
                return false;
            }

            expenses = mapper.readValue(file, new TypeReference<List<Expense>>() {
            });
            for (Expense expense : expenses) {
                if (expense.getId() == value.getId()) {
                    expense.setAmount(value.getAmount());
                    expense.setDescription(value.getDescription());
                    expense.setMonth(value.getMonth());
                    expense.setCategory(value.getCategory());
                    mapper.writerWithDefaultPrettyPrinter().writeValue(file, expenses);
                }
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Expense> getAll() {
        try {
            if (file.length() == 0) {
                return new ArrayList<>();
            }
            expenses = mapper.readValue(file, new TypeReference<List<Expense>>() {
            });
            return expenses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Expense get(int id) {
        Expense value = new Expense();
        try {

            if (file.length() == 0) {
                return value;
            }

            expenses = mapper.readValue(file, new TypeReference<List<Expense>>() {
            });
            for (Expense expense : expenses) {
                if (expense.getId() == id) {
                    value=expense;
                }
            }
            return value;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}