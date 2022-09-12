package com.ming.boot.intergration.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class Book {
    private long id;
    private String name;
    private String author;
    private Date publicationDate;
}
