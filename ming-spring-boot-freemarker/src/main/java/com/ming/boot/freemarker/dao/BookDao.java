package com.ming.boot.freemarker.dao;


import com.ming.boot.freemarker.model.Book;

import java.util.List;

public interface BookDao {
    List<Book> getBookList();
}
