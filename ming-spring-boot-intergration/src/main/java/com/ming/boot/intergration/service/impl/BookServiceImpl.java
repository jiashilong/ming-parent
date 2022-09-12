package com.ming.boot.intergration.service.impl;

import com.ming.boot.intergration.dao.BookDao;
import com.ming.boot.intergration.model.Book;
import com.ming.boot.intergration.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookDao bookDao;

    @Override
    public List<Book> getBookList() {
        return bookDao.getBookList();
    }
}
