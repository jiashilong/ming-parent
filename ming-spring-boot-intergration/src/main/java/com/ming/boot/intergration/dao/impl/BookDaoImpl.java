package com.ming.boot.intergration.dao.impl;

import com.ming.boot.intergration.dao.BookDao;
import com.ming.boot.intergration.model.Book;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BookDaoImpl implements BookDao {
    private List<Book> books = new ArrayList<>();

    @PostConstruct
    public void init() {
        books.add(new Book(1, "三国演义", "罗贯中", new Date()));
        books.add(new Book(2, "红楼梦", "曹雪芹", new Date()));
    }

    @Override
    public List<Book> getBookList() {
        return books;
    }
}
