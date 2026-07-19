package ir.spring.expensetracker.repository;

import ir.spring.expensetracker.entity.Category;
import ir.spring.expensetracker.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserIsNullAndType(TransactionType type);

    List<Category> findByUserIdAndType(Long userId, TransactionType type);

    Category findByName(String text);
}
