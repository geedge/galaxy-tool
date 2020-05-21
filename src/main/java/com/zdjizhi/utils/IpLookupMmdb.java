package com.zdjizhi.utils;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.*;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;


import java.io.File;
import java.net.InetAddress;

/**
 *
 * <p>maxmind 官网API结构，对IP定位查找工具类，加载mmdb数据格式文件</p>
 * @author 中电积至有限公司 darnell
 * @version 1.0 创建时间：2018-08-30
 *
 */
@Deprecated
public class IpLookupMmdb extends AbstractIpLookup{

    public final static String DEFAULT_IP_DATABASE_PATH = "dat";

    private static DatabaseReader countryReader = null;
    private static DatabaseReader cityReader = null;
    private static DatabaseReader asnReader = null;

    synchronized void init(Builder builder) {
        try {

            if (builder.isDefalultDB) {
                String datRootPath =  System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator;
                countryReader = new DatabaseReader.Builder(new File(datRootPath + "GeoLite2-Country.mmdb"))
                        .withCache(new CHMCache()).build();

                cityReader = new DatabaseReader.Builder(new File(datRootPath + "GeoLite2-City.mmdb"))
                        .withCache(new CHMCache()).build();

                asnReader = new DatabaseReader.Builder(new File(datRootPath + "GeoLite2-ASN.mmdb"))
                        .withCache(new CHMCache()).build();

            } else {

                if (builder.geoCountryFile != null) {
                    countryReader = new DatabaseReader.Builder(new File(builder.geoCountryFile))
                            .withCache(new CHMCache()).build();
                }

                if (builder.geoCityFile != null) {
                    cityReader = new DatabaseReader.Builder(new File(builder.geoCityFile))
                            .withCache(new CHMCache()).build();
                }

                if (builder.geoAsnFile != null) {
                    asnReader = new DatabaseReader.Builder(new File(builder.geoAsnFile))
                            .withCache(new CHMCache()).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Unknow　GeoIP mmdb dat file , please check your dat path!");

        }


    }

    private IpLookupMmdb(Builder builder) {
        init(builder);
    }


    /**
     * 加载mmdb数据字典文件，内部生成mmdb动态库。
     *
     * 1.默认记载IP库，使用方式如下:
     * IpLookupMmdb ipLookup = new IpLookupMmdb.Builder(true).build();
     * 自动加载应用dat目录下的库文件，命名为:GeoLite2-Country.mmdb\GeoLite2-City.mmdb与GeoLite2-ASN.mmdb库。
     *
     * 2.手动加载IP库，使用方式如下：
     * <p>
     *   IpLookup IpLookupMmdb = new IpLookupMmdb.Builder(false).loadDataFileV4("D:\\galaxy-tool\\dat\\GeoLite2-Country.mmdb").
     *   loadDataFileV6("D:\\galaxy-tool\\dat\\GeoLite2-City.mmdb").loadDataFileV6("D:\\galaxy-tool\\dat\\GeoLite2-ASN.mmdb").build();
     *
     * </p>
     */
    public static final class Builder {
        String geoCountryFile;
        String geoCityFile;
        String geoAsnFile;
        boolean isDefalultDB;

        /**
         *
         * @param isDefalultDB <code>false</code> 手动指定IP库路径 <code>true</code> 使用默认路径
         */
        public Builder(boolean isDefalultDB) {
            this.isDefalultDB = isDefalultDB;
        }

        /**
         *
         * @param geoCountryFile 加载GeoLite2-Country mmdb库文件
         * @return
         */
        public Builder loadGeoCountryFile(String geoCountryFile) {
            this.geoCountryFile = geoCountryFile;
            return this;
        }

        /**
         *
         * @param geoCityFile 加载GeoLite2-City mmdb库文件
         * @return
         */
        public Builder loadGeoCityFile(String geoCityFile) {
            this.geoCityFile = geoCityFile;
            return this;
        }


        /**
         *
         * @param geoAsnFile 加载GeoLite2-ASN mmdb库文件
         * @return
         */
        public Builder loadGeoAsnFile(String geoAsnFile) {
            this.geoAsnFile = geoAsnFile;
            return this;
        }

        /**
         * 构建对象
         * @return
         */
        public IpLookupMmdb build() {
            return new IpLookupMmdb(this);
        }

    }



    @Override
    public String countryLookup(String ip) {

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


    @Override
    public String cityLookupDetail(String ip) {

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

    @Override
    public String cityLookup(String ip) {
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

    @Override
    public String provinceLookup(String ip) {
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

    @Override
    public String latLngLookup(String ip) {
        StringBuilder  result = new StringBuilder(64);
        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response =  cityReader.city(ipAddress);
            Location location = response.getLocation();
            result.append(location.getLatitude()).append("\t").append(location.getLongitude());

        } catch (AddressNotFoundException ae) {
            logger.warn(ae.getMessage());
        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result.toString() ;
    }

    @Override
    public String cityLatLngLookup(String ip) {
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



    @Override
    public String asnLookupDetail(String ip) {

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




    @Override
    public String asnLookup(String ip) {

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




}
