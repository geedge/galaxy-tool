package com.zdjizhi.test;

import com.zdjizhi.utils.IpLookup;
import org.apache.log4j.Logger;
import org.junit.Test;

public class IpLookupTest {
    Logger logger  = Logger.getLogger(IpLookupTest.class);
    @Test
    public void testIpCoutry() {

       IpLookup ipLookup =  new IpLookup.Builder(false)
               .loadDataFileV4("dat/ip_v4.mmdb")
               .loadDataFilePrivateV4("dat/ip_private_v4.mmdb")
               .loadDataFilePrivateV6("dat/ip_v6.mmdb")
               .loadAsnDataFile("dat/asn_v4.mmdb")
               .loadAsnDataFileV6("dat/asn_v6.mmdb")
               .build();

        logger.info(ipLookup.countryLookup("192.168.50.21"));
        logger.info(ipLookup.provinceLookup("192.168.50.21"));
        logger.info(ipLookup.cityLookup("192.168.50.21"));
        logger.info(ipLookup.cityLookupDetail("192.168.50.21"));
        logger.info(ipLookup.cityLookup("120.221.155.223"));
        logger.info(ipLookup.countryLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.latLngLookup("119.253.39.18"));
        logger.info(ipLookup.cityLatLngLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("203.187.160.135"));
        logger.info(ipLookup.asnLookup("116.128.133.102"));
        logger.info(ipLookup.asnLookupInfo("116.128.133.102"));

        logger.info(ipLookup.asnLookup("2001:4860:4860::8888"));


    }


}
