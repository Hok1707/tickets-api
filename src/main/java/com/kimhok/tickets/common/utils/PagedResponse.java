package com.kimhok.tickets.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> items;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int pageSize;
}

