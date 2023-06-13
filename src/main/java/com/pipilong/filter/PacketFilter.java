package com.pipilong.filter;

import com.pipilong.domain.PacketData;
import com.pipilong.enums.ProtocolType;
import com.pipilong.util.Triple;

import java.util.*;

/**
 * @author pipilong
 * @createTime 2023/5/15
 * @description
 */
public class PacketFilter {

    private List<Triple<ProtocolType,String,String>> list = new ArrayList<>();

    public void setListItem(Triple<ProtocolType,String,String> triple){
        list.add(triple);
    }

    public boolean match(PacketData packetData){
        return true;
    }

    public void removeLast(){
        int idx=list.size()-1;
        list.remove(idx);
    }

    public void print(){
        list.forEach(System.out::println);
    }
}
