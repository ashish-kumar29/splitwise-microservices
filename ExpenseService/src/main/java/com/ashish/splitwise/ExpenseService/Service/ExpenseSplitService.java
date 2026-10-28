package com.ashish.splitwise.ExpenseService.Service;

import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;

import java.util.List;

public interface ExpenseSplitService {

    public List<ExpenseSplit> getExpenseSplitByUser(Long userId);
    public List<ExpenseSplit> getExpenseSplitByExpenseId(Long expenseId);
    public List<ExpenseSplit> getExpenseSplitByUserAndPaidStatus(Long userId, Boolean isPaid);
    public List<ExpenseSplit> getExpenseSplitByExpenseIdAndPaidStatus(Long expenseId, Boolean isPaid);
    public ExpenseSplit getExpenseSplitByUserInExpense(Long expenseId, Long userId);
    public ExpenseSplit getExpenseSplitById(Long id);
}
