package com.pipilong.exception;

/**
 * @author pipilong
 * @createTime 2023/5/10
 * @description
 */
public class DataLengthOverException extends Exception{

    public DataLengthOverException(String s) {
        super(s);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
