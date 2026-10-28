package com.ashish.splitwise.ExpenseService.Controller;


import com.ashish.splitwise.ExpenseService.Model.Expense;
import com.ashish.splitwise.ExpenseService.Model.ExpenseSplit;
import com.ashish.splitwise.ExpenseService.Service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@Tag(name = "Expense Controller", description = "Manage all expense-related operations")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get expense by ID", description = "Fetches a single expense from the database by its ID.")
    public Expense getExpenseById(@PathVariable Long id){
        return expenseService.getExpenseById(id);
    }

    @GetMapping("/{id}/split")
    @Operation(summary = "Get Expense Splits of Expense", description = "Fetches all expense splits of expense given by id")
    public List<ExpenseSplit> getExpenseSplitById(@PathVariable Long id){
        return expenseService.getExpenseById(id).getSplits();
    }

    @GetMapping("/{expenseId}/description")
    @Operation(summary = "Get Expense Description", description = "Fetch expense description of expense given by id")
    public String getExpenseDescriptionById(@PathVariable Long expenseId){
        return expenseService.getExpenseById(expenseId).getDescription();
    }


    @GetMapping("/{expenseId}/amount")
    @Operation(summary = "Get expense amount by ID",
            description = "Fetch the total amount for a specific expense by its ID.")
    public int getExpenseAmountById(@PathVariable Long expenseId){
        return getExpenseById(expenseId).getTotalAmount();
    }

    @GetMapping
    @Operation(summary = "Get all expenses",
            description = "Retrieve a list of all expenses stored in the system.")
    public List<Expense> getAllExpense(){
        return expenseService.getAllExpense();
    }

    @GetMapping("/groupId/{groupId}")
    @Operation(summary = "Get expenses by group",
            description = "Retrieve all expenses that belong to a specific group by group ID.")
    public List<Expense> getAllExpenseByGroup(@PathVariable Long groupId){
        return expenseService.getExpensesByGroupId(groupId);
    }

    @GetMapping("/group/{groupId}/user/{userId}")
    @Operation(summary = "Get expenses paid by user in group",
            description = "Retrieve all expenses that were paid by a specific user inside a given group.")
    public List<Expense> getExpensePaidByUserInGroup(@PathVariable Long groupId, @PathVariable Long userId){
        return expenseService.getExpensesByUserWhoPaidInGroup(groupId, userId);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get expenses paid by user",
            description = "Retrieve all expenses that were paid by a specific user across all groups.")
    public List<Expense> getExpensePaidByUser(@PathVariable Long userId){
        return expenseService.getExpensesByUserWhoPaid(userId);
    }

    @PostMapping
    @Operation(summary = "Create a new expense",
            description = "Create a new expense record and save it to the system.")
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense){
        Expense savedExpense = expenseService.createExpense(expense);
        return ResponseEntity.ok(savedExpense);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update expense by ID",
            description = "Update an existing expense record using its ID.")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @RequestBody Expense expense){
        Expense updatedNewExpense = expenseService.updateExpense(expense, id);
        return ResponseEntity.ok(updatedNewExpense);
    }

    @PostMapping("/{id}")
    @Operation(summary = "Add a split to expense",
            description = "Add a new split (share) to an existing expense by expense ID.")
    public ResponseEntity<Expense> addExpenseSplitInExpense(@PathVariable Long id, ExpenseSplit split){
        Expense expense = expenseService.addExpenseSplit(id, split);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{expenseId}/split/{splitId}")
    @Operation(summary = "Delete a split in expense",
            description = "Delete a specific split from an expense by expense ID and split ID.")
    public ResponseEntity<Expense> deleteExpenseSplitInExpense(@PathVariable Long expenseId, @PathVariable Long splitId){
        Expense expense = expenseService.deleteExpenseSplit(expenseId, splitId);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete expense by ID",
            description = "Remove an expense from the system using its ID.")
    public ResponseEntity<String> deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        return ResponseEntity.ok("Expense deleted successfully");
    }


}
