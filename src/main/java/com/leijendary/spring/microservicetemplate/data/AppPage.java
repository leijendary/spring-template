package com.leijendary.spring.microservicetemplate.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class AppPage<T> implements Serializable {

    private List<T> content = new ArrayList<>();
    private boolean first = false;
    private boolean last = false;
    private boolean empty = false;
    private long length;
    private long totalElements;
    private long totalPages;
    private long page;
    private long size;
    private long offset;

    @JsonIgnore
    private Pageable pageable;

    public AppPage() {
    }

    public AppPage(List<T> content, Pageable pageable, long totalElements) {
        this.content = content;
        this.pageable = pageable;
        this.length = content.size();
        this.totalElements = totalElements;
        this.page = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.offset = pageable.getOffset();
        this.totalPages = getSize() == 0 ? 1 : (int) Math.ceil((double) getTotalElements() / (double) getSize());
        this.first = getPage() == 0;
        this.last = getPage() + 1 == getTotalPages();
        this.empty = getTotalElements() == 0;
    }

    public static <T> AppPage<T> of(List<T> content, Pageable pageable, long total) {
        return new AppPage<>(content, pageable, total);
    }

    public <U> AppPage<U> map(Function<? super T, ? extends U> converter) {
        List<U> converted = getContent().stream()
                .map(converter)
                .collect(Collectors.toList());

        return new AppPage<>(converted, getPageable(), getTotalElements());
    }
}
