package com.doddysujatmiko.rumiapi.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimplePage<T> {
    private int maxPage;
    private int currentPage;
    private boolean hasNextPage;
    private List<T> list;

    public static <T> SimplePage<T> fromPage(Page<T> page) {
        return SimplePage.<T>builder()
                .maxPage(page.getTotalPages() - 1)
                .currentPage(page.getNumber())
                .hasNextPage(page.hasNext())
                .list(page.getContent())
                .build();
    }

    public static <T> SimplePage<T> fromPageWithEmptyList(Page<?> page) {
        return SimplePage.<T>builder()
                .maxPage(page.getTotalPages() - 1)
                .currentPage(page.getNumber())
                .hasNextPage(page.hasNext())
                .build();
    }
}
