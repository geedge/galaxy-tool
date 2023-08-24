package com.geedgenetworks.test;

import com.alibaba.fastjson2.JSONObject;
import com.geedgenetworks.utils.AbstractIpLookup;
import com.geedgenetworks.utils.IpLookupV2;
import com.maxmind.db.Network;
import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;


public class IpLookupV2Test {
    Logger logger  = Logger.getLogger(IpLookupV2Test.class);
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    IpLookupV2 ipLookupA = getIpLookupByDatabase();
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
            ipLookupV2 = new IpLookupV2.Builder(true)
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
    public void testLoadPrivateIPv4MMDBFiles() {
        try {
           IpLookupV2 ipLookupV4 = new IpLookupV2.Builder(false)
                   .loadDataFilePrivateV4(new FileInputStream(new File("dat/ip_v4_user_defined.mmdb")))
                   .build();
           assertEquals(ipLookupV4.getDbReaders().get(AbstractIpLookup.IpVersion.IPv4.name()).size(), 1);
        }catch (FileNotFoundException e) {
            logger.error(" File is not found:", e);
        }

    }

    @Test
    public void testLoadAllPrivateIPv4MMDBFiles() {
        try {
            IpLookupV2 ipLookupV4 = new IpLookupV2.Builder(false)
                    .loadDataFilePrivateV4(new FileInputStream(new File("dat/ip_v4_user_defined.mmdb")))
                    .loadDataFileV4(new FileInputStream(new File("dat/ip_v4_built_in.mmdb")))
                    .build();
            assertEquals(ipLookupV4.getDbReaders().get(AbstractIpLookup.IpVersion.IPv4.name()).size(), 2);
        } catch (FileNotFoundException e) {
            logger.error(" File is not found:", e);
        }

    }

    @Test
    public void testLoadDefaultMMDBFiles() {

       IpLookupV2 tmpIpLookup = new IpLookupV2.Builder(true).build();
       logger.info(tmpIpLookup.cityLookup("120.221.155.223"));

    }

    @Test
    public void testPrivateIP() {
       assertEquals(ipLookupA.countryLookup("192.168.10.123"), "Private IP");
       Object jsonObject =  ipLookupA.infoLookupToJSON("120.221.155.223");
       logger.info(jsonObject);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIpLookupIllegalArguments(){
        ipLookup.countryLookup("116.178.179.336");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIPIsBlank() {
        ipLookup.countryLookup("");
    }
    @Test(expected=IllegalArgumentException.class)
    public void testIPIsNull() {
        ipLookup.countryLookup(null);
    }

    @Test
    public void testIpLookupOutput() {
        assertEquals(ipLookup.countryLookup("120.221.155.223"), "China");
        assertEquals(ipLookup.provinceLookup("120.221.155.223"), "Shandong");
        assertEquals(ipLookup.cityLookup("120.221.155.223"), "Other");
        assertEquals(ipLookup.cityLookupDetail("120.221.155.223").split("\\" + AbstractIpLookup.LOCATION_SEPARATOR).length, 3);
        assertEquals(ipLookup.locationLookupDetail("120.221.155.223").split("\\" + AbstractIpLookup.LOCATION_SEPARATOR).length, 7);
        assertEquals(ipLookup.administrativeAreaLookupDetail("120.221.155.223").split("\\" + AbstractIpLookup.LOCATION_SEPARATOR).length, 4);
        assertEquals(ipLookup.latLngLookup("120.221.155.223"), "36.0986,120.3719");
        assertEquals(ipLookup.cityLatLngLookup("120.221.155.223").split( AbstractIpLookup.OBJECT_SEPARATOR).length, 2);
        assertEquals(ipLookup.ispLookup("120.221.155.223"), "-");
        assertEquals(ipLookup.organizationLookup("120.221.155.223"), "");
        assertEquals(ipLookup.infoLookupToCSV("120.221.155.223").split( AbstractIpLookup.OBJECT_SEPARATOR).length, 5);
        assertEquals(JSONObject.from(ipLookup.infoLookupToJSON("120.221.155.223")).get("country"),"China");
    }

    @Test
    public void testOutputJson() {
        logger.info(ipLookup.infoLookupToJSON("120.221.155.223"));
        logger.info(ipLookup.infoLookupToJSON("2001:db8:3333:4444:5555:6666:7777:8888"));
        logger.info(ipLookup.infoLookupToJSON("2001:db8::"));
        logger.info(ipLookup.infoLookupToJSON("120.221.155.223"));

    }

    @Test
    public void testIPv6() throws UnknownHostException {
        Network network = new Network(
                InetAddress.getByName("2001:0db8:85a3:0000:0000:8a2e:0370:7334"),
                28
        );

        assertEquals("2001:db0:0:0:0:0:0:0", network.getNetworkAddress().getHostAddress());
        assertEquals(28, network.getPrefixLength());
        assertEquals("2001:db0:0:0:0:0:0:0/28", network.toString());
    }


    @Test
    public void testUnknownIP() {
        logger.info(ipLookup.infoLookupToJSON("255.244.255.254"));
        logger.info(ipLookup.infoLookupToJSON("8.8.8.8"));
    }


 @Test
 public void testAsnLookupOutput() {
  logger.info(ipLookup.asnLookupOrganization("8.8.8.8"));
  logger.info(ipLookup.asnLookupDetail("8.8.8.8"));
  logger.info(ipLookup.asnLookupOrganization("8.8.8.8"));



 }











}

