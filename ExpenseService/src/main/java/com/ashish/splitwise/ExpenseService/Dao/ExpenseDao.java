package com.ashish.splitwise.ExpenseService.Dao;

import com.ashish.splitwise.ExpenseService.Model.Expense;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ExpenseDao {
    public Optional<Expense> getById(Long id);
    public List<Expense> getExpensesByGroupId(Long groupId);
    public List<Expense> getExpensesByGroupIdAndUserWhoPaid(Long groupId, Long userId);
    public Expense save(Expense expense);
    public Expense update(Expense expense);
    public List<Expense> findExpensePaidByUser(Long userId);
    public List<Expense> findAll();
    public void deleteExpenseById(Long id);
}
