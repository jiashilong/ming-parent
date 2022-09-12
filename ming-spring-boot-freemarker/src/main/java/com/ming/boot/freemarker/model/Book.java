package com.ming.boot.freemarker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Book {
    private long id;
    private String name;
    private String author;
}
