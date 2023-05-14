package com.pipilong.service.Impl;

import com.pipilong.domain.ParserResult;
import com.pipilong.domain.packet.ARPPacket;
import com.pipilong.domain.packet.IPv4Packet;
import com.pipilong.domain.packet.IPv6Packet;
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
public class InternetParser extends AbstractParser {
    @Override
    public ParserResult parser(byte[] data, ProtocolType protocol, int position) {
        System.out.println("-----internet:"+protocol);
        this.pointer = position;
        if(protocol == ProtocolType.IPv4){
           return ipv4Parser(data);
        }else if(protocol == ProtocolType.ARP){
            return arpParser(data);
        }else if(protocol == ProtocolType.IPv6){
            return ipv6Parser(data);
        }

        packetData.setInternet(null);
        return new ParserResult(true,null,0);
    }

    private ParserResult ipv6Parser(byte[] data) {
        int versionAndTrafficClassAndFlowLabel = convertToInt(data,4);
        //版本号
        int version = versionAndTrafficClassAndFlowLabel >>> 28;
        //通信号类
        int trafficClass = (versionAndTrafficClassAndFlowLabel >>> 20) & 0xff;
        //游标号
        int flowLabel = versionAndTrafficClassAndFlowLabel & 0xfffff;
        int payLoadLength = convertToInt(data,2);
        int nextHeader = convertToInt(data,1);
        ProtocolType protocolType = ProtocolType.getProtocol(nextHeader);
        int hopLimit = convertToInt(data,1);
        String sourceAddress = "";
        for(int i=1;i<=8;i++){
            sourceAddress += dataParser(data,2,"",true);
            if(i != 8){
                sourceAddress += ":";
            }
        }
        String destinationAddress = "";
        for(int i=1;i<=8;i++){
            destinationAddress += dataParser(data,2,"",true);
            if(i != 8){
                destinationAddress += ":";
            }
        }
        System.out.println("version:"+version+"  trafficClass:"+trafficClass+"  flowLabel:"+flowLabel);
        System.out.println("payLoadLength:"+payLoadLength+"  nextHeader:"+protocolType+"  hopLimit:"+hopLimit);
        System.out.println("sourceAddress:"+sourceAddress);
        System.out.println("destinationAddress:"+destinationAddress);

        packet.setSource(sourceAddress);
        packet.setDestination(destinationAddress);
        packet.setProtocol(ProtocolType.IPv6);
        packet.setInfo("payloadLength:"+payLoadLength+"  nextHeader:"+protocolType+"  hopLimit:"+hopLimit);

        IPv6Packet iPv6Packet = new IPv6Packet();
        iPv6Packet.setVersion(version);
        iPv6Packet.setFlowLabel("0x"+Integer.toHexString(flowLabel));
        iPv6Packet.setHopLimit(hopLimit);
        iPv6Packet.setNextHeader(protocolType);
        iPv6Packet.setDestinationAddress(destinationAddress);
        iPv6Packet.setSourceAddress(sourceAddress);
        iPv6Packet.setTrafficClass("0x"+Integer.toHexString(trafficClass));
        iPv6Packet.setPayLoadLength(payLoadLength);
        packetData.setInternet(new Pair<>(iPv6Packet,ProtocolType.IPv6));

        return new ParserResult(true,protocolType,40);
    }

    private ParserResult ipv4Parser(byte[] data){
        //版本和首部长度
        byte versionAndHeaderLength=data[pointer];
        pointer++;
        int version = (versionAndHeaderLength & 0xf0)>>>4;
        int headerLength = (versionAndHeaderLength & 0x0f);
        //区分服务
        int differentService = (data[pointer] & 0xff);
        pointer++;
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

        packet.setSource(sourceAddress);
        packet.setDestination(destinationAddress);
        packet.setProtocol(ProtocolType.IPv4);
        packet.setInfo("identification:"+identification+"  flag:"+flag+"  offset:"+offset);

        System.out.println("version:"+version+"  headerLength:"+headerLength+"  differentService:"+differentService+"  totalLength:"+totalLength);
        System.out.println("identification:"+identification+"  flag:"+flag+"  offset:"+offset);
        System.out.println("ttl:"+ttl+"  protocol:"+protocol+"  headerCheckSum:"+headerCheckSum);
        System.out.println("sourceAddress:"+sourceAddress);
        System.out.println("destinationAddress:"+destinationAddress);

        IPv4Packet iPv4Packet = new IPv4Packet();
        iPv4Packet.setFlag(flag);
        iPv4Packet.setIdentification("0x"+Integer.toHexString(identification));
        iPv4Packet.setOffset(offset);
        iPv4Packet.setTtl(ttl);
        iPv4Packet.setVersion(version);
        iPv4Packet.setDestinationAddress(destinationAddress);
        iPv4Packet.setDifferentService("0x"+Integer.toHexString(differentService));
        iPv4Packet.setSourceAddress(sourceAddress);
        iPv4Packet.setHeaderLength(headerLength);
        iPv4Packet.setTotalLength(totalLength);
        iPv4Packet.setHeaderCheckSum("0x"+headerCheckSum);
        iPv4Packet.setProtocol(protocol);
        packetData.setInternet(new Pair<>(iPv4Packet, ProtocolType.IPv4));

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

        ARPPacket arpPacket = new ARPPacket();
        arpPacket.setOperator(operator);
        arpPacket.setIpLength(ipLength);
        arpPacket.setHardWare(hardWareProtocol);
        arpPacket.setSenderIP(senderIP);
        arpPacket.setMacLength(macLength);
        arpPacket.setSenderMAC(senderMAC);
        arpPacket.setTargetIP(targetIP);
        arpPacket.setTargetMAC(targetMAC);
        arpPacket.setProtocol(protocol);
        packetData.setInternet(new Pair<>(arpPacket,ProtocolType.ARP));

        packet.setProtocol(ProtocolType.ARP);
        if(opcode == 1) packet.setInfo("Who has "+targetIP+"? Tell "+senderIP);
        else packet.setInfo(senderIP+" is at "+senderMAC);

        return new ParserResult(true,null,28);
    }
}














































