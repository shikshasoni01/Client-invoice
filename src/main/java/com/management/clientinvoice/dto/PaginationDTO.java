package com.biz4solutions.clientinvoice.dto;


import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginationDTO<T> {

    private List<T> list;

    private long totalCount;

    private long totalPages;

    private Integer limit;

    /**
     * @param list          Response list.
     * @param totalPages    Total Pages count.
     * @param totalElements Total Elements in a page count.
     */
    public PaginationDTO(List<T> list, long totalPages, long totalElements) {
        this.list = list;
        this.totalCount = totalElements;
        this.totalPages = totalPages;
    }

}



