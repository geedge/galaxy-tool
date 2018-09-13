package com.zdjizhi.utils;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 *
 * <p>对IP定位查找工具类.</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2018-08-30
 *
 */
public class IpLookup {
    private static Logger logger = LoggerFactory.getLogger(IpLookup.class);
    public final static String DEFAULT_IP_DATABASE_PATH = "dat";
    private static final String ASN_PATTERN = "AS(.*?) ";
    private static LookupService countyService;
    private static LookupService cityService;
    private static LookupService asnService;
    private static LookupService countyServicev6;
    private static LookupService cityServicev6;
    private static LookupService asnServicev6;

    static {
        try {
            String datRootPath =  System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator;
            countyService = new LookupService(datRootPath + "GeoIP.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
            cityService = new LookupService(datRootPath + "GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
            asnService = new LookupService(datRootPath + "GeoIPASNum.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
            countyServicev6 =  new LookupService(datRootPath + "GeoIPv6.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
            cityServicev6 =  new LookupService(datRootPath + "GeoLiteCityv6.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
            asnServicev6 = new LookupService(datRootPath + "GeoIPASNumv6.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Unknow GeoIP dat file ,please check your dat path!");

        }

    }


    /**
     *
     * <p>给定IP库文件路径，根据ip获取国家代码.</p>
     * @param ip  ip地址
     * @return String 国家代码
     */
    public static String countryLookup(String ip) {

        String result = "";

        try {
            Country country = null;

            if (IPUtil.isIP(ip)) {
                country = countyService.getCountry(ip);
            } else if (IPUtil.isIPv6Address(ip)){
                country = countyServicev6.getCountry(ip);
            }

            if (StringUtil.isNotEmpty(country)) {

                result = country.getName().equals("null") ? null :  country.getName();
            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);

        } finally {
            return result;
        }


    }



    /**
     * <p>
     * 默认IP城市库文件路径，根据ip获取城市代码.
     * 默认路径为:dat/GeoLiteCity.dat
     * </p>
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 城市代码 省代码 国家代码
     */
    public static String cityLookupDetail(String ip) {

        StringBuffer result = new StringBuffer(64);

        try {

            Location location = null;

            if (IPUtil.isIP(ip)) {
                location = cityService.getLocation(ip);
            } else if (IPUtil.isIPv6Address(ip)) {
                location = cityServicev6.getLocation(ip);
            }

            if (StringUtil.isNotEmpty(location)) {

                if (StringUtil.isEmpty(location.city)) {
                    result.append(" ").append("\t");

                } else {
                    result.append(location.city).append("\t");
                }


                if (StringUtil.isEmpty(location.countryCode)) {
                    result.append(" ").append("\t");
                } else {
                    result.append(regionName.regionNameByCode(location.countryCode, location.region)).append("\t");
                }



                if (StringUtil.isEmpty(location.countryName)) {
                    result.append(" ").append("\t");
                } else {
                    result.append(location.countryName).append("\t");
                }

            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result.toString();
    }

    /**
     * <p>
     * 默认IP城市库文件路径，根据ip获取城市代码.
     * 默认路径为:dat/GeoLiteCity.dat
     * </p>
     * @param ip ip地址，点分十进制格式,ipv4
     * @return String 城市|省代码
     */
    public static String cityLookup(String ip) {

        String result = "";
        try {

            Location location = null;

            if (IPUtil.isIP(ip)) {
                location = cityService.getLocation(ip);
            } else if (IPUtil.isIPv6Address(ip)) {
                location = cityServicev6.getLocation(ip);
            }

            if (StringUtil.isNotEmpty(location)) {

                result = regionName.regionNameByCode(location.countryCode, location.region);
                if (StringUtil.isBlank(result)
                        || result.equals("null")) {
                    result = location.city;
                }

            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result;

    }


    /**
     * <p>
     * 默认IP城市库文件路径，根据ip获取经纬度.
     * </p>
     * @param ip ip地址
     * @return String 省代码 纬度 经度
     */
    public static String cityLatLngLookup(String ip) {
        StringBuilder  result = new StringBuilder(64);
        try {

            Location location = null;

            if (IPUtil.isIP(ip)) {
                location = cityService.getLocation(ip);
            } else if (IPUtil.isIPv6Address(ip)) {
                location = cityServicev6.getLocation(ip);
            }

            if (StringUtil.isNotEmpty(location)) {

                result.append(regionName.regionNameByCode(location.countryCode, location.region));
                if (StringUtil.isBlank(result.toString())
                        || result.equals("null")) {
                    result.append(location.city);
                }

                result.append("\t").append(location.latitude).append("\t") .append(location.longitude);

            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result.toString();

    }





    /**
     *
     * <p>给定IP组织库文件路径，根据ip获取AS号,带组织结构</p>
     * @param ip ip地址
     * @return String 组织名称
     */
    public static String asnLookupDeatil(String ip) {

        String result = null;
        try {

            if (IPUtil.isIP(ip)) {
                result  = asnService.getOrg(ip);
            } else if (IPUtil.isIPv6Address(ip)) {
                result  = asnServicev6.getOrg(ip);
            }

            if (StringUtil.isNotBlank(result)) {
                result = result.equals("null") ? null : result;
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result;

    }

    /**
     *
     * <p>给定IP组织库文件路径，根据ip获取AS号</p>
     * @param ip ip地址
     * @return String 组织名称
     */
    public static String asnLookup(String ip) {

        String result = null;
        try {

            if (IPUtil.isIP(ip)) {
                result  = asnService.getOrg(ip);
            } else if (IPUtil.isIPv6Address(ip)) {
                result  = asnServicev6.getOrg(ip);
            }

            if (StringUtil.isNotBlank(result)) {

                result = result.equals("null") ? "" : result;

                result = StringUtil.getFromRegEx(result, ASN_PATTERN, 1);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result;

    }


    public static void flushAll() {

        if (countyService != null) {
            countyService.close();
        }

        if (cityService != null) {
            cityService.close();
        }

        if (asnService != null) {
            asnService.close();
        }


        if (countyServicev6 != null) {
            countyServicev6.close();
        }


        if (cityServicev6 != null) {

            cityServicev6.close();
        }


        if (asnServicev6 != null) {
            asnServicev6.close();
        }


    }

}
