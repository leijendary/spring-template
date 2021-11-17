package com.leijendary.spring.boot.template.data;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageMeta {

    private int numberOfElements;
    private int totalPages;
    private long totalElements;
    private int size;
    private int page;

    public PageMeta(final Page<?> page) {
        this.numberOfElements = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.size = page.getSize();
        this.page = page.getNumber() + 1;
    }
}
