package com.zdjizhi.utils;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.zdjizhi.domain.LocationResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @ClassName IpLookup
 * @Description  对IP定位查找工具类
 * @Author 中电积至有限公司 darnell
 * @Date 2018-10-8 16:48
 * @Version 1.0
 **/
public final class IpLookup extends AbstractIpLookup{

    public final static String DEFAULT_IP_DATABASE_PATH = "dat";

    private static GalaxyDataBaseReader locationReaderV4;
    private static GalaxyDataBaseReader locationReaderV6;


    synchronized void init(Builder builder) {
        try {

            if (builder.isDefalultDB) {
                String datRootPath =  System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator;

                locationReaderV4 = new GalaxyDataBaseReader.Builder(new File(datRootPath + "all_ip_info_v4.mmdb"))
                        .withCache(new CHMCache()).build();
                locationReaderV6 = new GalaxyDataBaseReader.Builder(new File(datRootPath + "all_ip_info_v6.mmdb"))
                        .withCache(new CHMCache()).build();

            } else {

                if (builder.databaseFileV4 != null) {
                    locationReaderV4 = new GalaxyDataBaseReader.Builder(new File(builder.databaseFileV4))
                            .withCache(new CHMCache()).build();
                }

                if (builder.databaseFileV6 != null) {
                    locationReaderV6 = new GalaxyDataBaseReader.Builder(new File(builder.databaseFileV6))
                            .withCache(new CHMCache()).build();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Unknow mmdb dat file , please check your dat path!");

        }


    }


    private IpLookup(Builder builder) {
        init(builder);
    }



    /**
     * 加载mmdb数据字典文件，内部生成mmdb动态库。
     *
     * 1.默认记载IP库，使用方式如下:
     * IpLookup ipLookup = new IpLookup.Builder(true).build();
     * 自动加载应用dat目录下的库文件，命名为:all_ip_info_v4.mmdb与all_ip_info_v6.mmdb库。
     *
     * 2.手动加载IP库，使用方式如下：
     * <p>
     *   IpLookup ipLookup = new IpLookup.Builder(false).loadDataFileV4("D:\\galaxy-tool\\dat\\all_ip_info_v4.mmdb").
     *   loadDataFileV6("D:\\galaxy-tool\\dat\\all_ip_info_v6.mmdb").build();
     *
     * </p>
     */
    public static final class Builder {
        String databaseFileV4;
        String databaseFileV6;
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
         * @param databaseFileV4 加载IPv4 mmdb库文件
         * @return
         */
        public Builder loadDataFileV4(String databaseFileV4) {
            this.databaseFileV4 = databaseFileV4;
            return this;
        }

        /**
         *
         * @param databaseFileV6 加载IPv6 mmdb库文件
         * @return
         */
        public Builder loadDataFileV6(String databaseFileV6) {
            this.databaseFileV6 = databaseFileV6;
            return this;
        }

        /**
         * 构建对象
         * @return
         * @throws IOException
         */
        public IpLookup build() {
            return new IpLookup(this);
        }

    }

    @Override
    public  String countryLookup(String ip) {
        String result = UNKNOW;

        LocationResponse response = getResponse(ip);

        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.setDefaultIfEmpty(response.getCountry(), "").toString() ;
        }
        return result;
    }

    @Override
    public String cityLookupDetail(String ip) {

        StringBuffer result = new StringBuffer(64);

        try {

            LocationResponse response = getResponse(ip);

            if (StringUtil.isNotEmpty(response)) {

                if (StringUtil.isBlank(response.getCity())) {
                    result.append(UNKNOW).append("\t");
                } else {
                    result.append(response.getCity()).append("\t");
                }


                if (StringUtil.isBlank(response.getProvince())) {
                    result.append(UNKNOW).append("\t");
                } else {
                    result.append(response.getProvince()).append("\t");
                }

                if (StringUtil.isBlank(response.getCountry())) {
                    result.append(UNKNOW);
                } else {
                    result.append(response.getCountry());
                }
            } else {
                result.append(UNKNOW).append("\t").append(UNKNOW).append("\t").append(UNKNOW);
            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result.toString();
    }

    @Override
    public String cityLookup(String ip) {
        String result = UNKNOW;
        LocationResponse response = getResponse(ip);

        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.isNotBlank(response.getProvince()) ? response.getProvince() : response.getCity();
        }

        return result;

    }

    @Override
    public  String latLngLookup(String ip) {
        StringBuilder  result = new StringBuilder(64);
        try {
            LocationResponse response = getResponse(ip);
            if (StringUtil.isNotEmpty(response)) {

                if (StringUtil.isBlank(response.getLatitude())) {
                    result.append(UNKNOW).append("\t");
                } else {
                    result.append(response.getLatitude()).append("\t");
                }

                if (StringUtil.isBlank(response.getLongitude())) {
                    result.append(UNKNOW);
                } else {
                    result.append(response.getLongitude());
                }
            } else {
                result.append(UNKNOW).append("\t").append("N/A");
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
            result.append(cityLookup(ip)).append("\t");
            LocationResponse response = getResponse(ip);
            if (StringUtil.isNotEmpty(response)) {

                if (StringUtil.isBlank(response.getLatitude())) {
                    result.append(UNKNOW).append("\t");
                } else {
                    result.append(response.getLatitude()).append("\t");
                }

                if (StringUtil.isBlank(response.getLongitude())) {
                    result.append(UNKNOW);
                } else {
                    result.append(response.getLongitude());
                }
            } else {
                result.append(UNKNOW).append("\t").append("N/A");
            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result.toString();

    }

    @Override
    public String asnLookup(String ip) {
        String result = UNKNOW;
        LocationResponse response = getResponse(ip);

        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.isNotBlank(response.getAsn()) ? response.getAsn() : UNKNOW;
        }

        return result;

    }


    @Override
    public String asnLookupDetail(String ip) {
        String result = UNKNOW;
        LocationResponse response = getResponse(ip);

        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.setDefaultIfEmpty(response.getAsn(), UNKNOW) + "\t"  + StringUtil.setDefaultIfEmpty(response.getIsp(), UNKNOW);
        }


        return result;

    }




    private LocationResponse getResponse(String ip) {
        LocationResponse response = null;
        if (!IPUtil.isIP(ip) && !IPUtil.isIPv6Address(ip)) {
            throw new IllegalArgumentException("unknown ip format :" + ip );
        }

        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            if (IPUtil.isIP(ip)) {
                response = locationReaderV4.location(ipAddress);
            } else if (IPUtil.isIPv6Address(ip)){
                response = locationReaderV6.location(ipAddress);
            }

        } catch(AddressNotFoundException addressNotFoundException) {
            response = null;
        } catch (Exception e) {
            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return response;
    }




    }











