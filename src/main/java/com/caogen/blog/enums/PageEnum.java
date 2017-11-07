package com.caogen.blog.enums;

public enum PageEnum {
    PageSize(10);

    private int pageSize;

    private PageEnum(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageEnum{" +
                "pageSize=" + pageSize +
                '}';
    }
}
