package com.pipilong.service.Impl;

import com.pipilong.domain.PacketData;
import com.pipilong.enums.ProtocolType;
import com.pipilong.filter.PacketFilter;
import com.pipilong.util.Triple;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author pipilong
 * @createTime 2023/5/15
 * @description
 */
@Service
@Slf4j
public class DFAParser {

    private final List<PacketFilter> list = new ArrayList<>();
    private Set<Character> charSet;
    private final Map<Character, Integer>[] states = new HashMap[] {
            // 表格中的每一行对应一个 Map 对象
            new HashMap<Character,Integer>(){{
                put(' ',0);put('c',1);
            }},
            new HashMap<Character,Integer>(){{
                put(' ',4);put('.',2);put('c',1);
            }},
            new HashMap<Character,Integer>(){{
                put('c',3);
            }},
            new HashMap<Character,Integer>(){{
                put('c',3);put(' ',4);
            }},
            new HashMap<Character,Integer>(){{
                put(' ',4);put('=',5);put('&',8);put('|',8);
            }},
            new HashMap<Character,Integer>(){{
                put(' ',5);put('c',6);
            }},
            new HashMap<Character,Integer>(){{
                put(' ',7);
            }},
            new HashMap<Character,Integer>(){{
                put(' ',7);put('&',8);put('|',8);
            }},
            new HashMap<Character,Integer>(){{
                put('c',1);put(' ',8);
            }},
    };

    public DFAParser(){
        this.charSet = new HashSet<>(Arrays.asList('&','|',' ','.','='));
        for (char i = 'a' ; i<='z';i++){
            charSet.add(i);
        }
        for(char i= '0';i<='9';i++){
            charSet.add(i);
        }
    }

    public void setPacketFilter(PacketFilter packetFilter){
        list.add(packetFilter);
    }

    public boolean match(PacketData packetData){
        for(PacketFilter filter : list){
            if(filter.match(packetData)) return true;
        }
        return false;
    }

    public boolean parser(String rule){

        Map<Character,Integer> state = states[0];
        int currentState = 0;
        PacketFilter filter = new PacketFilter();
        Triple<ProtocolType, String, String> triple = new Triple<>();
        StringBuilder builder = new StringBuilder();
        for (char x : rule.toCharArray()){
            char t = characterConversion(x);
            Integer nextState = state.get(t);
            if(nextState == null) return false;
            if(t=='c') builder.append(x);
            //协议
            if(nextState==2||nextState==4&&currentState==1){
                ProtocolType protocol = ProtocolType.getProtocolByName(builder.toString());
                if(protocol == null) return false;
                triple.setFirstElement(protocol);
                builder.setLength(0);
            }
            //属性
            if(nextState==4&&currentState==3){
                triple.setSecondElement(builder.toString());
                builder.setLength(0);
            }
            //属性值
            if(nextState==7&&currentState==6){
                triple.setThirdElement(builder.toString());
                builder.setLength(0);
            }
            if(currentState==7){
                filter.setListItem(triple);
            }
            if(currentState==4&&nextState==8){
                filter.setListItem(triple);
            }
            //如果操作符是或的话，就将过滤器的最后一个元素移除，并将其添加到新的过滤器中。
            if(t=='|'){
                filter.removeLast();
                list.add(filter);
                filter=new PacketFilter();
                filter.setListItem(triple);
            }
            if(t=='|'||t=='&'){
                triple = new Triple<>();
            }
            state=states[nextState];
            currentState=nextState;
        }
        if(currentState==1) {
            ProtocolType protocol = ProtocolType.getProtocolByName(builder.toString());
            if(protocol == null) return false;
            triple.setFirstElement(protocol);
        }
        if(currentState==3) triple.setSecondElement(builder.toString());
        if(currentState==6) triple.setThirdElement(builder.toString());
        if(currentState==1||currentState==3||currentState==6||currentState==4){
            filter.setListItem(triple);
        }
        list.add(filter);

        return currentState == 7 || currentState == 4 || currentState==1 || currentState==3 || currentState==6;
    }

    private char characterConversion(char x){
        if(x>='a'&&x<='z'||x>='0'&&x<='9') return 'c';
        if(x=='&'||x=='|'||x=='.'||x==' '||x=='=') return x;
        return '!';
    }

    public void print(){
        list.forEach(PacketFilter::print);
        log.info("---------打印了:"+list.size());
    }

}























