package com.ming.boot.intergration.service.impl;

import com.alibaba.fastjson.JSON;
import com.ming.boot.intergration.BaseTest;
import com.ming.boot.intergration.model.Book;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class BookServiceImplTest extends BaseTest {
    @Autowired
    private BookServiceImpl bookService;

    @Test
    public void getBookList() {
        for (Book book : bookService.getBookList()) {
            System.out.println(JSON.toJSONString(book));
        }
    }
}