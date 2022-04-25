package com.zdjizhi.test;

import com.zdjizhi.utils.IpLookup;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.*;


public class IpLookupTest {
    Logger logger  = Logger.getLogger(IpLookupTest.class);


    @Test
    public void testIpLookup() {

       IpLookup ipLookup =  new IpLookup.Builder(false)
               .loadDataFileV4("dat/ip_v4.mmdb")
               .loadDataFilePrivateV4("dat/ip_v4_user_defined_xj.mmdb")
               .loadDataFileV6("dat/ip_v6.mmdb")
               .loadDataFilePrivateV6("dat/ip_v6.mmdb")
               .loadAsnDataFile("dat/asn_v4.mmdb")
               .loadAsnDataFileV6("dat/asn_v6.mmdb")
               .build();

        logger.info(ipLookup.cityLookupDetail("239.255.255.250"));
        logger.info(ipLookup.provinceLookup("192.168.50.21"));
        logger.info(ipLookup.cityLookup("192.168.50.21"));
        logger.info(ipLookup.cityLookupDetail("192.168.50.21"));
        logger.info(ipLookup.cityLookup("120.221.155.223"));
        logger.info(ipLookup.countryLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.latLngLookup("120.221.155.223"));
        logger.info(ipLookup.cityLookupDetail("120.221.155.223"));
        logger.info(ipLookup.cityLatLngLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("203.187.160.135"));
        logger.info(ipLookup.asnLookup("116.128.133.102"));
        logger.info(ipLookup.asnLookupInfo("116.128.133.102"));
        logger.info(ipLookup.asnLookupDetail("116.128.133.102"));
        logger.info(ipLookup.asnLookup("2001:4860:4860::8888"));
        logger.info(ipLookup.cityLookupDetail("202.106.0.20"));
        logger.info(ipLookup.ispLookup("123.139.226.221"));
        logger.info(ipLookup.organizationLookup("123.139.226.221"));
        logger.info(ipLookup.ispLookup("116.178.179.36"));
        logger.info(ipLookup.organizationLookup("116.178.179.36"));


    }


    @Test
    public void testCountryLookup() throws IOException {

        IpLookup ipLookup =  new IpLookup.Builder(false)
                .loadDataFileV4("dat/all_ip_coun_city.mmdb")
                .loadDataFileV6("dat/ip_v6.mmdb")
                .loadAsnDataFileV4("dat/asn_v4.mmdb")
                .loadAsnDataFileV6("dat/asn_v6.mmdb")
                .build();

        logger.info(ipLookup.cityLookup("120.221.155.123"));
        logger.info(ipLookup.countryLookup("120.221.155.223"));
        logger.info(ipLookup.countryLookup("120.221.155.223"));
        logger.info(ipLookup.getCountryCode("120.221.155.223"));
        logger.info(ipLookup.getRegionCode("120.221.155.223"));







    }







}

