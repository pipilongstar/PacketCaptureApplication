package com.pipilong.service.Impl;

import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pipilong
 * @createTime 2023/5/7
 * @description
 */
@Service
public class NetworkInterfaceService {

    public List<PcapNetworkInterface> getNetworkInterface() throws PcapNativeException {

        return Pcaps.findAllDevs();

    }

}
