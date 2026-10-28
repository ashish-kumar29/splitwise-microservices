package com.ashish.splitwise.ExpenseService.Model;


import com.ashish.splitwise.ExpenseService.DTO.UserDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "expense_split",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"expense_id", "user_id"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseSplit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int sharedAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    @JsonBackReference
    private Expense expense;

    @Column(nullable = false)
    private boolean isPaid;
}
