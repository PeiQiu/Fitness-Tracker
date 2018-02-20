package com.example.demo.domain;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

public class SpringPageable implements Serializable, Pageable{

    private Integer pagenumber = 1;

    private Integer pagesize = 10;

    private Sort sort;




    @Override
    public int getPageNumber() {
        return getPagenumber();
    }

    @Override
    public int getPageSize() {
        return getPagesize();
    }

    @Override
    public int getOffset() {
        return ( getPagenumber() - 1 ) * getPagesize();
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    public Integer getPagenumber() {
        return pagenumber;
    }

    public void setPagenumber(Integer pagenumber) {
        this.pagenumber = pagenumber;
    }

    public Integer getPagesize() {
        return pagesize;
    }

    public void setPagesize(Integer pagesize) {
        this.pagesize = pagesize;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
