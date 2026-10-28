package com.ashish.splitwise.ExpenseService.Dao;

import com.ashish.splitwise.ExpenseService.Model.Expense;
import com.ashish.splitwise.ExpenseService.Repository.ExpenseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExpenseDaoImpl implements ExpenseDao{
    private final ExpenseRepository repository;

    public ExpenseDaoImpl(ExpenseRepository repository) {
        this.repository = repository;
    }


    @Override
    public Optional<Expense> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Expense> getExpensesByGroupId(Long groupId) {
        return repository.findByGroupId(groupId);
    }

    @Override
    public List<Expense> getExpensesByGroupIdAndUserWhoPaid(Long groupId, Long userId) {
        return repository.findByGroupIdAndPaidByUserId(groupId, userId);
    }

    @Override
    public Expense save(Expense expense) {
        return repository.save(expense);
    }

    @Override
    public Expense update(Expense expense) {
        return repository.save(expense);
    }

    @Override
    public List<Expense> findExpensePaidByUser(Long userId) {
        return repository.findByPaidByUserId(userId);
    }

    @Override
    public List<Expense> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteExpenseById(Long id) {
        repository.deleteById(id);
    }
}
