package com.caogen.blog.dto;

import java.util.List;

public class Page<T> {

    private int pageSize;
    private int pageNum;
    private int offSet;
    private long totalPages;
    private long totalCount;
    private List<T> result;

    public Page(int pageSize,Long totalCount,int pageNum) {
        this.setPageSize(pageSize);
        this.setTotalCount(totalCount);
        this.setPageNum(pageNum);
    }

    /**
     * 获取每页显示的数据量
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }
    /**
     * 设置每页显示的数据量
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * 获取当前页码
     * @return
     */
    public int getPageNum() {
        return pageNum;
    }
    /**
     * 设置当前页码
     * @param pageNum
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
        if(pageNum < 1) {
            this.pageNum = 1;
        } else if(pageNum > getTotalPages()) {
            this.pageNum = getTotalPages().intValue();
        }
        //根据当前页码自动计算起始行号
        setOffSet((this.pageNum - 1) * getPageSize());
    }
    /**
     * 获取起始行号
     * @return
     */
    public int getOffSet() {
        if (offSet < 0) offSet = 0;
        return offSet;
    }
    /**
     * 设置起始行号
     * @param offSet
     */
    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }
    /**
     * 获取总页数
     * @return
     */
    public Long getTotalPages() {
        return totalPages;
    }
    /**
     * 设置总页数
     * @param totalPages
     */
    private void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }
    /**
     * 获取总记录数
     * @return
     */
    public Long getTotalCount() {
        return totalCount;
    }
    /**
     * 设置总记录数
     * @param totalCount
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
        //根据总记录数自动计算总页数
        Long result = totalCount / getPageSize();
        if(totalCount % pageSize != 0) {
            result += 1;
        }
        setTotalPages(result);
    }
    /**
     * 获取当前页的数据集
     * @return
     */
    public List<T> getResult() {
        return result;
    }
    /**
     * 设置当前页的数据集
     * @param result
     */
    public void setResult(List<T> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Page{" +
                "pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                ", offSet=" + offSet +
                ", totalPages=" + totalPages +
                ", totalCount=" + totalCount +
                ", result=" + result +
                '}';
    }
}
