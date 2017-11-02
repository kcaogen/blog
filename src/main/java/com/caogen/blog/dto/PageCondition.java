package com.caogen.blog.dto;

public class PageCondition {

    private int offset;

    private int pageSize;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageCondition{" +
                "offset=" + offset +
                ", pageSize=" + pageSize +
                '}';
    }
}
