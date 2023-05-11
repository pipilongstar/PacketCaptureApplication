package com.pipilong.controller;

import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public ModelAndView index() throws PcapNativeException {
        List<PcapNetworkInterface> devs = Pcaps.findAllDevs();
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("devs",devs);
        return modelAndView;
    }

}


























