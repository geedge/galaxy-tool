package com.zdjizhi.test;

import com.zdjizhi.utils.IpLookup;
import org.apache.log4j.Logger;
import org.junit.Test;

public class IpLookupTestUtils {
    Logger logger  = Logger.getLogger(IpLookupTestUtils.class);
    @Test
    public void testIpCoutry() {
       IpLookup ipLookup =  new IpLookup.Builder(true).build();
        logger.info(ipLookup.cityLookupDetail("192.168.10.1"));

    }


}
