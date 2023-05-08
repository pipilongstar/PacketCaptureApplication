package com.pipilong.service.Impl;

import com.pipilong.domain.ParserResult;
import com.pipilong.enums.DNSType;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.abstracts.AbstractParser;
import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pipilong
 * @createTime 2023/5/5
 * @description
 */
@Service
public class ApplicationLayerParser extends AbstractParser {
    private final StringBuilder nameBuilder = new StringBuilder();
    private Pair<String,Integer> res;
    private final Map<Integer,String> dnsCache = new HashMap<>();
    private int startPosition;
    @Override
    public ParserResult parser(byte[] data, ProtocolType protocol, int position) {
        System.out.println("-----ApplicationLayer:"+protocol);
        this.startPosition = position;
        pointer.set(position);
        if(protocol == ProtocolType.HTTP){
            return httpParser(data);
        }else if(protocol == ProtocolType.DNS){
            dnsCache.clear();
            return dnsParser(data);
        }

        return new ParserResult(true,null,0);
    }

    public ParserResult httpParser(byte[] data){
        return new ParserResult(true,null,0);
    }

    public ParserResult dnsParser(byte[] data){
        //DNS协议解析
        //会话标识
        int transactionId = convertToInt(data,2);
        //标志
        int flags = convertToInt(data,2);
        int QR = (flags >>> 15) & 1;
        int opcode = flags & 0x7800;
        int AA = (flags >>> 10) & 1;
        int TC = (flags >>> 9) & 1;
        int RD = (flags >>> 8) & 1;
        int RA = (flags >>> 7) & 1;
        int rCode = flags & 0xf;
        //数量字段
        int Questions = convertToInt(data,2);
        int AnswerRRs = convertToInt(data,2);
        int AuthorityRRs = convertToInt(data,2);
        int AdditionalRRs = convertToInt(data,2);
        String DNSDataType = QR == 0 ? "query" : "response";
        System.out.println("("+DNSDataType+")");
        System.out.println("transactionId:0x"+Integer.toHexString(transactionId)+"  flags:0x"+Integer.toHexString(flags));
        System.out.println("Questions:"+Questions+"  AnswerRRs:"+AnswerRRs);
        System.out.println("AuthorityRRs:"+AuthorityRRs+"  AdditionalRRs:"+AdditionalRRs);
        int dataLength = 0;
        //解析Questions
        for(int i=1;i<=Questions;i++) {
            Pair<Integer, DNSType> questions = dnsRRParser(data, "Questions", i);
            dataLength += questions.getKey();
        }
        //解析资源记录RR区域
        //AnswerRRs
        for(int i=1;i<=AnswerRRs;i++) {
            Pair<Integer, DNSType> answerRRs = dnsRRParser(data, "AnswerRRs", i);
            dataLength += answerRRs.getKey();
            dataLength += dnsRRAdditionalParser(data,answerRRs.getValue());
        }
        //AuthorityRRs
        for(int i=1;i<=AuthorityRRs;i++) {
            Pair<Integer, DNSType> authorityRRs = dnsRRParser(data, "AuthorityRRs", i);
            dataLength += authorityRRs.getKey();
            dataLength += dnsRRAdditionalParser(data,authorityRRs.getValue());
        }
        //AdditionalRRs
        for(int i=1;i<=AdditionalRRs;i++) {
            Pair<Integer, DNSType> additionalRRs = dnsRRParser(data, "AdditionalRRs", i);
            dataLength += additionalRRs.getKey();
            dataLength += dnsRRAdditionalParser(data,additionalRRs.getValue());
        }
        return new ParserResult(true,null,12+dataLength);
    }

    private Pair<String,Integer> dnsParseName(byte[] data, int position, int size){
        int totalLength = 0;
        int length;
        while( (length=convertToIntByOffset(data,1,position)) != 0){
            if(length != 0xc0){
                position++;
                nameBuilder.append(convertToString(data, length, position));
                position+=length;
                totalLength += 1+length;
            }else{
                int p = startPosition+(data[position+1] & 0xff);
                length = convertToIntByOffset(data,1,p);
                p++;
                nameBuilder.append(convertToString(data,length,p));
                position += 2;
                totalLength += 2;
            }
            nameBuilder.append(".");
            if(size != -1 && totalLength>=size) break;
        }
        String name = nameBuilder.substring(0, nameBuilder.length() - 1);
        //清空builder中的数据
        nameBuilder.setLength(0);
        //最后以字节0 结尾
        if(length == 0) totalLength++;
        return new Pair<>(name,totalLength);
    }

    private Pair<Integer,DNSType> dnsRRParser(byte[] data,String RRType,int id){
        int totalLength = 0;
        //判断是否为压缩表示法
        byte isCompress = data[pointer.get()];
        int position;
        //计算name所在dns报文中的偏移位置
        if((isCompress & 0xff) != 0xc0) position = pointer.get();
        else position = startPosition+(data[pointer.get()+1] & 0xff);
        String name ;
        if(dnsCache.get(position) != null) {
            name = dnsCache.get(position);
        }else{
            //解析name字段
            res = dnsParseName(data,position,-1);
            name = res.getKey();
            dnsCache.put(position,name);
        }
        //重置指针位置
        if((isCompress & 0xff) != 0xc0) {
            pointer.set(pointer.get()+res.getValue());
//            pointer += res.getValue();
            totalLength += res.getValue();
        }
        else {
            pointer.set(pointer.get()+2);
//            pointer += 2;
            totalLength += 2;
        }
        //查询类型
        int typeId = convertToInt(data,2);
        DNSType type = DNSType.getDNSType(typeId);
        //查询类
        int queryClassId = convertToInt(data,2);
        String queryClass = queryClassId == 1 ? "IN" : null;
        System.out.println("["+RRType+id+"]:");
        System.out.println("name:"+name+"  type:"+type+"  Class:"+queryClass);
        totalLength+=4;

        return new Pair<>(totalLength,type);
    }

    private Integer dnsRRAdditionalParser(byte[] data,DNSType type){
        //生存时间
        int ttl = convertToInt(data,4);
        //资源数据长度
        int dataLength = convertToInt(data,2);
        String dataInfo = "";
        if(type == DNSType.A){
            dataInfo = dataParser(data,4,".",false);
        }else if(type == DNSType.AAAA){
            for(int i=1;i<=8;i++){
                dataInfo += dataParser(data,2,"",true);
                if(i!=8) dataInfo+=":";
            }
        }else if(type == DNSType.CNAME || type == DNSType.MX || type == DNSType.NS){
            byte isCompress = data[pointer.get()];
            int position;
            //计算name所在dns报文中的偏移位置
            if((isCompress & 0xff) != 0xc0) position = pointer.get();
            else position = startPosition+(data[pointer.get()+1] & 0xff);
            //解析name字段
            if(dnsCache.get(position) != null){
                dataInfo = dnsCache.get(position);
            }else{
                res = dnsParseName(data,position,dataLength);
                dataInfo = res.getKey();
                dnsCache.put(position,dataInfo);
            }

            if((isCompress & 0xff) != 0xc0) pointer.set(pointer.get()+res.getValue());
            else pointer.set(pointer.get()+2);
        }else if(type == DNSType.TXT){
            dataInfo = convertToString(data, dataLength, pointer.get());
        }
        System.out.println("ttl:"+ttl+"  dataLength:"+dataLength);
        System.out.println(type+":"+dataInfo);
        return 6+dataLength;
    }

}
























































