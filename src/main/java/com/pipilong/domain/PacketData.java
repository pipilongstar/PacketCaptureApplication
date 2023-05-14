package com.pipilong.domain;

import com.pipilong.enums.ProtocolType;

import com.pipilong.util.Pair;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author pipilong
 * @createTime 2023/5/12
 * @description
 */
@Component
@Data
public class PacketData{

    private Frame frame;
    private Pair<Object, ProtocolType> ethernet;
    private Pair<Object,ProtocolType> internet;
    private Pair<Object,ProtocolType> aTransport;
    private Pair<Object,ProtocolType> application;

}































