package com.zdjizhi.utils;

import com.google.common.base.Joiner;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.zdjizhi.domain.LocationResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.StringJoiner;

/**
 * @ClassName IpLookup
 * @Description  对IP定位查找工具类
 * @Author 中电积至有限公司 darnell
 * @Date 2018-10-8 16:48
 * @Version 1.0
 **/
public final class IpLookup extends AbstractIpLookup{


    public final static String DEFAULT_IP_DATABASE_PATH = "dat";
    private final static String SEPRATOR = ",";

    private static GalaxyDataBaseReader ipLocationReaderV4;
    private static GalaxyDataBaseReader ipLocationReaderV6;
    private static GalaxyDataBaseReader asnLocationReaderV4;
    private static GalaxyDataBaseReader asnLocationReaderV6;



    synchronized void init(Builder builder) {
        try {

            if (builder.isDefalultDB) {

                File dbV4File = new File(System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator
                        + "all_ip_info_v4.mmdb");

                if (!dbV4File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_IP_DATABASE_PATH + "/" + "all_ip_info_v4.mmdb");
                    if (url != null) {
                        dbV4File =  new File(url.getPath());
                    }
                }

                File dbV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator
                        + "all_ip_info_v6.mmdb");
                if (!dbV6File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_IP_DATABASE_PATH + "/" + "all_ip_info_v6.mmdb");
                    if (url != null) {
                        dbV6File =  new File(url.getPath());
                    }
                }

                File asnDbV4File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator
                        + "asn_v4.mmdb");
                if (!asnDbV4File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_IP_DATABASE_PATH + "/" + "asn_v4.mmdb");
                    if (url != null) {
                        asnDbV4File =  new File(url.getPath());
                    }

                }


                File asnDbV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_IP_DATABASE_PATH + File.separator
                        + "asn_v6.mmdb");
                if (!asnDbV6File.exists()) {
                    URL url = IpLookup.class.getResource("/"+ DEFAULT_IP_DATABASE_PATH + "/" + "asn_v6.mmdb");
                    if (url != null) {
                        asnDbV6File =  new File(url.getPath());
                    }
                }


                if (dbV4File.exists()) {
                    ipLocationReaderV4 = new GalaxyDataBaseReader.Builder(dbV4File).withCache(new CHMCache()).build();
                }

                if (dbV6File.exists()) {
                    ipLocationReaderV6 = new GalaxyDataBaseReader.Builder(dbV6File).withCache(new CHMCache()).build();
                }


                if (asnDbV4File.exists()) {
                    asnLocationReaderV4 = new GalaxyDataBaseReader.Builder(asnDbV4File).withCache(new CHMCache()).build();
                }

                if (asnDbV6File.exists()) {
                    asnLocationReaderV6 = new GalaxyDataBaseReader.Builder(asnDbV6File).withCache(new CHMCache()).build();
                }


            } else {

                if (builder.ipDatabaseFileV4 != null) {
                    ipLocationReaderV4 = new GalaxyDataBaseReader.Builder(new File(builder.ipDatabaseFileV4))
                            .withCache(new CHMCache()).build();
                }

                if (builder.ipDatabaseFileV6 != null) {
                    ipLocationReaderV6 = new GalaxyDataBaseReader.Builder(new File(builder.ipDatabaseFileV6))
                            .withCache(new CHMCache()).build();
                }

                if (builder.asnDatabaseFileV4 != null) {
                    asnLocationReaderV4 = new GalaxyDataBaseReader.Builder(new File(builder.asnDatabaseFileV4))
                            .withCache(new CHMCache()).build();
                }

                if (builder.asnDatabaseFileV6 != null) {
                    asnLocationReaderV6 = new GalaxyDataBaseReader.Builder(new File(builder.asnDatabaseFileV6))
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
        String ipDatabaseFileV4;
        String ipDatabaseFileV6;
        String asnDatabaseFileV4;
        String asnDatabaseFileV6;
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
         * @param ipDatabaseFileV4 加载IPv4 mmdb库文件
         * @return
         */
        public Builder loadDataFileV4(String ipDatabaseFileV4) {
            this.ipDatabaseFileV4 = ipDatabaseFileV4;
            return this;
        }

        /**
         *
         * @param ipDatabaseFileV6 加载IPv6 mmdb库文件
         * @return
         */
        public Builder loadDataFileV6(String ipDatabaseFileV6) {
            this.ipDatabaseFileV6 = ipDatabaseFileV6;
            return this;
        }


        /**
         *
         * @param asnDatabaseFileV4 加载asn IPv4 mmdb库文件
         * @return
         */
        public Builder loadAsnDataFileV4(String asnDatabaseFileV4) {
            this.asnDatabaseFileV4 = asnDatabaseFileV4;
            return this;
        }



        /**
         *
         * @param asnDatabaseFileV6 加载asn IPv6 mmdb库文件
         * @return
         */
        public Builder loadAsnDataFileV6(String asnDatabaseFileV6) {
            this.asnDatabaseFileV6 = asnDatabaseFileV6;
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

       String result;

        try {

            LocationResponse response = getResponse(ip);

            if (StringUtil.isNotEmpty(response)) {

                result = Joiner.on(SEPRATOR).skipNulls().join(response.getCity(), response.getProvince(), response.getCountry());

            } else {
                result = UNKNOW;
            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result;
    }

    @Override
    public String cityLookup(String ip) {
        String result = UNKNOW;
        LocationResponse response = getResponse(ip);

        if (StringUtil.isNotEmpty(response)) {
            if (StringUtil.isNotBlank(response.getProvince())) {
                result = response.getProvince();
            } else if(StringUtil.isNotBlank(response.getCity())) {
                result = response.getCity();
            } else {
                result = response.getCountry();
            }

        }

        return result;

    }

    @Override
    public  String latLngLookup(String ip) {

        String result;
        try {
            LocationResponse response = getResponse(ip);
            if (StringUtil.isNotEmpty(response)
                && StringUtil.isNotBlank(response.getLatitude())
                && StringUtil.isNotBlank(response.getLongitude())) {


                result = Joiner.on(SEPRATOR).skipNulls().join(response.getLatitude(), response.getLongitude());

            } else {
                result = Joiner.on(SEPRATOR).join(UNKNOW, UNKNOW);
            }

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return result;

    }


    @Override
    public  String cityLatLngLookup(String ip) {
        String result;
        try {

            result = Joiner.on(SEPRATOR).skipNulls().join(cityLookup(ip), latLngLookup(ip));

        } catch (Exception e) {

            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }


        return result;

    }

    @Override
    public String asnLookup(String ip) {
       return asnLookup(ip, false);

    }

    @Override
    public String asnLookup(String ip, boolean isAsnDB) {
        String result = UNKNOW;

        LocationResponse response = getResponse(ip,isAsnDB);

        if (StringUtil.isNotEmpty(response)) {

            result = StringUtil.isNotBlank(response.getAsn()) ? response.getAsn() : UNKNOW;
        }

        return result;
    }


    @Override
    public String asnLookupDetail(String ip) {

        return asnLookupDetail(ip, false);


    }



    @Override
    public String asnLookupDetail(String ip, boolean isAsnDB) {
        String result = UNKNOW;
        LocationResponse response = getResponse(ip, isAsnDB);

        if (StringUtil.isNotEmpty(response)) {
            result = StringUtil.setDefaultIfEmpty(response.getAsn(), UNKNOW) + "\t"  + StringUtil.setDefaultIfEmpty(response.getIsp(), UNKNOW);
        }


        return result;
    }

    private LocationResponse getResponse(String ip, boolean isAsnDB) {

        LocationResponse response = null;

        if (!IPUtil.isIP(ip) && !IPUtil.isIPv6Address(ip)) {
            throw new IllegalArgumentException("unknown ip format :" + ip );
        }


        //private ip
        if (IPUtil.isIP(ip) && IPUtil.internalIp(ip)) {
             response = new LocationResponse();
             response.setCountry("Private IP");
             response.setCity(null);
             response.setProvince(null);
             response.setAsn(null);
             response.setIsp(null);
             response.setLatitude(null);
             response.setLongitude(null);
             response.setAreaCode(null);

            return  response;
        }

        try {

            InetAddress ipAddress = InetAddress.getByName(ip);

            if (isAsnDB) {
                if (IPUtil.isIP(ip)) {
                    response = asnLocationReaderV4.location(ipAddress);
                } else if (IPUtil.isIPv6Address(ip)){
                    response = asnLocationReaderV6.location(ipAddress);
                }
            } else {

                if (IPUtil.isIP(ip)) {
                    response = ipLocationReaderV4.location(ipAddress);
                } else if (IPUtil.isIPv6Address(ip)){
                    response = ipLocationReaderV6.location(ipAddress);
                }
            }


        } catch(AddressNotFoundException addressNotFoundException) {
            response = null;
        } catch (Exception e) {
            throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
        }

        return response;
    }



    private LocationResponse getResponse(String ip) {
        return getResponse(ip, false);
    }




    }











