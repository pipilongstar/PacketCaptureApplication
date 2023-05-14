package com.pipilong.controller;

import com.pipilong.domain.Packet;
import com.pipilong.service.Impl.PcapParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author pipilong
 * @createTime 2023/5/12
 * @description
 */
@RestController
@RequestMapping("/uploadFile")
public class UploadController {

    @Autowired
    private PcapParser parser;

    @PostMapping
    public ResponseEntity<List<Packet>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException, CloneNotSupportedException {
        System.out.println(file.getOriginalFilename());
        List<Packet> list = parser.parser(file.getBytes());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}




































