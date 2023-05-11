package com.pipilong.service.abstracts;

import com.pipilong.domain.Packet;
import com.pipilong.domain.ParserResult;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
@Service
public class AbstractParser implements Parser {
    protected int pointer =0;

    @Autowired
    protected Packet packet;
    protected StringBuffer builder = new StringBuffer();
    @Override
    public ParserResult parser(byte[] data, ProtocolType protocol, int position){return null;}


    /**
     * 按顺序解析数据
     * @param data 要解析的数据
     * @param size 解析的长度
     * @param separator 每个字节之间的分隔符
     * @param isHex 是否以16进制形式表示
     * @return 解析后的结果
     */
    protected String dataParser(byte[] data,int size,String separator,boolean isHex){

        for(int i=1;i<=size;i++){
//            byte b = data[pointer.get()];
//            pointer.set(pointer.get()+1);
            byte b = data[pointer++];
            if(isHex) builder.append(Integer.toHexString(b & 0xFF));
            else builder.append(b & 0xFF);
            if(i!=size) builder.append(separator);
        }
        String res=builder.toString();
        builder.setLength(0);
        return res;
    }

    /**
     * 将字节类型的数据转换为整型 -- 正序执行
     * @param data 要解析的数据
     * @param size 长度
     * @return 解析后的结果
     */
    protected int convertToInt(byte[] data,int size){
        int res=0;
        int leftMove;
        for(int i=1;i<=size;i++){
            leftMove = 8 * (size - i);
            res = res | ((data[pointer++] & 0xff) << leftMove);
        }
        return res;
    }
    /**
     * 根据指定位置将字节类型的数据转换为整型 -- 正序执行
     * @param data 要解析的数据
     * @param size 长度
     * @return 解析后的结果
     */
    protected int convertToIntByOffset(byte[] data,int size,int position){
        int res=0;
        int leftMove;
        for(int i=1;i<=size;i++){
            leftMove = 8 * (size - i);
            res = res | ((data[position++] & 0xff) << leftMove);
        }
        return res;
    }

    /**
     * 将字节类型的数据转换为整型 -- 逆序执行
     * @param data 要解析的数据
     * @param size 长度
     * @return 解析后的结果
     */
    protected int convertToInt(byte[] data,int position,int size){
//        pointer.set(pointer.get()+size);//更新字节数组中的指针位置。
        pointer += size;
        int res=0;
        int leftMove;
        while((size--)>0){
            leftMove=8*size;
            res=res | ((data[position--] & 0xff) << leftMove);
        }
        return res;
    }

    /**
     * 将字节类型的数据转换为整型
     * @param data 要解析的数据
     * @param size 长度
     * @return 解析后的结果
     */
    protected long convertToLong(byte[] data,int position,int size){
//        pointer += size; //更新字节数组中的指针位置。
        pointer += size;

        long res=0;
        int leftMove;
        while((size--)>0){
            leftMove=8*size;
            res=res | ((long) (data[position--] & 0xff) << leftMove);
        }
        return res;
    }

    /**
     * 将字节类型数据转化为字符串类型
     * @param data 要解析的数据
     * @param size 长度
     * @return 解析后的结果
     */
    protected String convertToString(byte[] data,int size,int position){
        byte[] t=new byte[size];
        for(int i=0;i<size;i++){
            t[i] = data[position++];
        }
        return new String(t);
    }

}
