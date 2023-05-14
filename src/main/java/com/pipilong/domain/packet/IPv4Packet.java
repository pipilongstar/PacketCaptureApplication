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
public class IPv4Packet {

    private int version;
    private int headerLength;
    private String differentService;
    private int totalLength;
    private String identification;
    private int flag;
    private int offset;
    private int ttl;
    private ProtocolType protocol;
    private String headerCheckSum;
    private String sourceAddress;
    private String destinationAddress;

}





























