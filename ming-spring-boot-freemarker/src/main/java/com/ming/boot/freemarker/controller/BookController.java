package com.ming.boot.freemarker.controller;


import com.ming.boot.freemarker.dao.BookDao;
import com.ming.boot.freemarker.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookDao bookDao;

    @GetMapping("/api")
    @ResponseBody
    public List<Book> api() {
        return bookDao.getBookList();
    }

    @GetMapping("/view")
    public ModelAndView view() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("books", bookDao.getBookList());
        mv.setViewName("books");
        return mv;
    }
}
