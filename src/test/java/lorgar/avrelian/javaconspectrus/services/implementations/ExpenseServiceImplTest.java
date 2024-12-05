package lorgar.avrelian.javaconspectrus.services.implementations;

import lorgar.avrelian.javaconspectrus.models.Expense;
import lorgar.avrelian.javaconspectrus.models.ExpensesByCategory;
import lorgar.avrelian.javaconspectrus.repository.ExpenseRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static lorgar.avrelian.javaconspectrus.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName(value = "Test of ExpenseServiceImpl")
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExpenseServiceImplTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Test
    @DisplayName(value = "getAllExpenses(): return Collection<Expense>")
    @Order(1)
    void getAllExpenses1() {
        when(expenseRepository.findAll())
                .thenReturn(EXPENSES_COLLECTION);
        //
        Collection<Expense> actualCollection = expenseService.getAllExpenses();
        assertInstanceOf(Collection.class, actualCollection);
        assertIterableEquals(EXPENSES_COLLECTION, actualCollection);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "getAllExpenses(): return Collection<>")
    @Order(2)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllExpenses2() {
        when(expenseRepository.findAll())
                .thenReturn(new ArrayList<>());
        //
        Collection<Expense> actualCollection = expenseService.getAllExpenses();
        assertInstanceOf(Collection.class, actualCollection);
        assertIterableEquals(Collections.EMPTY_LIST, actualCollection);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "getAllExpenses(int page, int size): return Collection<Expense>")
    @Order(3)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllExpenses3() {
        int page = 1;
        int size = 4;
        when(expenseRepository.findAll(eq(PageRequest.of(page - 1, size))))
                .thenReturn(EXPENSES_COLLECTION_PAGE);
        //
        Collection<Expense> actualCollection = expenseService.getAllExpenses(page, size);
        assertInstanceOf(Collection.class, actualCollection);
        assertIterableEquals(EXPENSES_COLLECTION, actualCollection);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "getAllExpenses(int page, int size): return Collection<>")
    @Order(4)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getAllExpenses4() {
        int page = 1;
        int size = 4;
        when(expenseRepository.findAll(eq(PageRequest.of(page - 1, size))))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        //
        Collection<Expense> actualCollection = expenseService.getAllExpenses(page, size);
        assertInstanceOf(Collection.class, actualCollection);
        assertIterableEquals(Collections.EMPTY_LIST, actualCollection);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "getExpense(int id): return Expense")
    @Order(5)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getExpense1() {
        when(expenseRepository.findById(eq(EXPENSE_1.getId())))
                .thenReturn(Optional.of(EXPENSE_1));
        //
        Expense actualExpense = expenseService.getExpense(EXPENSE_1.getId());
        assertInstanceOf(Expense.class, actualExpense);
        assertEquals(EXPENSE_1, actualExpense);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "getExpense(int id): return null")
    @Order(6)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getExpense2() {
        when(expenseRepository.findById(eq(EXPENSE_2.getId())))
                .thenReturn(Optional.ofNullable(null));
        //
        Expense actualExpense = expenseService.getExpense(EXPENSE_2.getId());
        assertNull(actualExpense);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "addExpense(Expense expense): return Expense")
    @Order(7)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void addExpense1() {
        when(expenseRepository.save(eq(EXPENSE_1)))
                .thenReturn(EXPENSE_1);
        //
        Expense actualExpense = expenseService.addExpense(EXPENSE_1);
        assertInstanceOf(Expense.class, actualExpense);
        assertEquals(EXPENSE_1, actualExpense);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "addExpense(Expense expense): return null")
    @Order(8)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void addExpense2() {
        when(expenseRepository.save(eq(EXPENSE_2)))
                .thenReturn(null);
        //
        Expense actualExpense = expenseService.addExpense(EXPENSE_2);
        assertNull(actualExpense);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "getExpensesByCategories(): return Collection<ExpensesByCategory>")
    @Order(9)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getExpensesByCategories1() {
        when(expenseRepository.getExpenseByCategories())
                .thenReturn(EXPENSES_BY_CATEGORY_COLLECTION);
        //
        Collection<ExpensesByCategory> actualExpenses = expenseService.getExpensesByCategories();
        assertInstanceOf(Collection.class, actualExpenses);
        assertIterableEquals(EXPENSES_BY_CATEGORY_COLLECTION, actualExpenses);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }

    @Test
    @DisplayName(value = "getExpensesByCategories(): return Collection<>")
    @Order(10)
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void getExpensesByCategories2() {
        when(expenseRepository.getExpenseByCategories())
                .thenReturn(new ArrayList<>());
        //
        Collection<ExpensesByCategory> actualExpenses = expenseService.getExpensesByCategories();
        assertInstanceOf(Collection.class, actualExpenses);
        assertIterableEquals(Collections.EMPTY_LIST, actualExpenses);
        assertDoesNotThrow(() -> expenseService.getAllExpenses());
    }
}