package com.pipilong.domain;
import com.pipilong.enums.ProtocolType;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.io.Serializable;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@Component
@Data
public class Packet implements Serializable {

    int id;
    String time;
    String source;
    String destination;
    ProtocolType protocol;
    int length;
    String info;

}
