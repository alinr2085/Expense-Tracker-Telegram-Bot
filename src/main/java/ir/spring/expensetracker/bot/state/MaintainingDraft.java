package ir.spring.expensetracker.bot.state;

import ir.spring.expensetracker.entity.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintainingDraft {
    private BigDecimal amount;
    private Long categoryId;
    private TransactionType transactionType;
    private String description;
    private LocalDate transactionDate;
    private Long transactionId;
    private String editField;
}
