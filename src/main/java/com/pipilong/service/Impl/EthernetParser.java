package com.pipilong.service.Impl;

import com.pipilong.domain.PacketData;
import com.pipilong.domain.ParserResult;
import com.pipilong.domain.packet.EthernetPacket;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.abstracts.AbstractParser;
import com.pipilong.util.Pair;
import org.springframework.stereotype.Service;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
@Service
public class EthernetParser extends AbstractParser {

    @Override
    public ParserResult parser(byte[] data, ProtocolType protocol, int position) {
        this.pointer = position;
        //解析目的MAC
        String destination = dataParser(data,6,"-",true);
        //解析源MAC
        String source = dataParser(data,6,"-",true);
        //解析类型
        String type = dataParser(data,2,"",true);
        ProtocolType nextProtocol = ProtocolType.getProtocol(type);
        System.out.println("-----ethernet:");
        System.out.println("destination:"+destination+"  source:"+source+"  type:"+nextProtocol);
        packet.setInfo(source+"--->"+destination);
        packet.setProtocol(ProtocolType.ETHERNET);
        packet.setSource(source);
        packet.setDestination(destination);

        //存储详细信息
        EthernetPacket ethernetPacket = new EthernetPacket();
        ethernetPacket.setDestination(destination);
        ethernetPacket.setSource(source);
        ethernetPacket.setType(nextProtocol);
        packetData.setEthernet(new Pair<>(ethernetPacket,ProtocolType.ETHERNET));

        return new ParserResult(true,nextProtocol,14);
    }
}









































