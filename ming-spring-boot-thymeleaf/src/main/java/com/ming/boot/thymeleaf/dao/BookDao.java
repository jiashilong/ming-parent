package com.ming.boot.thymeleaf.dao;

import com.ming.boot.thymeleaf.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> getBookList();
}
