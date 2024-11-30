package lorgar.avrelian.javaconspectrus.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lorgar.avrelian.javaconspectrus.models.Expense;
import lorgar.avrelian.javaconspectrus.models.ExpensesByCategory;
import lorgar.avrelian.javaconspectrus.services.ExpenseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/expenses")
@Tag(name = "6 Затраты", description = "Контроллер для работы с затратами")
// Включает поддержку базовой аутентификации
// Swagger UI для методов данного контроллера
@SecurityRequirement(name = "basicAuth")
public class ExpensesController {
    private final ExpenseService expenseService;

    public ExpensesController(@Qualifier("expenseServiceImpl") ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    @Operation(
            summary = "Все затраты",
            description = "Посмотреть все затраты",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = Expense.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<Expense>> getExpenses(@RequestParam(required = false) @Parameter(description = "Номер страницы", required = true, schema = @Schema(implementation = Long.class), example = "1") Integer page, @RequestParam(required = false) @Parameter(description = "Размер страницы", required = true, schema = @Schema(implementation = Long.class), example = "4") Integer size) {
        Collection<Expense> expenses;
        if (page == null && size == null) {
            expenses = expenseService.getAllExpenses();
        } else if (page != null && size != null && size > 0 && page > 0) {
            expenses = expenseService.getAllExpenses(page, size);
        } else {
            return ResponseEntity.badRequest().build();
        }
        if (expenses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expenses);
        }
    }

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Посмотреть",
            description = "Посмотреть затрату по ID",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Expense.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Expense> getExpenseById(@PathVariable("id") @Parameter(description = "ID записи о затратах", required = true, schema = @Schema(implementation = Long.class), example = "1") int id) {
        Expense expense = expenseService.getExpense(id);
        if (expense == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expense);
        }
    }

    @PostMapping
    @Operation(
            summary = "Внести",
            description = "Внести затрату",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Expense.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        Expense addedExpense = expenseService.addExpense(expense);
        if (addedExpense == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok(addedExpense);
        }
    }

    @GetMapping(path = "/categories")
    @Operation(
            summary = "Затраты по категориям",
            description = "Посмотреть все затраты по категориям",
            tags = "Затраты",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = ExpensesByCategory.class))
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    schema = @Schema(implementation = Void.class)
                            )
                    )
            }
    )
    public ResponseEntity<Collection<ExpensesByCategory>> getExpensesByCategories() {
        Collection<ExpensesByCategory> expenses = expenseService.getExpensesByCategories();
        if (expenses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(expenses);
        }
    }
}
