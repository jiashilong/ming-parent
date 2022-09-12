package com.ming.boot.intergration.controller;

import com.ming.boot.intergration.model.Book;
import com.ming.boot.intergration.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/list")
    List<Book> getBookList() {
        return bookService.getBookList();
    }
}
