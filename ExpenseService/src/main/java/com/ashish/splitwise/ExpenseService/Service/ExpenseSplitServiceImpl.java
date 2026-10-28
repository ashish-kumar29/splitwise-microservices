package com.ashish.splitwise.ExpenseService.Service;

import com.ashish.splitwise.ExpenseService.Dao.ExpenseSplitDao;
import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseSplitServiceImpl implements ExpenseSplitService{

    private final ExpenseSplitDao splitDao;

    public ExpenseSplitServiceImpl(ExpenseSplitDao splitDao) {
        this.splitDao = splitDao;
    }


    @Override
    public List<ExpenseSplit> getExpenseSplitByUser(Long userId) {
        return splitDao.findAllSplitOfUserId(userId);
    }

    @Override
    public List<ExpenseSplit> getExpenseSplitByExpenseId(Long expenseId) {
        return splitDao.findAllSplitofExpense(expenseId);
    }

    @Override
    public List<ExpenseSplit> getExpenseSplitByUserAndPaidStatus(Long userId, Boolean isPaid) {
        return splitDao.findAllSplitOfUserIdAndIsPaid(userId, isPaid);
    }

    @Override
    public List<ExpenseSplit> getExpenseSplitByExpenseIdAndPaidStatus(Long expenseId, Boolean isPaid) {
        return splitDao.findAllSplitofExpenseAndIsPaid(expenseId, isPaid);
    }

    @Override
    public ExpenseSplit getExpenseSplitByUserInExpense(Long expenseId, Long userId) {
        return splitDao.findSplitofGivenExpenseAndUser(expenseId, userId);
    }

    @Override
    public ExpenseSplit getExpenseSplitById(Long id) {
        return splitDao.getById(id).orElseThrow(() -> new RuntimeException("No Expense Split is present with id "+id));
    }
}
