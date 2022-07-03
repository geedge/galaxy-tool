package com.zdjizhi.test;

import com.zdjizhi.utils.AsnLookup;
import com.zdjizhi.utils.IpLookup;
import com.zdjizhi.utils.IpLookupV2;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;

public class IPBenchMarkTest {
    private final static Logger logger  = Logger.getLogger(IpLookupTest.class);
    private  final static boolean TRACE = false;

    private final static int COUNT = 1000000;

    private final static int SEED = 5;


    private static IpLookupV2 ipLookupV2 =  new IpLookupV2.Builder(false)
            .loadDataFileV4("dat/ip_v4_built_in.mmdb")
            .loadDataFilePrivateV6("dat/ip_v4_user_defined.mmdb")
            .loadDataFileV6("dat/ip_v6_built_in.mmdb")
            .loadDataFilePrivateV6("dat/ip_v6_user_defined.mmdb")
            .loadAsnDataFile("dat/asn_v4.mmdb")
            .loadAsnDataFileV6("dat/asn_v6.mmdb")
            .build();

    private static IpLookup ipLookup =  new IpLookup.Builder(false)
            .loadDataFileV4("dat/ip_v4_built_in.mmdb")
            .loadDataFilePrivateV6("dat/ip_v4_user_defined.mmdb")
            .loadDataFileV6("dat/ip_v6_built_in.mmdb")
            .loadDataFilePrivateV6("dat/ip_v6_user_defined.mmdb")
            .loadAsnDataFile("dat/asn_v4.mmdb")
            .loadAsnDataFileV6("dat/asn_v6.mmdb")
            .build();

    private static AsnLookup asnLookup =  new AsnLookup.Builder(false)
            .loadDataFileV4("dat/asn_v4.mmdb")
            .loadDataFileV6("dat/asn_v6.mmdb")
            .build();

    public static void main(String[] args) throws IOException{
        for(int i=0; i< SEED; i++) {
            benchMark(COUNT, i);
        }
    }

    private static void benchMark(int count, int seed) throws IOException {
        Random random = new Random(seed);
        long startTime = System.nanoTime();
        byte[] address = new byte[4];
        for (int i = 0; i < count; i++) {
            random.nextBytes(address);
            InetAddress ip = InetAddress.getByAddress(address);
            String ipInfo = ipLookupV2.infoLookupToJson(ip.getHostAddress());
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
