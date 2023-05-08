package com.pipilong.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@Component
@Data
public class Packet {

    long highTimestamp;
    long lowTimestamp;
    long capLen;
    long len;

}
