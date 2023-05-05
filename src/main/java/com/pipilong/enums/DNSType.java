package com.pipilong.enums;

/**
 * @author pipilong
 * @createTime 2023/5/5
 * @description
 */
public enum DNSType {
    A, NS, CNAME, SOA, WKS, PTR, HINFO, MX, AAAA, AXFR, ANY, TXT;

    public static DNSType getDNSType(int id){
        switch (id){
            case 1: return A;
            case 2: return NS;
            case 5: return CNAME;
            case 6: return SOA;
            case 11: return WKS;
            case 12: return PTR;
            case 13: return HINFO;
            case 15: return MX;
            case 16: return TXT;
            case 28: return AAAA;
            case 252: return AXFR;
            case 255: return ANY;
            default: return null;
        }
    }

}
