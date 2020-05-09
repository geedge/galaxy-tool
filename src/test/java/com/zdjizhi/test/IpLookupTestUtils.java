package com.zdjizhi.test;

import com.zdjizhi.utils.IpLookup;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.math.BigInteger;
import java.net.InetAddress;

public class IpLookupTestUtils {
    Logger logger  = Logger.getLogger(IpLookupTestUtils.class);
    @Test
    public void testIpCoutry() {

       IpLookup ipLookup =  new IpLookup.Builder(false)
               .loadDataFile("dat/ip.mmdb")
               .loadDataFilePrivateV4("dat/ip_private_v4.mmdb")
               .loadDataFilePrivateV6("dat/ip_v6.mmdb")
               .loadAsnDataFile("dat/asn_v4.mmdb")
               .loadAsnDataFileV6("dat/asn_v6.mmdb")
               .build();

        logger.info(ipLookup.countryLookup("192.168.50.231"));
        logger.info(ipLookup.cityLookupDetail("120.221.155.223"));
        logger.info(ipLookup.countryLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.asnLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.latLngLookup("119.253.39.18"));
        logger.info(ipLookup.cityLatLngLookup("120.221.155.223"));


    }


}
