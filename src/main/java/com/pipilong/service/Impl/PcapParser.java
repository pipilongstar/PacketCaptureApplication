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

    public void parser(byte[] data) {
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

        if (pointer >= data.length) return;

        do {
            //解析每个packet
            //解析packet header
            long highTimestamp = convertToLong(data, pointer + 3, 4);
            long lowTimestamp = convertToLong(data, pointer + 3, 4);
            String date = DateConvert.convert(highTimestamp * 1000L) + "." + lowTimestamp;
            int capLen = convertToInt(data, pointer + 3, 4);
            int Len = convertToInt(data, pointer + 3, 4);
            System.out.println("[" + date + "] " + capLen + " Bytes");
            //解析数据包的数据部分
            //解析数据部分的链路层
            ParserResult ethernetResult = ethernetParser.parser(data, ProtocolType.ETHERNET, pointer);
            pointer += ethernetResult.getDataLength();
//            if(ethernetResult.getNextProtocol() == null){
//                continue;
//            }
            //解析数据部分的网络层
            ParserResult internetResult = internetParser.parser(data, ethernetResult.getNextProtocol(), pointer);
            pointer += internetResult.getDataLength();
//            if(internetResult.getNextProtocol() == null){
//                continue;
//            }
            //调整指针位置
            pointer += (capLen - ethernetResult.getDataLength() - internetResult.getDataLength());
        } while (pointer < data.length);

    }

}