package com.ashish.splitwise.ExpenseService.Controller;

import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;
import com.ashish.splitwise.ExpenseService.Service.ExpenseSplitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenseSplit")
@Tag(name = "Expense Split Controller", description = "Manage expense splits, filter by user and payment status")
public class ExpenseSplitController {

    private final ExpenseSplitService splitService;

    public ExpenseSplitController(ExpenseSplitService splitService) {
        this.splitService = splitService;
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all splits for a user",
            description = "Retrieve all expense splits where the specified user is involved.")
    public List<ExpenseSplit> getExpenseSplitByUser(@PathVariable Long userId){
        return splitService.getExpenseSplitByUser(userId);
    }

    @GetMapping("/expense/{expenseId}")
    @Operation(summary = "Get all splits for an expense",
            description = "Retrieve all expense splits for a specific expense using its ID.")
    public List<ExpenseSplit> getExpenseSplitByExpenseId(@PathVariable Long expenseId){
        return splitService.getExpenseSplitByExpenseId(expenseId);
    }

    @GetMapping("/user/{id}/paid")
    @Operation(summary = "Get all paid splits for a user",
            description = "Retrieve all expense splits where the user has already paid.")
    public List<ExpenseSplit> getPaidExpenseSplitByUser(@PathVariable("id") Long userId){
        return splitService.getExpenseSplitByUserAndPaidStatus(userId, true);
    }

    @GetMapping("/user/{id}/unPaid")
    @Operation(summary = "Get all unpaid splits for a user",
            description = "Retrieve all expense splits where the user still has to pay.")
    public List<ExpenseSplit> getUnPaidExpenseSplitByUser(@PathVariable("id") Long userId){
        return splitService.getExpenseSplitByUserAndPaidStatus(userId, false);
    }

    @GetMapping("/expense/{id}/unPaid")
    @Operation(summary = "Get all unpaid splits for an expense",
            description = "Retrieve all expense splits for a specific expense where payment is pending.")
    public List<ExpenseSplit> getUnPaidExpenseSplitByExpenseId(@PathVariable("id") Long expenseId){
        return splitService.getExpenseSplitByExpenseIdAndPaidStatus(expenseId, false);
    }

    @GetMapping("/expense/{id}/paid")
    @Operation(summary = "Get all paid splits for an expense",
            description = "Retrieve all expense splits for a specific expense where payment is completed.")
    public List<ExpenseSplit> getPaidExpenseSplitByExpenseId(@PathVariable("id") Long expenseId){
        return splitService.getExpenseSplitByExpenseIdAndPaidStatus(expenseId, true);
    }
}
