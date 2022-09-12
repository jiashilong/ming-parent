package com.ming.boot.freemarker.dao.impl;


import com.ming.boot.freemarker.dao.BookDao;
import com.ming.boot.freemarker.model.Book;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookDaoImpl implements BookDao {
    private List<Book> books = new ArrayList<>();

    @PostConstruct
    public void init() {
        books.add(new Book(1, "红楼梦", "曹雪芹"));
        books.add(new Book(2, "三国演绎", "罗贯中"));
    }

    @Override
    public List<Book> getBookList() {
        return books;
    }
}
