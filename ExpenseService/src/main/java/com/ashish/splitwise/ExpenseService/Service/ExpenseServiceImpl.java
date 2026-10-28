package com.ashish.splitwise.ExpenseService.Service;

import com.ashish.splitwise.ExpenseService.Dao.ExpenseDao;
import com.ashish.splitwise.ExpenseService.Model.Expense;
import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService{
    private final ExpenseDao expenseDao;
    private final ExpenseSplitService splitService;

    public ExpenseServiceImpl(ExpenseDao expenseDao, ExpenseSplitService expenseSplitService) {
        this.expenseDao = expenseDao;
        this.splitService = expenseSplitService;
    }

    @Override
    public Expense getExpenseById(Long id) {
        return expenseDao.getById(id).orElseThrow(()-> new RuntimeException("No Expense with given Id "+id));
    }

    @Override
    public List<Expense> getExpensesByGroupId(Long groupId) {
        return expenseDao.getExpensesByGroupId(groupId);
    }

    @Override
    public List<Expense> getAllExpense() {
        return expenseDao.findAll();
    }

    @Override
    public List<Expense> getExpensesByUserWhoPaidInGroup(Long groupId, Long userId) {
        return expenseDao.getExpensesByGroupIdAndUserWhoPaid(groupId, userId);
    }

    @Override
    public List<Expense> getExpensesByUserWhoPaid(Long userId) {
        return expenseDao.findExpensePaidByUser(userId);
    }

    @Override
    public List<ExpenseSplit> getExpenseSplitById(Long id) {
        return List.of();
    }

    @Override
    public Expense createExpense(Expense expense) {
        if (expense.getSplits() != null) {
            expense.getSplits().forEach(split -> split.setExpense(expense));
        }
        return expenseDao.save(expense);
    }

    @Override
    public Expense updateExpense(Expense expense, Long id) {
        return convertToExpense(expense, id);
    }

    @Override
    public Expense addExpenseSplit(Long expenseId, ExpenseSplit split) {
        Expense expense = getExpenseById(expenseId);
        split.setExpense(expense);
        expense.getSplits().add(split);
        adjustSplitAmount(expense);
        return expenseDao.update(expense);
    }

    @Override
    public Expense deleteExpenseSplit(Long expenseId, Long splitId) {
        Expense expense = getExpenseById(expenseId);
        ExpenseSplit split = splitService.getExpenseSplitById(splitId);
        expense.getSplits().remove(split);
        adjustSplitAmount(expense);
        return expenseDao.update(expense);
    }

    @Override
    public void deleteExpense(Long expenseId) {
        expenseDao.deleteExpenseById(expenseId);
    }

    public Expense convertToExpense(Expense e , Long id){
        Expense expense = getExpenseById(id);
        if(e.getTotalAmount() != null){
            expense.setTotalAmount(e.getTotalAmount());
            adjustSplitAmount(expense);
        }
        if(e.getDescription() != null){
            expense.setDescription(e.getDescription());
        }
        return expense;
    }

    private void adjustSplitAmount(Expense expense) {
        int adjustedAmountForEachSplit = expense.getTotalAmount()/expense.getSplits().size();
        expense.getSplits().forEach(split -> split.setSharedAmount(adjustedAmountForEachSplit));
    }
}
