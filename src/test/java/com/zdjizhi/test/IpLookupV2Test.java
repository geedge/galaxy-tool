package com.zdjizhi.test;

import com.zdjizhi.utils.IpLookupV2;
import org.apache.log4j.Logger;
import org.junit.Test;


public class IpLookupV2Test {
    Logger logger  = Logger.getLogger(IpLookupV2Test.class);


    @Test
    public void testIpLookup() {

       IpLookupV2 ipLookup =  new IpLookupV2.Builder(false)
               .loadDataFileV4("dat/ip_v4_built_in.mmdb")
               .loadDataFilePrivateV4("dat/ip_v4_user_defined_xj.mmdb")
               .loadDataFileV6("dat/ip_v6_built_in.mmdb")
               .loadDataFilePrivateV6("dat/ip_v6_user_defined.mmdb")
               .loadAsnDataFile("dat/asn_v4.mmdb")
               .loadAsnDataFileV6("dat/asn_v6.mmdb")
               .build();
        logger.info(ipLookup.administrativeAreaLookupDetail("1.1.1.1"));
        logger.info(ipLookup.locationLookupDetail("1.1.1.1"));
        logger.info(ipLookup.administrativeAreaLookupDetail("220.158.208.254"));
        logger.info(ipLookup.locationLookupDetail("220.158.208.254"));

        logger.info( ipLookup.provinceLookup("217.71.108.0"));
        logger.info(ipLookup.cityLookup("217.71.108.0"));
        logger.info(ipLookup.cityLookupDetail("217.71.108.0"));
        logger.info(ipLookup.cityLookup("120.221.155.223"));
        logger.info(ipLookup.countryLookup("2001:4860:4860::8888 "));
        logger.info(ipLookup.latLngLookup("217.71.108.0"));
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
        logger.info(ipLookup.ispLookup("116.178.223.14"));
        logger.info(ipLookup.organizationLookup("116.178.223.14"));


    }










}

