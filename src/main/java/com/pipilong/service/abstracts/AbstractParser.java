package com.pipilong.service.abstracts;

import com.pipilong.domain.ParserResult;
import com.pipilong.enums.ProtocolType;
import com.pipilong.service.Parser;
import org.springframework.stereotype.Service;

/**
 * @author pipilong
 * @createTime 2023/5/4
 * @description
 */
@Service
public class AbstractParser implements Parser {
    protected Integer pointer=0;
    protected StringBuilder builder = new StringBuilder();
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
     * 将字节类型的数据转换为整型 -- 逆序执行
     * @param data 要解析的数据
     * @param size 长度
     * @return 解析后的结果
     */
    protected int convertToInt(byte[] data,int position,int size){
        pointer += size; //更新字节数组中的指针位置。
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
        pointer += size; //更新字节数组中的指针位置。
        long res=0;
        int leftMove;
        while((size--)>0){
            leftMove=8*size;
            res=res | ((long) (data[position--] & 0xff) << leftMove);
        }
        return res;
    }
}
