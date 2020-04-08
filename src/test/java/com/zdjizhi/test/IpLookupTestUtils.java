package com.zdjizhi.test;

import com.zdjizhi.utils.IpLookup;
import org.apache.log4j.Logger;
import org.junit.Test;

public class IpLookupTestUtils {
    Logger logger  = Logger.getLogger(IpLookupTestUtils.class);
    @Test
    public void testIpCoutry() {

       IpLookup ipLookup =  new IpLookup.Builder(false).loadDataFileV4("dat/all_ip_info_v4.mmdb")
               .loadDataFileV6("dat/all_ip_info_ipv6.mmdb")
               .loadAsnDataFileV4("dat/asn_v4.mmdb")
               .loadAsnDataFileV6("dat/asn_v6.mmdb")
               .build();

        logger.info(ipLookup.cityLookup("120.221.155.223"));
        logger.info(ipLookup.countryLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("120.221.155.223"));
        logger.info(ipLookup.latLngLookup("120.221.155.223"));
        logger.info(ipLookup.cityLatLngLookup("120.221.155.223"));

    }


}
