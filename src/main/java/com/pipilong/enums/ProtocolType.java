package com.pipilong.enums;



public enum ProtocolType {
    IPv4, ARP, IPv6, ICMP, IGMP, IP, TCP, EGP, IGP, UDP, ESP, AH, ICMP_IPv6, OSPF, ETHERNET;
    public static ProtocolType getProtocol(String type){
        if("80".equals(type)){
            return IPv4;
        }else if("86".equals(type)){
            return ARP;
        }else if("86dd".equals(type)){
            return IPv6;
        }
        return null;
    }

    public static ProtocolType getProtocol(int type){
        switch (type){
            case 1 : return ICMP;
            case 2 : return IGMP;
            case 4 : return IP;
            case 6 : return TCP;
            case 8 : return EGP;
            case 9 : return IGP;
            case 17 : return UDP;
            case 41 : return IPv6;
            case 50 : return ESP;
            case 51 : return AH;
            case 58 : return ICMP_IPv6;
            case 89 : return OSPF;
            default:return null;
        }
    }

}
