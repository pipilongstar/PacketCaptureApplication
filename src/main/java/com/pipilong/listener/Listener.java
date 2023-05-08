package com.pipilong.listener;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
public interface Listener {

    void parse(byte[] packetHeader,byte[] packetData);

}
