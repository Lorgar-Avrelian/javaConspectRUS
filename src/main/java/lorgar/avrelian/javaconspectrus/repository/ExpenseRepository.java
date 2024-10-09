package lorgar.avrelian.javaconspectrus.repository;

import lorgar.avrelian.javaconspectrus.models.Expense;
import lorgar.avrelian.javaconspectrus.models.ExpensesByCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query(value = "SELECT category, SUM(amount) AS amount FROM expense GROUP BY category", nativeQuery = true)
    List<ExpensesByCategory> getExpenseByCategories();
}
