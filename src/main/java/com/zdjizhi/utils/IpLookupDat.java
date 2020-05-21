package com.zdjizhi.utils;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;

import java.io.File;

/**
 *
 * <p>maxmind 官网API结构，对IP定位查找工具类，加载dat数据格式文件</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2018-08-30
 *
 */
@Deprecated
public class IpLookupDat extends AbstractIpLookup {
    public final static String DEFAULT_IP_DATABASE_PATH = "dat";
    private static final String ASN_PATTERN = "AS(.*?) ";
    private static LookupService countyService;
    private static LookupService cityService;
    private static LookupService asnService;
    private static LookupService countyServicev6;
    private static LookupService cityServicev6;
    private static LookupService asnServicev6;

      synchronized void init(Builder builder) {

        try {

            if (builder.isDefalultDB) {
                String datRootPath =  System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator;
                countyService = new LookupService(datRootPath + "GeoIP.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                cityService = new LookupService(datRootPath + "GeoLiteCity.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                asnService = new LookupService(datRootPath + "GeoIPASNum.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                countyServicev6 =  new LookupService(datRootPath + "GeoIPv6.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                cityServicev6 =  new LookupService(datRootPath + "GeoLiteCityv6.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                asnServicev6 = new LookupService(datRootPath + "GeoIPASNumv6.dat", LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);

            } else {
                if (builder.geoIPFile !=  null) {
                    countyService = new LookupService(builder.geoIPFile, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                }

                if (builder.geoLiteCityFile !=  null) {
                    cityService = new LookupService(builder.geoLiteCityFile, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                }


                if (builder.geoIPASNumFile !=  null) {
                    asnService = new LookupService(builder.geoIPASNumFile, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                }


                if (builder.geoIPv6File !=  null) {
                    countyServicev6 =  new LookupService(builder.geoIPv6File, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                }

                if (builder.geoLiteCityv6File !=  null) {
                    cityServicev6 =  new LookupService(builder.geoLiteCityv6File, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                }


                if (builder.geoIPASNumv6File !=  null) {
                    asnServicev6 =  new LookupService(builder.geoIPASNumv6File, LookupService.GEOIP_MEMORY_CACHE | LookupService.GEOIP_CHECK_CACHE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Unknow GeoIP dat file ,please check your dat path!");

        }
    }



    private IpLookupDat(Builder builder) {
        init(builder);
    }

    /**
     * 加载dat数据字典文件，使用方式如下：
     * IpLookupDat ipLookupV = new IpLookupDat.Builder(false).loadGeoIPFile("D:\\galaxy-tool\\dat\\GeoIPv6.dat")
     *.loadGeoIPASNumFile("D:\\galaxy-tool\\dat\\GeoIPASNum.dat").build();
     */
    public static final class Builder {
        boolean isDefalultDB;
        String geoIPFile;
        String geoLiteCityFile;
        String geoIPASNumFile;
        String geoIPv6File;
        String geoLiteCityv6File;
        String geoIPASNumv6File;

        /**
         * 指定是否采用默认目录
         * @param isDefalultDB true： 默认路径，应用所在dat目录下 false：指定加载dat文件绝对路径
         *
         */
        public Builder(boolean isDefalultDB) {
            this.isDefalultDB = isDefalultDB;
        }

        /**
         * 加载GeoIP.dat文件
         * @param geoIPFile 包含国家定位信息,ipv4
         * @return
         */
        public Builder loadGeoIPFile(String geoIPFile) {
            this.geoIPFile = geoIPFile;
            return this;
        }

        /**
         * 加载GeoLiteCity.dat文件
         * @param geoLiteCityFile 包含城市定位信息,ipv4
         * @return
         */
        public Builder loadGeoLiteCityFile(String geoLiteCityFile) {
            this.geoLiteCityFile = geoLiteCityFile;
            return this;
        }

        /**
         * 加载GeoIPASNum.dat文件
         * @param geoIPASNumFile 包含as号信息,ipv4
         * @return
         */
        public Builder loadGeoIPASNumFile(String geoIPASNumFile) {
            this.geoIPASNumFile = geoIPASNumFile;
            return this;
        }

        /**
         * 加载GeoIPv6.dat文件
         * @param geoIPv6File 包含国家定位信息,ipv6
         * @return
         */
        public Builder loadGeoIPv6File(String geoIPv6File) {
            this.geoIPv6File = geoIPv6File;
            return this;
        }

        /**
         * 加载GeoLiteCityv6.dat文件
         * @param geoLiteCityv6File 包含城市定位信息,ipv6
         * @return
         */
        public Builder loadGeoLiteCityv6File(String geoLiteCityv6File) {
            this.geoLiteCityv6File = geoLiteCityv6File;
            return this;
        }

        /**
         * 加载GeoIPASNumv6.dat文件
         * @param geoIPASNumv6File 包含as号信息,ipv6
         * @return
         */
        public Builder loadGeoIPASNumv6File(String geoIPASNumv6File) {
            this.geoIPASNumv6File = geoIPASNumv6File;
            return this;
        }

        /**
         * 构建对象
         * @return
         */
        public IpLookupDat build() {
            return new IpLookupDat(this);
        }

    }


   @Override
    public String countryLookup(String ip) {

        String result = UNKNOW;

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



    @Override
    public String cityLookupDetail(String ip) {

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
                    result.append(UNKNOW).append("\t");

                } else {
                    result.append(location.city).append("\t");
                }


                if (StringUtil.isEmpty(location.countryCode)) {
                    result.append(UNKNOW).append("\t");
                } else {
                    result.append(regionName.regionNameByCode(location.countryCode, location.region)).append("\t");
                }



                if (StringUtil.isEmpty(location.countryName)) {
                    result.append(UNKNOW).append("\t");
                } else {
                    result.append(location.countryName).append("\t");
                }

            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result.toString();
    }

    @Override
    public String cityLookup(String ip) {

        String result = UNKNOW;
        try {

            Location location = null;

            if (IPUtil.isIP(ip)) {

                if (IPUtil.internalIp(ip)){
                    return "N/A";
                }
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

    @Override
    public String provinceLookup(String ip) {
        String result = UNKNOW;
        try {

            Location location = null;

            if (IPUtil.isIP(ip)) {

                if (IPUtil.internalIp(ip)){
                    return "N/A";
                }
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

    @Override
    public String latLngLookup(String ip) {
        StringBuilder  result = new StringBuilder(64);
        try {

            Location location = null;

            if (IPUtil.isIP(ip)) {
                location = cityService.getLocation(ip);
            } else if (IPUtil.isIPv6Address(ip)) {
                location = cityServicev6.getLocation(ip);
            }

            if (StringUtil.isNotEmpty(location)) {
                result.append(location.latitude).append("\t") .append(location.longitude);
            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result.toString();
    }


    @Override
    public  String cityLatLngLookup(String ip) {
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
                if (StringUtil.isBlank(result.toString())) {

                    result.append(location.city);
                }

                result.append("\t").append(location.latitude).append("\t") .append(location.longitude);

            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result.toString();

    }



    @Override
    public String asnLookup(String ip) {

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



    @Override
    public String asnLookupDetail(String ip) {
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




    public synchronized  void close() {

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
