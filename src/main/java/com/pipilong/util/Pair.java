package com.pipilong.util;

import lombok.Data;

/**
 * @author pipilong
 * @createTime 2023/5/12
 * @description
 */
@Data
public class Pair <K,V>{
    K key;
    V value;

    public Pair() {
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
