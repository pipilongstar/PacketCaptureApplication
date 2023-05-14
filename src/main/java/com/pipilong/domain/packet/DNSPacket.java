package com.pipilong.domain.packet;

import com.pipilong.enums.DNSType;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pipilong
 * @createTime 2023/5/11
 * @description
 */
@Component
@Data
public class DNSPacket {

    private String transactionId;
    private String flags;
    private Map<String,Integer> flagsMap = new HashMap<>();
    private int questions;
    private int answerRRs;
    private int authorityRRs;
    private int additionalRRs;
    private List<Queries> queriesList = new ArrayList<>();
    private List<RR> rrList = new ArrayList<>();
    private String DNSDataType;

    @Data
    public static class Queries{
        private String name;
        private DNSType type;
        private String queryClass;

        public Queries(String name, DNSType type, String queryClass) {
            this.name=name;
            this.type=type;
            this.queryClass=queryClass;
        }

        public Queries() {
        }
    }

    @Data
    public static class RR{
        private String name;
        private DNSType type;
        private String aClass;
        private int ttl;
        private int dataLength;
        private String data;

        public RR(String name, DNSType type, String aClass) {
            this.name = name;
            this.type = type;
            this.aClass = aClass;
        }

        public RR(String name, DNSType type, String aClass, int ttl, int dataLength, String data) {
            this.name = name;
            this.type = type;
            this.aClass = aClass;
            this.ttl = ttl;
            this.dataLength = dataLength;
            this.data = data;
        }

        public RR() {
        }
    }

}

















