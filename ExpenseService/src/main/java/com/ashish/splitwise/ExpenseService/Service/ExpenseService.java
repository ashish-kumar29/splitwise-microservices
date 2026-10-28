package com.ashish.splitwise.ExpenseService.Service;

import com.ashish.splitwise.ExpenseService.Model.Expense;
import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;

import java.util.List;

public interface ExpenseService {
    public Expense getExpenseById(Long id);
    public List<Expense> getExpensesByGroupId(Long groupId);
    public List<Expense> getAllExpense();
    public List<Expense> getExpensesByUserWhoPaidInGroup(Long groupId, Long userId);
    public List<Expense> getExpensesByUserWhoPaid(Long userId);
    public List<ExpenseSplit> getExpenseSplitById(Long id);
    public Expense createExpense(Expense expense);
    public Expense updateExpense(Expense expense, Long id);
    public Expense addExpenseSplit(Long expenseId, ExpenseSplit split);
    public Expense deleteExpenseSplit(Long expenseId, Long splitId);
    public void deleteExpense(Long expenseId);
}
