package com.pipilong.enums;



public enum ProtocolType {
    IPv4, ARP, IPv6, ICMP, IGMP, IP, TCP, EGP, IGP, UDP, ESP, AH, ICMP_IPv6, OSPF, ETHERNET,
    HTTP, HTTPS, SSH, Telnet, SMTP, POP3, IMAP, DNS, FTPController, FTPData, MDNS;
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
            default: return null;
        }
    }

    public static ProtocolType getProtocolByPort(int port){
        switch (port){
            case 80: return HTTP;
            case 443: return HTTPS;
            case 20: return FTPData;
            case 21: return FTPController;
            case 22: return SSH;
            case 23: return Telnet;
            case 25: return SMTP;
            case 110: return POP3;
            case 143: return IMAP;
            case 53: return DNS;
            case 5353: return MDNS;
            default: return null;
        }
    }

}



































