package com.zdjizhi.test;

import com.zdjizhi.utils.IPUtil;
import com.zdjizhi.utils.IpLookup;
import com.zdjizhi.utils.IpLookupV2;
import com.zdjizhi.utils.StringUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by dell on 2018-8-30.
 */

public class IpTest {

    private static final String valueSeparator = "\t";
    private static Logger logger = LoggerFactory.getLogger(IpTest.class);
    private Map<String, String> parserIpFile(String filePath) {

        Map<String, String> lineMap = new LinkedHashMap<>();
        File logFile = new File(filePath);

        if (logFile.exists() && logFile.isFile()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(logFile));
                String tempString = null;
                int line = 0;
                // 一次读入一行，直到读入null为文件结束
                int count =0;
                while ((tempString = reader.readLine()) != null) {
                    // 显示行号
                    if (tempString.split(valueSeparator).length >= 4) {
                        count++;
                        String[] rules = tempString.split(valueSeparator);
                        logger.info(rules[0] + " " + rules[1]);
                        lineMap.put(String.valueOf(count),  rules[0].trim() + "\t" + rules[1].trim());
                    }

                    line++;

                }
                System.out.println("file total line is : " + line + "; correct line is :" + count);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }

        return lineMap;

    }


   // @Test
    public void testCountry() {

        Map<String, String> lineMap = parserIpFile("D:\\galaxy\\dat\\kaz.txt");
        logger.info("total ip count : " + lineMap.keySet().size());
        long sum = 0;

        for (String line : lineMap.keySet()) {

            String[] ips = lineMap.get(line).split("\t");

            logger.info("line:{}, ip numbers is {}" , ips[1] + "-" + ips[0], (IPUtil.getIpHostDesimal(ips[1])- IPUtil.getIpHostDesimal(ips[0]))+1);
            List<String> ipList = IPUtil.getIPRange(ips[0], ips[1]);
            sum += ipList.size();
            for(String ip : ipList) {
                String country = IpLookup.countryLookup(ip);

                if (!country.contains("Kazakhstan")) {
                    writeFile(ip + "\t" + country , "nokacountry.txt");
                }
            }
            logger.info("=======================================================");


        }

        logger.info("kaz ip sum :" + sum);


    }

    @Test
    public void testIpRange() {
        logger.info(Arrays.toString(IPUtil.getIPRange("192.168.10.1", 25).toArray()));
    }

   // @Test
    public void testCity() {

        Map<String, String> lineMap = parserIpFile("D:\\galaxy\\dat\\kaz.txt");
        logger.info("total ip count : " + lineMap.keySet().size());
        long sum = 0;
        long citysum = 0;
        long unknowCitySum = 0;
        long nocitysum = 0;

        for (String line : lineMap.keySet()) {

            String[] ips = lineMap.get(line).split("\t");



            logger.info("line:{}, ip numbers is {}" , ips[1] + "-" + ips[0], (IPUtil.getIpHostDesimal(ips[1])- IPUtil.getIpHostDesimal(ips[0]))+1);
            List<String> ipList = IPUtil.getIPRange(ips[0], ips[1]);
            sum += ipList.size();
            for(String ip : ipList) {
                String location = IpLookup.cityLookup(ip);

                if (location != null && location.contains("Kazakhstan")) {
                    String[] locations = location.split("\t");
                    if (locations[0].equals("null") && locations[1].equals("null")) {
                        writeFile(ip + "\t" + location , "unknowkazcity.txt");
                        unknowCitySum ++;
                    } else {
                        citysum ++;
                    }

                } else {
                    writeFile(ip + "\t" + location , "nokazcity.txt");
                    nocitysum ++;
                }
            }
            logger.info("=======================================================");




        }

        logger.info("kaz ip sum :{}, citysum:{}, unknowCitySum:{}, nocitysum:{}" ,sum ,citysum ,unknowCitySum ,nocitysum);


    }

   // @Test
    public void testAsn() {

        Map<String, String> lineMap = parserIpFile("D:\\galaxy\\dat\\kaz.txt");
        logger.info("total ip count : " + lineMap.keySet().size());
        long sum = 0;
        long asnSum = 0;
        long unknowAsnSum = 0;
        long noAsnsum = 0;

        for (String line : lineMap.keySet()) {

            String[] ips = lineMap.get(line).split("\t");



            logger.info("line:{}, ip numbers is {}" , ips[1] + "-" + ips[0], (IPUtil.getIpHostDesimal(ips[1])- IPUtil.getIpHostDesimal(ips[0]))+1);
            List<String> ipList = IPUtil.getIPRange(ips[0], ips[1]);
            sum += ipList.size();
            for(String ip : ipList) {
                String location = IpLookup.countryLookup(ip);

                if (location != null && location.contains("Kazakhstan")) {

                    String asnNum = IpLookup.asnLookup(ip);

                    if ( null == asnNum || asnNum.equals("null")) {
                        writeFile(ip + "\t" + asnNum , "unknowAsnnum.txt");
                        unknowAsnSum ++;
                    } else {
                        asnSum ++;
                        writeFile(ip + "\t" + asnNum , "asnnum.txt");
                    }

                } else {
                    writeFile(ip + "\t" + location , "noasnnum.txt");
                    noAsnsum ++;
                }
            }
            logger.info("=======================================================");




        }

        logger.info("kaz ip sum :{}, asnSum:{}, unknowAsnSum:{}, noAsnsum:{}" ,sum ,asnSum ,unknowAsnSum ,noAsnsum);


    }



    private static void writeFile (String result, String fileName) {
        File resultDir = new File("D:\\galaxy\\result");
        BufferedWriter writer = null;
        try{

            if (!resultDir.exists()) {
                resultDir.mkdirs();
            }
            writer = new BufferedWriter(new FileWriter(resultDir + File.separator + fileName, true));
            writer.write(result + "\r\n");

        } catch(Exception e){
            e.printStackTrace();
        } finally {
             if (writer != null) {
                 try {
                     writer.close();
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }

             }
        }

    }


    //@Test
    public void testCountryv2() {

        Map<String, String> lineMap = parserIpFile("D:\\galaxy\\dat\\kaz.txt");
        logger.info("total ip count : " + lineMap.keySet().size());
        long sum = 0;

        for (String line : lineMap.keySet()) {

            String[] ips = lineMap.get(line).split("\t");

            logger.info("line:{}, ip numbers is {}" , ips[1] + "-" + ips[0], (IPUtil.getIpHostDesimal(ips[1])- IPUtil.getIpHostDesimal(ips[0]))+1);
            List<String> ipList = IPUtil.getIPRange(ips[0], ips[1]);
            sum += ipList.size();
            for(String ip : ipList) {
                String country = IpLookupV2.countryLookup(ip);

                if (country == null || !country.contains("Kazakhstan")) {
                    writeFile(ip + "\t" + country , "nokacountryv2.txt");
                }
            }
            logger.info("=======================================================");


        }

        logger.info("kaz ip sum :" + sum);


    }
    //@Test
    public void testCityV2() {

        Map<String, String> lineMap = parserIpFile("D:\\galaxy\\dat\\kaz.txt");
        logger.info("total ip count : " + lineMap.keySet().size());
        long sum = 0;
        long citysum = 0;
        long unknowCitySum = 0;
        long nocitysum = 0;

        for (String line : lineMap.keySet()) {

            String[] ips = lineMap.get(line).split("\t");



            logger.info("line:{}, ip numbers is {}" , ips[1] + "-" + ips[0], (IPUtil.getIpHostDesimal(ips[1])- IPUtil.getIpHostDesimal(ips[0]))+1);
            List<String> ipList = IPUtil.getIPRange(ips[0], ips[1]);
            sum += ipList.size();
            for(String ip : ipList) {
                String location = IpLookupV2.cityLookup(ip);

                if (location != null && location.contains("Kazakhstan")) {
                    String[] locations = location.split("\t");
                    if (StringUtil.isBlank(locations[0])
                            && StringUtil.isBlank(locations[1])) {
                        writeFile(ip + "\t" + location , "unknowkazcityV2.txt");
                        unknowCitySum ++;
                    } else {
                        writeFile(ip + "\t" + location , "kazcityV2.txt");
                        citysum ++;
                    }

                } else {
                    writeFile(ip + "\t" + location , "nokazcityV2.txt");
                    nocitysum ++;
                }
            }
            logger.info("=======================================================");




        }

        logger.info("kaz ip sum :{}, citysum:{}, unknowCitySum:{}, nocitysum:{}" ,sum ,citysum ,unknowCitySum ,nocitysum);


    }

    @Test
    public void ipTest() {
        logger.info(IPUtil.internalIp("173.168.6.123") + "");
        logger.info(IpLookup.cityLookup("10.0.3.123"));

        logger.info(IPUtil.convertMask(25));

        logger.info(IPUtil.getEndIP("192.168.100.3","255.255.255.0"));
    }



}
