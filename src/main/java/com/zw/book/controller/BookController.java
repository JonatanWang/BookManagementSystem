package com.zw.book.controller;

import com.zw.book.model.Book;
import com.zw.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Book create(@Valid @RequestBody Book book) {

        return bookService.save(book);
    }

    @GetMapping
    public Iterable<Book> read() {

        return bookService.findAll();
    }

    @PutMapping
    public ResponseEntity<Book> update(@RequestBody Book book) {

        if (bookService.findById(book.getId()).isPresent()) {

            return new ResponseEntity<>(bookService.save(book), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(book, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {

        bookService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Optional<Book> findById(@PathVariable Integer id) {

        return bookService.findById(id);
    }

    @GetMapping("/search")
    public Iterable<Book> findByQuery(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "isbn", required = false) String isbn
    ) {
        if (name != null && isbn != null) {

            return bookService.findByNameAndIsbn(name, isbn);
        } else if (name != null) {

            return bookService.findByName(name);
        } else if (isbn != null) {

            return bookService.findByIsbn(isbn);
        } else {

            return bookService.findAll();
        }
    }
}
