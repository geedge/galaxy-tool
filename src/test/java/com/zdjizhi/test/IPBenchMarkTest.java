package com.zdjizhi.test;

import com.zdjizhi.utils.AsnLookup;
import com.zdjizhi.utils.IpLookup;
import com.zdjizhi.utils.IpLookupV2;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

public class IPBenchMarkTest {
    private final static Logger logger  = Logger.getLogger(IpLookupTest.class);
    private  final static boolean TRACE = false;

    private final static int COUNT = 1000000;

    private final static int SEED = 5;


    private IpLookupV2 ipLookup =  getIpLookupByDatabase();

    private IpLookupV2 ipLookupA = getIpLookupByStream();


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
                    .loadAsnDataFile("dat/asn_v4.mmdb")
                    .loadAsnDataFileV6("dat/asn_v6.mmdb")
                    .build();
        }catch (FileNotFoundException e) {
            logger.error(" File is not found:", e);
        }
        return ipLookupV2;
    }

    private static AsnLookup asnLookup =  new AsnLookup.Builder(false)
            .loadDataFileV4("dat/asn_v4.mmdb")
            .loadDataFileV6("dat/asn_v6.mmdb")
            .build();

    public static void main(String[] args) throws IOException{
        for(int i=0; i< SEED; i++) {
            new IPBenchMarkTest().benchMark(COUNT, i);
        }
    }

    private void benchMark(int count, int seed) throws IOException {
        Random random = new Random(seed);
        long startTime = System.nanoTime();
        byte[] address = new byte[4];
        for (int i = 0; i < count; i++) {
            random.nextBytes(address);
            InetAddress ip = InetAddress.getByAddress(address);
            String ipInfo = ipLookup.infoLookupToCSV(ip.getHostAddress());
            if (TRACE) {
                if (i % 50000 == 0) {
                    logger.info(i + " " + ip);
                    logger.info(ipInfo);
                }
            }
        }
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long qps = count * 1000000000L / duration;
        logger.info("Requests per second: " + qps);
    }

}
