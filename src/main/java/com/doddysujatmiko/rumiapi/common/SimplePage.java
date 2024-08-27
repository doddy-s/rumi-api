package com.doddysujatmiko.rumiapi.common;

import lombok.Data;

import java.util.List;

@Data
public class SimplePage {
    private int maxPage;
    private int currentPage;
    private boolean hasNextPage;
    private List<?> list;
}
