package com.pipilong.domain.packet;

import com.pipilong.enums.ProtocolType;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author pipilong
 * @createTime 2023/5/11
 * @description
 */
@Component
@Data
public class ARPPacket {

    private ProtocolType hardWare;
    private ProtocolType protocol;
    private int macLength;
    private int ipLength;
    private String operator;
    private String senderMAC;
    private String senderIP;
    private String targetMAC;
    private String targetIP;

}





































