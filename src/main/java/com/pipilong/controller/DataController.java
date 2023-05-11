package com.pipilong.controller;

import com.pipilong.service.Impl.RawPacketCapturer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @DeleteMapping("/clearDataArray")
    public void dataDelete(){
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

}




















