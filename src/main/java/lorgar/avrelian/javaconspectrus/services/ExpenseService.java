package lorgar.avrelian.javaconspectrus.services;

import lorgar.avrelian.javaconspectrus.models.Expense;
import lorgar.avrelian.javaconspectrus.models.ExpensesByCategory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ExpenseService {
    Collection<Expense> getAllExpenses();

    Expense getExpense(int id);

    Expense addExpense(Expense expense);

    Collection<ExpensesByCategory> getExpensesByCategories();

    Collection<Expense> getAllExpenses(int page, int size);
}