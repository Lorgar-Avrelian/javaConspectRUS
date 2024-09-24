package lorgar.avrelian.javaconspectrus.controllers;

import lorgar.avrelian.javaconspectrus.models.Book;
import lorgar.avrelian.javaconspectrus.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping(path = "/books")
public class BooksController {
    private final BookService bookService;

    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping                    // http://localhost:8080/books      C - create
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.createBook(book));
    }

    @GetMapping(path = "/{id}")     // http://localhost:8080/books/1    R - read
    public ResponseEntity<Book> readBook(@PathVariable long id) {
        Book findedBook = bookService.findBook(id);
        if (findedBook != null) {
            return ResponseEntity.ok(findedBook);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping                     // http://localhost:8080/books      U - update
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Book updatedBook = bookService.editBook(book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(path = "/{id}")  // http://localhost:8080/books/1    D - delete
    public ResponseEntity<Book> deleteBook(@PathVariable long id) {
        Book deletedBook = bookService.deleteBook(id);
        if (deletedBook != null) {
            return ResponseEntity.ok(deletedBook);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Book>> getAllBooks() {
        return ResponseEntity.status(200).body(bookService.getAllBooks());
    }
}
