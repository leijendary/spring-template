package com.leijendary.spring.microservicetemplate.data;

import lombok.Data;
import org.springframework.data.domain.Page;

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
        this.page = page.getNumber();
    }
}
