package com.pipilong.service.Impl;

import com.pipilong.domain.ParserResult;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.Parser;
import com.pipilong.service.abstracts.AbstractParser;
import com.pipilong.util.DateConvert;
import org.springframework.stereotype.Service;
/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description pcap文件解析器
 */
@Service
public class PcapParser extends AbstractParser {

    private final Parser ethernetParser = new EthernetParser();
    private final Parser internetParser = new InternetParser();

    private final Parser transportParser = new TransportParser();

    private final Parser applicationLayerParser = new ApplicationLayerParser();

    public void parser(byte[] data) {
        byte magic1 = data[pointer.get()];
        if (magic1 == (byte) 161) {
            boolean isBig = true;
        }
        //解析pcap header
        pointer.set(4);
        int major = convertToInt(data, pointer.get() + 1, 2);
        int minor = convertToInt(data, pointer.get() + 1, 2);
        int thisZone = convertToInt(data, pointer.get() + 3, 4);
        int sigFigs = convertToInt(data, pointer.get() + 3, 4);
        int snapLen = convertToInt(data, pointer.get() + 3, 4);
        int linkType = convertToInt(data, pointer.get() + 3, 4);
        System.out.println("major:" + major + "  minor:" + minor + "" +
                "  thisZone:" + thisZone + "  sigFigs:" + sigFigs + "  snapLen:" + snapLen + "  linkType:" + linkType);

        if (pointer.get() >= data.length) return;

        do {
            //解析每个packet
            //解析packet header
            long highTimestamp = convertToLong(data, pointer.get() + 3, 4);
            long lowTimestamp = convertToLong(data, pointer.get() + 3, 4);
            String date = DateConvert.convert(highTimestamp * 1000L) + "." + lowTimestamp;
            int capLen = convertToInt(data, pointer.get() + 3, 4);
            int Len = convertToInt(data, pointer.get() + 3, 4);
            System.out.println("[" + date + "] " + capLen + " Bytes");
            //解析数据包的数据部分
            //解析数据部分的链路层
            ParserResult ethernetResult = ethernetParser.parser(data, ProtocolType.ETHERNET, pointer.get());
            pointer.set(pointer.get()+ethernetResult.getDataLength());
//            if(ethernetResult.getNextProtocol() == null){
//                continue;
//            }
            //解析数据部分的网络层
            ParserResult internetResult = internetParser.parser(data, ethernetResult.getNextProtocol(), pointer.get());
            pointer.set(pointer.get()+internetResult.getDataLength());

//            if(internetResult.getNextProtocol() == null){
//                continue;
//            }
            //解析数据部分的传输层
            ParserResult transportResult = transportParser.parser(data, internetResult.getNextProtocol(), pointer.get());
            pointer.set(pointer.get()+transportResult.getDataLength());

            //解析数据部分的应用层
            ParserResult applicationLayerResult = applicationLayerParser.parser(data, transportResult.getNextProtocol(), pointer.get());
            pointer.set(pointer.get()+applicationLayerResult.getDataLength());

            //调整指针位置
            pointer.set(pointer.get()+capLen-ethernetResult.getDataLength() - internetResult.getDataLength()-transportResult.getDataLength()-applicationLayerResult.getDataLength());

            System.out.print("\n");
        } while (pointer.get() < data.length);

    }

}
