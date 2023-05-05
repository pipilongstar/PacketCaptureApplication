package com.pipilong.service.Impl;

import com.pipilong.domain.ParserResult;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.abstracts.AbstractParser;
import org.springframework.stereotype.Service;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
@Service
public class TransportParser extends AbstractParser {

    @Override
    public ParserResult parser(byte[] data, ProtocolType protocol, int position) {
        System.out.println("-----transport:"+protocol);
        this.pointer = position;
        if(protocol == ProtocolType.UDP){
            return udpParser(data);
        }else if(protocol == ProtocolType.TCP){
            return tcpParser(data);
        }
        return new ParserResult(true,null,0);
    }

    private ParserResult udpParser(byte[] data){
        ProtocolType protocolType;
        //源端口
        int sourcePort = convertToInt(data,2);
        protocolType = ProtocolType.getProtocolByPort(sourcePort);
        //目的端口
        int destinationPort = convertToInt(data,2);
        if(protocolType == null) protocolType = ProtocolType.getProtocolByPort(destinationPort);
        //udp长度
        int length = convertToInt(data,2);
        //校验和
        String checkSum = "0x" + dataParser(data,2,"",true);
        System.out.println("sourcePort:"+sourcePort+"  destinationPort:"+destinationPort);
        System.out.println("length:"+length+"  checkSum:"+checkSum);
        return new ParserResult(true,protocolType,8);
    }

    private ParserResult tcpParser(byte[] data){
        return new ParserResult(true,null,0);
    }
}































