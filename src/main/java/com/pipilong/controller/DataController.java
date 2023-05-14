package com.pipilong.controller;

import com.alibaba.fastjson.JSON;
import com.pipilong.domain.PacketData;
import com.pipilong.service.Impl.DataWriter;
import com.pipilong.service.Impl.RawPacketCapturer;
import com.pipilong.service.Impl.RawPacketParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author pipilong
 * @createTime 2023/5/10
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/dataProcessing")
public class DataController {

    @Autowired
    private RawPacketCapturer rawPacketCapturer;

    @Autowired
    private DataWriter dataWriter;

    @DeleteMapping("/clearDataArray")
    public void dataDelete() throws IOException {
        rawPacketCapturer.clearDataArray();
    }

    @GetMapping("/fileDownload")
    public ResponseEntity<byte[]> getPcapFile(){
        byte[] data = rawPacketCapturer.getDataArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "packet.pcap");
        return new ResponseEntity<>(data,headers, HttpStatus.OK);
    }

    @GetMapping("/dataDetail/{id}")
    public ResponseEntity<PacketData> getDataDetail(@PathVariable("id") int id) throws IOException {
        PacketData detailOfPacket = dataWriter.getDetailOfPacket(id);
        return new ResponseEntity<>(detailOfPacket,HttpStatus.OK);
    }

}




















