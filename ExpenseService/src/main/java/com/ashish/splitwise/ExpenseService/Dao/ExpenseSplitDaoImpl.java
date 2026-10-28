package com.ashish.splitwise.ExpenseService.Dao;

import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;
import com.ashish.splitwise.ExpenseService.Repository.ExpenseSplitRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ExpenseSplitDaoImpl implements ExpenseSplitDao{

    private final ExpenseSplitRepository repository;

    public ExpenseSplitDaoImpl(ExpenseSplitRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ExpenseSplit> findAllSplitOfUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<ExpenseSplit> findAllSplitofExpense(Long expenseId) {
        return repository.findByExpense_Id(expenseId);
    }

    @Override
    public List<ExpenseSplit> findAllSplitOfUserIdAndIsPaid(Long userId, Boolean isPaid) {
        return repository.findByUserIdAndIsPaid(userId, isPaid);
    }

    @Override
    public List<ExpenseSplit> findAllSplitofExpenseAndIsPaid(Long expenseId, Boolean isPaid) {
        return repository.findByExpense_IdAndIsPaid(expenseId, isPaid);
    }

    @Override
    public ExpenseSplit findSplitofGivenExpenseAndUser(Long expenseId, Long userId) {
        return repository.findByExpense_IdAndUserId(expenseId, userId);
    }

    @Override
    public Optional<ExpenseSplit> getById(Long id) {
        return repository.findById(id);
    }
}
