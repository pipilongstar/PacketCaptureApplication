package com.pipilong.service.Impl;

import com.pipilong.domain.ParserResult;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.Parser;
import com.pipilong.service.abstracts.AbstractParser;
import com.pipilong.util.DateConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

    @Autowired
    public RawPacketParser(
            EthernetParser ethernetParser,
            InternetParser internetParser,
            TransportParser transportParser,
            ApplicationLayerParser applicationLayerParser) {
        this.ethernetParser = ethernetParser;
        this.internetParser = internetParser;
        this.transportParser = transportParser;
        this.applicationLayerParser = applicationLayerParser;
    }

    @Async
    @Override
    public void parser(byte[] data) {
        pointer.set(0);
        System.out.println(Thread.currentThread().getName());
        //解析每个packet
        //解析packet header
        int highTimestamp = convertToInt(data, pointer.get() + 3, 4);
        int lowTimestamp = convertToInt(data, pointer.get() + 3, 4);
        String date = DateConvert.convert(highTimestamp * 1000L) + "." + lowTimestamp;
        int capLen = convertToInt(data, pointer.get() + 3, 4);
        int Len = convertToInt(data, pointer.get() + 3, 4);
        System.out.println("[" + date + "] " + capLen + " Bytes");
        //解析原始数据包数据包的数据部分
        //解析数据部分的链路层
        ParserResult ethernetResult = ethernetParser.parser(data, ProtocolType.ETHERNET, pointer.get());
        pointer.set(pointer.get()+ethernetResult.getDataLength());

        //解析数据部分的网络层
        ParserResult internetResult = internetParser.parser(data, ethernetResult.getNextProtocol(), pointer.get());
        pointer.set(pointer.get()+internetResult.getDataLength());

        //解析数据部分的传输层
        ParserResult transportResult = transportParser.parser(data, internetResult.getNextProtocol(), pointer.get());
        pointer.set(pointer.get()+transportResult.getDataLength());

        //解析数据部分的应用层
        ParserResult applicationLayerResult = applicationLayerParser.parser(data, transportResult.getNextProtocol(), pointer.get());
        pointer.set(pointer.get()+applicationLayerResult.getDataLength());

        System.out.print("\n");

    }

}