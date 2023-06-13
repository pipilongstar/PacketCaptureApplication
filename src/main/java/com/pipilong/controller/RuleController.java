package com.pipilong.controller;

import com.pipilong.service.Impl.DFAParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pipilong
 * @createTime 2023/5/15
 * @description
 */
@RestController
@RequestMapping("/rule")
public class RuleController {

    @Autowired
    private DFAParser dfaParser;

    @PostMapping("/filter")
    public ResponseEntity<String> filter(@RequestBody String rule){
        if(dfaParser.parser(rule)) {

            new ResponseEntity<>("规则合法", HttpStatus.OK);
        }

        return new ResponseEntity<>("规则非法",HttpStatus.BAD_REQUEST);
    }


}
