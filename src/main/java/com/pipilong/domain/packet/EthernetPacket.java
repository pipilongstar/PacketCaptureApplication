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
public class EthernetPacket {

    String destination;
    String source;
    ProtocolType type;

}
