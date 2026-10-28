package com.ashish.splitwise.ExpenseService.Repository;

import com.ashish.splitwise.ExpenseService.Model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByGroupId(Long groupId);
    List<Expense> findByPaidByUserId(Long userId);
    List<Expense> findByGroupIdAndPaidByUserId(Long groupId, Long userId);
}
