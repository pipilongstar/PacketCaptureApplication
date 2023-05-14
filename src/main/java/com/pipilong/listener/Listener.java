package com.pipilong.listener;

import java.io.IOException;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
public interface Listener {

    void parse(byte[] packetHeader,byte[] packetData, int id) throws IOException;

}
