package com.geedgenetworks.test;

import com.alibaba.fastjson2.JSONObject;
import com.geedgenetworks.model.LocationResponse;
import com.geedgenetworks.utils.AbstractDatabaseReader;
import com.geedgenetworks.utils.AsnLookup;
import com.geedgenetworks.utils.IpLookupV2;
import com.maxmind.db.Network;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class IpLookupV2Test {
    Logger logger  = Logger.getLogger(IpLookupV2Test.class);
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    IpLookupV2 ipLookupByDatabase;
    IpLookupV2 ipLookupByStream;
    AsnLookup asnLookup;

    @Before
    public void begin() {
        ipLookupByDatabase = getIpLookupByDatabase();
        ipLookupByStream =  getIpLookupByStream();
        asnLookup = getAsnLookup();
    }

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

    private AsnLookup getAsnLookup() {
        AsnLookup asnLookup = null;
        try {
            asnLookup = new AsnLookup.Builder(false)
                    .loadDataFileV4(new FileInputStream(new File("dat/asn_v4.mmdb")))
                    .loadDataFileV6(new FileInputStream(new File("dat/asn_v6.mmdb")))
                    .build();
        }catch (FileNotFoundException e) {
            logger.error(" File is not found:", e);
        }
        return asnLookup;
    }

    @Test
    public void testLoadPrivateIPv4MMDBFiles() {
        try {
           IpLookupV2 ipLookup = new IpLookupV2.Builder(false)
                   .loadDataFilePrivateV4(new FileInputStream(new File("dat/ip_v4_user_defined.mmdb")))
                   .build();
           assertEquals(ipLookup.getDbReaders().get(AbstractDatabaseReader.IpVersion.IPv4.name()).size(), 1);
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
            assertEquals(ipLookupV4.getDbReaders().get(AbstractDatabaseReader.IpVersion.IPv4.name()).size(), 2);
        } catch (FileNotFoundException e) {
            logger.error(" File is not found:", e);
        }

    }

    @Test
    public void testLoadDefaultMMDBFiles() {
       IpLookupV2 ipLookup = new IpLookupV2.Builder(true).build();
        assertEquals(ipLookup.getDbReaders().values().size(), 2);
    }

    @Test
    public void testPrivateIP() {
       assertEquals(ipLookupByDatabase.countryLookup("192.168.10.123"), "Private IP");
       Object jsonObject =  ipLookupByDatabase.infoLookupToJSON("192.168.10.123");
       assertEquals(JSONObject.from(jsonObject).get("privateIP"), true);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIpLookupIllegalArguments(){
        ipLookupByStream.countryLookup("116.178.179.336");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testIPIsBlank() {
        ipLookupByStream.countryLookup("");
    }
    @Test(expected=IllegalArgumentException.class)
    public void testIPIsNull() {
        ipLookupByStream.countryLookup(null);
    }

    @Test
    public void testIpLookupByStream() {
        assertEquals(ipLookupByStream.countryLookup("120.221.155.223"), "China");
        assertEquals(ipLookupByStream.provinceLookup("120.221.155.223"), "Shandong");
        assertEquals(ipLookupByStream.cityLookup("120.221.155.223"), "Other");
        assertEquals(ipLookupByStream.cityLookupDetail("120.221.155.223").split("\\" + AbstractDatabaseReader.LOCATION_SEPARATOR).length, 3);
        assertEquals(ipLookupByStream.locationLookupDetail("120.221.155.223").split("\\" + AbstractDatabaseReader.LOCATION_SEPARATOR).length, 7);
        assertEquals(ipLookupByStream.administrativeAreaLookupDetail("120.221.155.223").split("\\" + AbstractDatabaseReader.LOCATION_SEPARATOR).length, 4);
        assertEquals(ipLookupByStream.latLngLookup("120.221.155.223"), "36.0986,120.3719");
        assertEquals(ipLookupByStream.cityLatLngLookup("120.221.155.223").split( AbstractDatabaseReader.OBJECT_SEPARATOR).length, 2);
        assertEquals(ipLookupByStream.ispLookup("120.221.155.223"), "-");
        assertEquals(ipLookupByStream.organizationLookup("120.221.155.223"), "");
        assertEquals(ipLookupByStream.infoLookupToCSV("120.221.155.223").split( AbstractDatabaseReader.OBJECT_SEPARATOR).length, 5);
        assertEquals(JSONObject.from(ipLookupByStream.infoLookupToJSON("120.221.155.223")).get("country"),"China");
    }

    @Test
    public void testIpLookupByDatabase() {
        assertEquals(ipLookupByDatabase.countryLookup("120.221.155.223"), "China");
        assertEquals(ipLookupByDatabase.provinceLookup("120.221.155.223"), "Shandong");
        assertEquals(ipLookupByDatabase.cityLookup("120.221.155.223"), "Other");
        assertEquals(ipLookupByDatabase.cityLookupDetail("120.221.155.223").split("\\" + AbstractDatabaseReader.LOCATION_SEPARATOR).length, 3);
        assertEquals(ipLookupByDatabase.locationLookupDetail("120.221.155.223").split("\\" + AbstractDatabaseReader.LOCATION_SEPARATOR).length, 7);
        assertEquals(ipLookupByDatabase.administrativeAreaLookupDetail("120.221.155.223").split("\\" + AbstractDatabaseReader.LOCATION_SEPARATOR).length, 4);
        assertEquals(ipLookupByDatabase.latLngLookup("120.221.155.223"), "36.0986,120.3719");
        assertEquals(ipLookupByDatabase.cityLatLngLookup("120.221.155.223").split( AbstractDatabaseReader.OBJECT_SEPARATOR).length, 2);
        assertEquals(ipLookupByDatabase.ispLookup("120.221.155.223"), "-");
        assertEquals(ipLookupByDatabase.organizationLookup("120.221.155.223"), "");
        assertEquals(ipLookupByDatabase.infoLookupToCSV("120.221.155.223").split( AbstractDatabaseReader.OBJECT_SEPARATOR).length, 5);
        assertEquals(JSONObject.from(ipLookupByDatabase.infoLookupToJSON("120.221.155.223")).get("country"),"China");
    }

    @Test
    public void testCountryLookup() {
        assertEquals(ipLookupByDatabase.countryLookup("120.221.155.223"), "China");
        assertEquals(ipLookupByDatabase.countryLookup("192.168.12.1"), "Private IP");
        assertEquals(ipLookupByDatabase.countryLookup("2001:db8:3333:4444:5555:6666:7777:8888"), "");
        System.out.println(ipLookupByDatabase.infoLookupToCSV("120.221.155.223"));
    }

    @Test
    public void testProvinceLookup() {
        assertEquals(ipLookupByDatabase.provinceLookup("120.221.155.223"), "Shandong");
        assertEquals(ipLookupByDatabase.provinceLookup("192.168.12.1"), "Private IP");
        assertEquals(ipLookupByDatabase.provinceLookup("2001:db8:3333:4444:5555:6666:7777:8888"), "");
    }

    @Test
    public void testCityLookup() {
        assertEquals(ipLookupByDatabase.cityLookup("120.221.155.223"), "Other");
        assertEquals(ipLookupByDatabase.cityLookup("192.168.12.1"), "Private IP");
        assertEquals(ipLookupByDatabase.cityLookup("2001:db8:3333:4444:5555:6666:7777:8888"), "");
    }

    @Test
    public void testCityLookupDetail() {
        assertEquals(ipLookupByStream.cityLookupDetail("120.221.155.223").split("\\" + AbstractDatabaseReader.LOCATION_SEPARATOR).length, 3);
        assertEquals(ipLookupByDatabase.cityLookupDetail("192.168.12.1"), "Private IP");
        assertEquals(ipLookupByDatabase.cityLookupDetail("2001:db8:3333:4444:5555:6666:7777:8888"), "..");
    }

    @Test
    public void testCountryCodeLookup() {
        assertEquals(ipLookupByDatabase.countryCodeLookup("120.221.155.223"), "");
        assertEquals(ipLookupByDatabase.countryCodeLookup("192.168.12.1"), "");
        assertEquals(ipLookupByDatabase.countryCodeLookup("2001:db8:3333:4444:5555:6666:7777:8888"), "");
    }
    @Test
    public void testRegionCodeLookup() {
        assertEquals(ipLookupByDatabase.regionCodeLookup("120.221.155.223"), "-");
        assertEquals(ipLookupByDatabase.regionCodeLookup("192.168.12.1"), "");
        assertEquals(ipLookupByDatabase.regionCodeLookup("2001:db8:3333:4444:5555:6666:7777:8888"), "");
    }

    @Test
    public void testInfoLookupJsonString() {
        assertTrue(JSONObject.parseObject(ipLookupByDatabase.infoLookupToJSONString("120.221.155.223"), LocationResponse.class) instanceof  LocationResponse);
        assertEquals(ipLookupByDatabase.infoLookupToJSONString("2001:db8:3333:4444:5555:6666:7777:8888"), "");
    }




    @Test
    public void testOutputJson() {
        logger.info(ipLookupByStream.infoLookupToJSON("120.221.155.223"));
        logger.info(ipLookupByStream.infoLookupToJSON("2001:db8:3333:4444:5555:6666:7777:8888"));
        logger.info(ipLookupByStream.infoLookupToJSON("2001:db8::"));
        logger.info(ipLookupByStream.infoLookupToJSON("120.221.155.223"));

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
        logger.info(ipLookupByStream.infoLookupToJSON("255.244.255.254"));
        logger.info(ipLookupByStream.infoLookupToJSON("8.8.8.8"));
    }


 @Test
 public void testAsnLookupOutput() {
  logger.info(ipLookupByStream.asnLookupOrganization("8.8.8.8"));
  logger.info(ipLookupByStream.asnLookupDetail("8.8.8.8"));
  logger.info(ipLookupByStream.asnLookupInfo("8.8.8.8"));
  logger.info(ipLookupByStream.asnLookupOrganization("8.8.8.8"));

  logger.info(asnLookup.asnLookupOrganization("8.8.8.8"));
  logger.info(asnLookup.asnLookupDetail("8.8.8.8"));
  logger.info(asnLookup.asnLookupInfo("8.8.8.8"));
  logger.info(asnLookup.asnLookupOrganization("8.8.8.8"));

 }











}

