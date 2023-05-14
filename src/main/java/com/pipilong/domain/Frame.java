package com.pipilong.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author pipilong
 * @createTime 2023/5/12
 * @description
 */
@Component
@Data
public class Frame implements Cloneable{

    private String interfaceName;
    private String interfaceDescription;
    private String arrivalTime;
    private int frameId;
    private int frameLength;


    @Override
    public Frame clone() {
        try {
            return (Frame) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
