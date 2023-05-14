package com.pipilong.service.Impl;

import com.pipilong.domain.Packet;
import com.pipilong.domain.PacketData;
import com.pipilong.domain.ParserResult;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.Parser;
import com.pipilong.service.abstracts.AbstractParser;
import com.pipilong.util.DateConvert;
import com.pipilong.util.KryoSerializer;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@Service
public class RawPacketParser extends AbstractParser {
    private final Parser ethernetParser;

    private final Parser internetParser;

    private final Parser transportParser;

    private final Parser applicationLayerParser;

    private final DataWriter dataWriter;
    private final Packet packet;
    @Autowired
    public RawPacketParser(
            EthernetParser ethernetParser,
            InternetParser internetParser,
            TransportParser transportParser,
            ApplicationLayerParser applicationLayerParser,
            Packet packet,
            DataWriter dataWriter) {
        this.ethernetParser = ethernetParser;
        this.internetParser = internetParser;
        this.transportParser = transportParser;
        this.applicationLayerParser = applicationLayerParser;
        this.packet = packet;
        this.dataWriter=dataWriter;
    }

    public Packet parser(byte[] data,int id) throws IOException {
        pointer = 0;
        System.out.println(Thread.currentThread().getName());
        //解析每个packet
        //解析packet header
        int highTimestamp = convertToInt(data, pointer + 3, 4);
        int lowTimestamp = convertToInt(data, pointer + 3, 4);
        String date = DateConvert.convert(highTimestamp * 1000L) + "." + lowTimestamp;
        int capLen = convertToInt(data, pointer + 3, 4);
        int Len = convertToInt(data, pointer + 3, 4);
        System.out.println("[" + date + "] " + capLen + " Bytes");
        packetData.getFrame().setArrivalTime(date);

        packet.setId(id);
        packet.setTime(date);
        packet.setLength(capLen);
        //解析原始数据包数据包的数据部分
        //解析数据部分的链路层
        ParserResult ethernetResult = ethernetParser.parser(data, ProtocolType.ETHERNET, pointer);
        pointer += ethernetResult.getDataLength();

        //解析数据部分的网络层
        ParserResult internetResult = internetParser.parser(data, ethernetResult.getNextProtocol(), pointer);
        pointer += internetResult.getDataLength();

        //解析数据部分的传输层
        ParserResult transportResult = transportParser.parser(data, internetResult.getNextProtocol(), pointer);
        pointer += transportResult.getDataLength();

        //解析数据部分的应用层
        ParserResult applicationLayerResult = applicationLayerParser.parser(data, transportResult.getNextProtocol(), pointer);
        pointer += applicationLayerResult.getDataLength();

        dataWriter.setDetailOfPacket(packetData);
        System.out.print("\n");

        return packet;
    }

}
























