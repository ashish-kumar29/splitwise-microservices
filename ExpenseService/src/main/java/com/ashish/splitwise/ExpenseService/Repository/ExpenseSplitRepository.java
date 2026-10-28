package com.ashish.splitwise.ExpenseService.Repository;

import com.ashish.splitwise.ExpenseService.Model.Expense;
import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseSplitRepository extends JpaRepository<ExpenseSplit, Long> {
    ExpenseSplit findByExpense_IdAndUserId(Long expenseId, Long userId);
    List<ExpenseSplit> findByExpense_Id(Long expenseId);
    List<ExpenseSplit> findByExpense_IdAndIsPaid(Long expenseId, Boolean isPaid);
    List<ExpenseSplit> findByUserId(Long userId);
    List<ExpenseSplit>  findByUserIdAndIsPaid(Long userId , Boolean isPaid);
}
