package com.zw.book.controller;

import com.zw.book.model.Book;
import com.zw.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * The controller of the application. Uses a BookService to indirectly access
 * the BookDao and the corresponding entity. Mapped to <code>/api/v1/books</code>.
 * @author JonatanWang, codeSourc3
 */

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Creates a new entry in the database from the Book object recieved from the request body.
     *
     * @param book a Book object recieved from the request body of a HTTP request.
     * @returns a newly persisted Book object.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Book create(@Valid @RequestBody Book book) {

        return bookService.save(book);
    }

    /**
     * Delegates to the BookService's <code>findAll</code> method to load all Book objects from a database.
     *
     * @returns an Iterable of type Book
     * @see com.zw.book.service.BookService#findAll()
     */
    @GetMapping
    public Iterable<Book> read() {

        return bookService.findAll();
    }

    /**
     * Updates a Book object from a seperate Book object recieved in the request body. 
     *
     * @param a Book object from a request body. This method is used by Spring, not humans.  
     * @returns a ResponseEntity of type Book that's persisted along with the HttpStatus of OK if the id of the 
     * book from the request matches the id of an entity in the database. Otherwise it returns 
     * a ResponseEntity containing a unpersisted Book entity along with the HttpStatus of BAD_REQUEST.
     */
    @PutMapping
    public ResponseEntity<Book> update(@RequestBody Book book) {

        if (bookService.findById(book.getId()).isPresent()) {

            return new ResponseEntity<>(bookService.save(book), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(book, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delegates to the BookService's <code>deleteById</code> method to delete a Book from the database
     * by its id.
     *
     * @param id a wrapped <code style="color:red;">int</code>, mapped to the id specified in the url.
     * @see com.zw.book.service.BookService#deleteById(Integer id)
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {

        bookService.deleteById(id);
    }

    /**
     * Delegates to the BookService's <code>findById</code> method and returns an Optional of type Book.
     *
     * @param id a wrapper for an <code style="color:red;">int</code>
     * @returns an Optional of type Book.
     * @see com.zw.book.service.BookService#findById(Integer id)
     */
    @GetMapping("/{id}")
    public Optional<Book> findById(@PathVariable Integer id) {

        return bookService.findById(id);
    }

    /**
     * Searches and returns a Book or collection of Books based on the name and 
     * <abbr title="International Standard Book Number">ISBN</abbr>.
     *
     * @param name   a String representing the title of the book
     * @param isbn   a String representing the ISBN of the book. 
     * @returns  an Iterable of type Book if both or one of the parameters is supplied and a collection of Iterable Book
     * objects if neither parameters are supplied.
     */
    @GetMapping("/search")
    public Iterable<Book> findByQuery(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "isbn", required = false) String isbn) {
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
