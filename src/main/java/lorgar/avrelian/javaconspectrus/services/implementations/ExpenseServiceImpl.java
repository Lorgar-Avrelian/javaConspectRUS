package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.models.Expense;
import lorgar.avrelian.javaconspectrus.models.ExpensesByCategory;
import lorgar.avrelian.javaconspectrus.repository.ExpenseRepository;
import lorgar.avrelian.javaconspectrus.services.ExpenseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Collection<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public Collection<Expense> getAllExpenses(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page resultPage = expenseRepository.findAll(pageRequest);
        return resultPage.getContent();
    }

    @Override
    public Expense getExpense(int id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Collection<ExpensesByCategory> getExpensesByCategories() {
        return expenseRepository.getExpenseByCategories();
    }
}