package com.zdjizhi.test;

import com.zdjizhi.utils.IpLookupV2;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class IpLookupV2Test {
    Logger logger  = Logger.getLogger(IpLookupV2Test.class);
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    IpLookupV2 ipLookupA =  getIpLookupByDatabase();

    IpLookupV2 ipLookup = getIpLookupByStream();


    private IpLookupV2 getIpLookupByDatabase() {
        return new IpLookupV2.Builder(false)
                .loadDataFileV4("dat/ip_v4_built_in.mmdb")
                .loadDataFilePrivateV6("dat/ip_v4_user_defined.mmdb")
                .loadDataFileV6("dat/ip_v6_built_in.mmdb")
                .loadDataFilePrivateV6("dat/ip_v6_user_defined.mmdb")
                .loadAsnDataFile("dat/asn_v4.mmdb")
                .loadAsnDataFileV6("dat/asn_v6.mmdb")
                .build();
    }

    private IpLookupV2 getIpLookupByStream() {
        IpLookupV2 ipLookupV2 = null;
        try {
            ipLookupV2 = new IpLookupV2.Builder(false)
                    .loadDataFileV4(new FileInputStream(new File("dat/ip_v4_built_in.mmdb")))
                    .loadDataFilePrivateV4(new FileInputStream(new File("dat/ip_v4_user_defined.mmdb")))
                    .loadDataFileV6(new FileInputStream(new File("dat/ip_v6_built_in.mmdb")))
                    .loadDataFilePrivateV6(new FileInputStream(new File("dat/ip_v6_user_defined.mmdb")))
                    .loadAsnDataFile(new FileInputStream(new File("dat/asn_v4.mmdb")))
                    .loadAsnDataFileV6(new FileInputStream(new File("dat/asn_v6.mmdb")))
                    .build();
        }catch (FileNotFoundException e) {
          logger.error(" File is not found:", e);
        }
        return ipLookupV2;
    }
    @Test
    public void testPrivateIP() {
        Assert.assertEquals(ipLookup.countryLookup("192.168.10.123"), "Private IP");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIpLookupIllegalArguments(){
        ipLookup.countryLookup("116.178.179.336");
    }

    @Test
    public void testIpLookupOutput() {
        logger.info(ipLookup.countryLookup("120.221.155.223"));
        logger.info(ipLookup.provinceLookup("120.221.155.223"));
        logger.info(ipLookup.cityLookup("120.221.155.223"));
        logger.info(ipLookup.cityLookupDetail("120.221.155.223"));
        logger.info(ipLookup.administrativeAreaLookupDetail("120.221.155.223"));
        logger.info(ipLookup.locationLookupDetail("120.221.155.223"));
        logger.info(ipLookup.latLngLookup("120.221.155.223"));
        logger.info(ipLookup.cityLatLngLookup("120.221.155.223"));
        logger.info(ipLookup.ispLookup("120.221.155.223"));
        logger.info(ipLookup.organizationLookup("120.221.155.223"));
        logger.info(ipLookup.asnLookup("120.221.155.223"));
        logger.info(ipLookup.infoLookupToCSV("120.221.155.223"));
        logger.info(ipLookup.infoLookupToJson("120.221.155.223"));
    }


 @Test
 public void testAsnLookupOutput() {

  logger.info(ipLookup.asnLookup("120.221.155.223"));
  logger.info(ipLookup.asnLookup("203.187.160.135"));
  logger.info(ipLookup.asnLookup("116.128.133.102"));
  logger.info(ipLookup.asnLookupInfo("116.128.133.102"));
  logger.info(ipLookup.asnLookupDetail("116.128.133.102"));
  logger.info(ipLookup.asnLookup("2001:4860:4860::8888"));
  logger.info(ipLookup.asnLookup("1.1.1.1"));

 }











}

