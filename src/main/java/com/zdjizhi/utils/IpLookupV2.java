package com.zdjizhi.utils;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip.regionName;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;

/**
 *
 * <p>对IP定位查找工具类.</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2018-08-30
 *
 */
public class IpLookupV2 {

    private static Logger logger = LoggerFactory.getLogger(IpLookup.class);
    public final static String DEFAULT_IP_DATABASE_PATH = "dat";

    private static DatabaseReader countryReader = null;
    private static DatabaseReader cityReader = null;
    private static DatabaseReader asnReader = null;
    static {
        try {
            String datRootPath =  System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator;
            countryReader = new DatabaseReader.Builder(new File(datRootPath + "GeoLite2-Country.mmdb"))
                    .withCache(new CHMCache()).build();

            cityReader = new DatabaseReader.Builder(new File(datRootPath + "GeoLite2-City.mmdb"))
                    .withCache(new CHMCache()).build();


            asnReader = new DatabaseReader.Builder(new File(datRootPath + "GeoLite2-ASN.mmdb"))
                    .withCache(new CHMCache()).build();

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Unknow GeoIP dat file ,please check your dat path!");

        }

    }

    /**
     *
     * <p>给定IP库文件路径，根据ip获取国家代码.</p>
     * @param ip  ip地址
     * @return 国家代码
     */
    public static String countryLookup(String ip) {

        String result = null;

        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response =  countryReader.country(ipAddress);
            Country country = response.getCountry();
            result = StringUtil.setDefaultIfEmpty(country.getName(), "").toString() ;
        } catch (AddressNotFoundException ae) {
            logger.warn(ae.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result;

    }


    /**
     * <p>
     * 默认IP城市库文件路径，根据ip获取城市代码.
     * 默认路径为:dat/GeoLiteCity.dat
     * </p>
     * @param ip ip地址
     * @return 城市代码 省代码 国家代码
     */
    public static String cityLookupDetail(String ip) {

        String result = null;

        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response =  cityReader.city(ipAddress);
            Country country = response.getCountry();
            Subdivision subdivision = response.getMostSpecificSubdivision();
            City city = response.getCity();
            result = StringUtil.setDefaultIfEmpty(city.getName(), "") + "\t" + StringUtil.setDefaultIfEmpty(subdivision.getName(), "")
                        + "\t " + StringUtil.setDefaultIfEmpty(country.getName(), "");

        } catch (AddressNotFoundException ae) {
            logger.warn(ae.getMessage());
        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result ;
    }

    /**
     * <p>
     * 默认IP城市库文件路径，根据ip获取城市代码.
     * </p>
     * @param ip ip地址
     * @return 省代码
     */
    public static String cityLookup(String ip) {
        String result = null;
        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response =  cityReader.city(ipAddress);
            City city = response.getCity();
            result = StringUtil.setDefaultIfEmpty(city.getName(), "").toString();
            if (StringUtil.isBlank(result)) {
                Subdivision subdivision = response.getMostSpecificSubdivision();
                result = StringUtil.setDefaultIfEmpty(subdivision.getName(), "").toString();
            }

        } catch (AddressNotFoundException ae) {
            logger.warn(ae.getMessage());
        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result ;
    }

    /**
     * <p>
     * 默认IP城市库文件路径，根据ip获取经纬度.
     * </p>
     * @param ip ip地址
     * @return 省代码 纬度 经度
     */
    public static String cityLatLngLookup(String ip) {
        StringBuilder  result = new StringBuilder(64);
        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response =  cityReader.city(ipAddress);
            City city = response.getCity();
            Location location = response.getLocation();
            result.append(StringUtil.setDefaultIfEmpty(city.getName(), ""));
            if (StringUtil.isBlank(result.toString())) {
                Subdivision subdivision = response.getMostSpecificSubdivision();
                result.append(StringUtil.setDefaultIfEmpty(subdivision.getName(), ""));
            }
            result.append("\t").append(location.getLatitude()).append("\t").append(location.getLongitude());

        } catch (AddressNotFoundException ae) {
            logger.warn(ae.getMessage());
        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result.toString() ;
    }



    /**
     *
     * <p>给定IP组织库文件路径，根据ip获取AS号详细，带组织.</p>
     * @param ip ip地址
     * @return 组织名称
     */
    public static String asnLookupDetail(String ip) {

        String result = null;
        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            AsnResponse response = asnReader.asn(ipAddress);
            result = StringUtil.setDefaultIfEmpty(response.getAutonomousSystemNumber(), "") + "\t"
                        + StringUtil.setDefaultIfEmpty(response.getAutonomousSystemOrganization(), "");
        } catch (AddressNotFoundException ae) {
            logger.warn(ae.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result;

    }


    /**
     *
     * <p>给定IP组织库文件路径，根据ip获取AS号.</p>
     * @param ip ip地址
     * @return 组织名称
     */
    public static String asnLookup(String ip) {

        String result = null;
        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            AsnResponse response = asnReader.asn(ipAddress);
            result = StringUtil.setDefaultIfEmpty(response.getAutonomousSystemNumber(), "").toString();
        } catch (AddressNotFoundException ae) {
            logger.warn(ae.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result;

    }

    public static void main(String[] args){
        logger.info(countryLookup("202.106.10.20"));
        logger.info(cityLatLngLookup("222.175.94.0"));
        logger.info(asnLookup("202.106.10.20"));
    }


}
