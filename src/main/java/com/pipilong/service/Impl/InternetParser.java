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
public class InternetParser extends AbstractParser {
    @Override
    public ParserResult parser(byte[] data, ProtocolType protocol, int position) {
        System.out.println("-----internet:"+protocol);
        this.pointer = position;
        if(protocol == ProtocolType.IPv4){
           return ipv4Parser(data);
        }else if(protocol == ProtocolType.ARP){
            return arpParser(data);
        }
        return new ParserResult(true,null,0);
    }

    private ParserResult ipv4Parser(byte[] data){
        //版本和首部长度
        byte versionAndHeaderLength=data[pointer++];
        int version = (versionAndHeaderLength & 0xf0)>>>4;
        int headerLength = (versionAndHeaderLength & 0x0f);
        //区分服务
        int differentService = (data[pointer++] & 0xff);
        //总长度
        int totalLength = convertToInt(data,2);
        //标识
        int identification = convertToInt(data,2);
        //标志和片偏移
        int flagAndOffset = convertToInt(data,2);
        int flag = (flagAndOffset & 0xE000) >>> 13;
        int offset = flagAndOffset & 0x1FFF;
        //生产时间ttl
        int ttl = convertToInt(data,1);
        //协议
        int type = convertToInt(data, 1);
        ProtocolType protocol = ProtocolType.getProtocol(type);
        //首部检验和
        String headerCheckSum = dataParser(data,2,"",true);
        //源地址
        String sourceAddress = dataParser(data,4,".",false);
        //目的地址
        String destinationAddress = dataParser(data,4,".",false);
        System.out.println("version:"+version+"  headerLength:"+headerLength+"  differentService:"+differentService+"  totalLength:"+totalLength);
        System.out.println("identification:"+identification+"  flag:"+flag+"  offset:"+offset);
        System.out.println("ttl:"+ttl+"  protocol:"+protocol+"  headerCheckSum:"+headerCheckSum);
        System.out.println("sourceAddress:"+sourceAddress);
        System.out.println("destinationAddress:"+destinationAddress+"\n");

        return new ParserResult(true,protocol,headerLength * 4);
    }

    private ParserResult arpParser(byte[] data){
         //硬件类型
        int hardWare = convertToInt(data,2);
        ProtocolType hardWareProtocol = null;
        if(hardWare == 1){
            hardWareProtocol = ProtocolType.ETHERNET;
        }
        //上层协议类型
        ProtocolType protocol = ProtocolType.getProtocol(dataParser(data, 2, "", true));
        //MAC地址长度
        int macLength = convertToInt(data,1);
        //IP地址长度
        int ipLength = convertToInt(data,1);
        //操作类型
        int opcode = convertToInt(data,2);
        String operator = opcode == 1 ? "request" : "reply";
        //源MAC地址
        String senderMAC = dataParser(data, 6, "-", true);
        //源IP地址
        String senderIP = dataParser(data, 4, ".", false);
        //目的MAC地址
        String targetMAC = dataParser(data, 6, "-", true);
        //目的IP地址
        String targetIP = dataParser(data, 4, ".", false);
        System.out.println("hardWare:"+hardWareProtocol+"  protocol:"+protocol);
        System.out.println("macLength:"+macLength+"  ipLength:"+ipLength+"  operator:"+operator);
        System.out.println("senderMAC:"+senderMAC+"  senderIP:"+senderIP);
        System.out.println("targetMAC:"+targetMAC+"  targetIP:"+targetIP);
        return new ParserResult(true,null,28);
    }
}














































