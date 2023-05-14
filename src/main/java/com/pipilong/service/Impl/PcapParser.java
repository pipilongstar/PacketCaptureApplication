package com.pipilong.service.Impl;

import com.pipilong.domain.Frame;
import com.pipilong.domain.Packet;
import com.pipilong.domain.ParserResult;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.Parser;
import com.pipilong.service.abstracts.AbstractParser;
import com.pipilong.util.DateConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description pcap文件解析器
 */
@Service
@Slf4j
public class PcapParser extends AbstractParser {

    private final EthernetParser ethernetParser;
    private final InternetParser internetParser;

    private final TransportParser transportParser;
    private final DataWriter dataWriter;

    private final ApplicationLayerParser applicationLayerParser;
    @Autowired
    public PcapParser(
            EthernetParser ethernetParser,
            InternetParser internetParser,
            TransportParser transportParser,
            DataWriter dataWriter,
            ApplicationLayerParser applicationLayerParser
    ){
        this.ethernetParser = ethernetParser;
        this.internetParser = internetParser;
        this.transportParser = transportParser;
        this.dataWriter = dataWriter;
        this.applicationLayerParser = applicationLayerParser;
    }
    public List<Packet> parser(byte[] data) throws IOException, CloneNotSupportedException {
        pointer=0;
        List<Packet> list = new ArrayList<>();
        int id=1;
        byte magic1 = data[pointer];
        if (magic1 == (byte) 161) {
            boolean isBig = true;
        }
        //解析pcap header
        pointer = 4;
        int major = convertToInt(data, pointer + 1, 2);
        int minor = convertToInt(data, pointer + 1, 2);
        int thisZone = convertToInt(data, pointer + 3, 4);
        int sigFigs = convertToInt(data, pointer + 3, 4);
        int snapLen = convertToInt(data, pointer + 3, 4);
        int linkType = convertToInt(data, pointer + 3, 4);
        System.out.println("major:" + major + "  minor:" + minor + "" +
                "  thisZone:" + thisZone + "  sigFigs:" + sigFigs + "  snapLen:" + snapLen + "  linkType:" + linkType);

        if (pointer >= data.length) return null;

        do {
            //解析每个packet
            //解析packet header
            long highTimestamp = convertToLong(data, pointer + 3, 4);
            long lowTimestamp = convertToLong(data, pointer + 3, 4);
            String date = DateConvert.convert(highTimestamp * 1000L) + "." + lowTimestamp;
            int capLen = convertToInt(data, pointer + 3, 4);
            int Len = convertToInt(data, pointer + 3, 4);
            System.out.println("[" + date + "] " + capLen + " Bytes");

            packet.setId(id);
            packet.setTime(date);
            packet.setLength(capLen);

            Frame frame = new Frame();
            frame.setFrameId(id++);
            frame.setFrameLength(capLen);
            frame.setArrivalTime(date);
            packetData.setFrame(frame);

            //解析数据包的数据部分
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
            //调整指针位置
            pointer += (capLen-ethernetResult.getDataLength() - internetResult.getDataLength()-transportResult.getDataLength()-applicationLayerResult.getDataLength());
            System.out.print("\n");

            dataWriter.setDetailOfPacket(packetData);

            list.add(packet.clone());
        } while (pointer < data.length);

        return list;
    }

}
