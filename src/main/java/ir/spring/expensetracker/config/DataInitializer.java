package ir.spring.expensetracker.config;

import ir.spring.expensetracker.entity.Category;
import ir.spring.expensetracker.entity.TransactionType;
import ir.spring.expensetracker.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    private void categoriesInitialize() {
        List<Category> categories = new ArrayList<>();

        if (categoryRepository.findByUserIsNullAndType(TransactionType.Expense).isEmpty()) {
            Map<String, String> defaultExpenses = Map.of("Feed", "🍔", "Transportation", "🚗", "Shop", "🛍️", "Invoices", "💡");
            defaultExpenses.forEach((expense, icon) -> {
                categories.add(new Category(expense, icon, TransactionType.Expense));
            });
        }

        if (categoryRepository.findByUserIsNullAndType(TransactionType.Income).isEmpty()) {
            Map<String, String> defaultExpenses = Map.of("Salary", "💰", "Gifts", "🎁", "Profit", "📈");
            defaultExpenses.forEach((income, icon) -> {
                categories.add(new Category(income, icon, TransactionType.Income));
            });
        }

        categoryRepository.saveAll(categories);
    }

    @Override
    public void run(String... args) throws Exception {
        categoriesInitialize();
    }
}
