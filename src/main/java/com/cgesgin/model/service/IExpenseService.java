package com.cgesgin.model.service;

import com.cgesgin.model.Expense;
import com.cgesgin.model.enums.BudgetSeverity;

public interface IExpenseService extends IGenericService<Expense> {
    double showMonthSummary(int month);
    double showSummary();
    double showCategoySummary(String category);
    BudgetSeverity getBudgetSeverity(double budgetLimit,int month);
    Boolean exportCVS();
}
