package com.zw.book.service;

import com.zw.book.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookService extends CrudRepository<Book, Integer> {

    Iterable<Book> findByNameAndIsbn(String name, String isbn);

    Iterable<Book> findByName(String name);

    Iterable<Book> findByIsbn(String isbn);
}
