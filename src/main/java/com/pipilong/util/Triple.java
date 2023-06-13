package com.pipilong.util;

/**
 * @author pipilong
 * @createTime 2023/5/15
 * @description
 */
public class Triple<T1, T2, T3> {
    private T1 firstElement;
    private T2 secondElement;
    private T3 thirdElement;

    public Triple(){}

    public Triple(T1 firstElement, T2 secondElement, T3 thirdElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
        this.thirdElement = thirdElement;
    }

    public T1 getFirstElement() {
        return firstElement;
    }

    public T2 getSecondElement() {
        return secondElement;
    }

    public T3 getThirdElement() {
        return thirdElement;
    }

    public void setFirstElement(T1 firstElement) {
        this.firstElement = firstElement;
    }

    public void setSecondElement(T2 secondElement) {
        this.secondElement = secondElement;
    }

    public void setThirdElement(T3 thirdElement) {
        this.thirdElement = thirdElement;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "firstElement=" + firstElement +
                ", secondElement=" + secondElement +
                ", thirdElement=" + thirdElement +
                '}';
    }
}

