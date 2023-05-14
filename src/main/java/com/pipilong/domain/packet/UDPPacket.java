package com.pipilong.domain.packet;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author pipilong
 * @createTime 2023/5/11
 * @description
 */
@Component
@Data
public class UDPPacket {

    private int sourcePort;
    private int destinationPort;
    private int length;
    private String checkSum;

}
