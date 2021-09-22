package com.p5.adoptions.service.DTO;

import java.util.ArrayList;
import java.util.List;

public class ListDTO<T> {
    private Long totalCount;
    private List<T> data = new ArrayList<>();

    public ListDTO() {
    }

    public ListDTO(Long totalCount, List<T> data) {
        this.totalCount = totalCount;
        this.data = data;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
