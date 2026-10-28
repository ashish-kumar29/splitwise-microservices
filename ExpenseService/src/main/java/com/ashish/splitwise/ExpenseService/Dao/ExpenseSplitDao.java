package com.ashish.splitwise.ExpenseService.Dao;

import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ExpenseSplitDao {
    public List<ExpenseSplit> findAllSplitOfUserId(Long userId);
    public List<ExpenseSplit> findAllSplitofExpense(Long expenseId);
    public List<ExpenseSplit> findAllSplitOfUserIdAndIsPaid(Long userId, Boolean isPaid);
    public List<ExpenseSplit> findAllSplitofExpenseAndIsPaid(Long expenseId, Boolean isPaid);
    public ExpenseSplit findSplitofGivenExpenseAndUser(Long expenseId, Long userId);
    public Optional<ExpenseSplit> getById(Long id);
}
