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
public class IPv6Packet {

    private int version;
    private String trafficClass;
    private String flowLabel;
    private int payLoadLength;
    private ProtocolType nextHeader;
    private int hopLimit;
    private String sourceAddress;
    private String destinationAddress;

}





























