package com.caogen.blog.dto;

public class BlogResult<T> {

    private boolean result;

    private T data;

    private String error;

    public BlogResult(boolean result, T data) {
        this.result = result;
        this.data = data;
    }

    public BlogResult(boolean result, String error) {
        this.result = result;
        this.error = error;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BlogResult{" +
                "result=" + result +
                ", data=" + data +
                ", error='" + error + '\'' +
                '}';
    }
}
