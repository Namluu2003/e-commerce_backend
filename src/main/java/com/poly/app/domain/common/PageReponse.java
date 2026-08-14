package com.poly.app.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PageReponse<T> {
    private List<T> data;
    private long totalPages;
    private int currentPage;
    private long totalElements;

    public PageReponse(Page<T> page) {
        this.data = page.getContent();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.totalElements = page.getTotalElements();
    }
}
