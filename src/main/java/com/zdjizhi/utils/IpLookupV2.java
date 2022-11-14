package com.zdjizhi.utils;

import com.google.common.base.Joiner;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.zdjizhi.domain.LocationResponseV2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Optional;

/**
 * @ClassName IpLookupV2
 * @Description  IP定位库与TSG四级IP定位库命名体系保持一致
 * @Author 中电积至有限公司 darnell
 * @Date 2022-01-27 15:01
 * @Version 1.0.8
 **/
public final class IpLookupV2 extends AbstractIpLookup{

    enum ServiceEnum {
        PRIVATE, PUBLIC
    }
    public final static String DEFAULT_DATABASE_PATH = "dat";
    private final static String DEFAULT_DB_IP_PUBLIC = "ip.mmdb";
    private final static String DEFAULT_DB_IP_PUBLIC_V4 = "ip_v4.mmdb";
    private final static String DEFAULT_DB_IP_PUBLIC_V6 = "ip_v6.mmdb";
    private final static String DEFAULT_DB_IP_PRIVATE = "ip_private.mmdb";
    private final static String DEFAULT_DB_IP_PRIVATE_V4 = "ip_private_v4.mmdb";
    private final static String DEFAULT_DB_IP_PRIVATE_V6 = "ip_private_v6.mmdb";

    private AsnLookup asnLookup;

    private static GalaxyDataBaseReader publicDatabaseReader;
    private static GalaxyDataBaseReader publicV4DatabaseReader;
    private static GalaxyDataBaseReader publicV6DatabaseReader;

    private static GalaxyDataBaseReader privateDatabaseReader;
    private static GalaxyDataBaseReader privateV4DatabaseReader;
    private static GalaxyDataBaseReader privateV6DatabaseReader;

    synchronized void init(Builder builder) {
        try {

            if (builder.isDefaultDB) {

                File publicDatabaseFile = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PUBLIC);

                if (!publicDatabaseFile.exists()) {
                    URL url = IpLookupV2.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PUBLIC);
                    if (url != null) {
                        publicDatabaseFile =  new File(url.getPath());
                    }
                }

                File dbPublicV4File = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PUBLIC_V4);

                if (!dbPublicV4File.exists()) {
                    URL url = IpLookupV2.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PUBLIC_V4);
                    if (url != null) {
                        dbPublicV4File =  new File(url.getPath());
                    }
                }

                File dbPublicV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PUBLIC_V6);
                if (!dbPublicV6File.exists()) {
                    URL url = IpLookupV2.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PUBLIC_V6);
                    if (url != null) {
                        dbPublicV6File =  new File(url.getPath());
                    }
                }

                File dbPrivateFile = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        +DEFAULT_DB_IP_PRIVATE);

                if (!dbPrivateFile.exists()) {
                    URL url = IpLookupV2.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PRIVATE);
                    if (url != null) {
                        dbPrivateFile =  new File(url.getPath());
                    }
                }


                File dbPrivateV4File = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PRIVATE_V4);

                if (!dbPrivateV4File.exists()) {
                    URL url = IpLookupV2.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PRIVATE_V4);
                    if (url != null) {
                        dbPrivateV4File =  new File(url.getPath());
                    }
                }

                File dbPrivateV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_IP_PRIVATE_V6);
                if (!dbPrivateV6File.exists()) {
                    URL url = IpLookupV2.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_IP_PRIVATE_V6);
                    if (url != null) {
                        dbPrivateV6File =  new File(url.getPath());
                    }
                }


                publicDatabaseReader = buildDataReader(publicDatabaseFile);
                publicV4DatabaseReader = buildDataReader(dbPublicV4File);
                publicV6DatabaseReader = buildDataReader(dbPublicV6File);
                privateDatabaseReader = buildDataReader(dbPrivateFile);
                privateV4DatabaseReader = buildDataReader(dbPrivateV4File);
                privateV6DatabaseReader = buildDataReader(dbPrivateV6File);

                asnLookup =  new AsnLookup.Builder(true).build();

            } else {

                if (StringUtil.isNotBlank(builder.publicDatabaseFile)) {
                    publicDatabaseReader = buildDataReader(new File(builder.publicDatabaseFile));
                } else if (builder.publicDatabaseStream != null) {
                    publicDatabaseReader = buildDataReader(builder.publicDatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.publicV4DatabaseFile)) {
                    publicV4DatabaseReader = buildDataReader(new File(builder.publicV4DatabaseFile));
                } else if (builder.publicV4DatabaseStream != null) {
                    publicV4DatabaseReader = buildDataReader(builder.publicV4DatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.publicV6DatabaseFile)) {
                    publicV6DatabaseReader = buildDataReader(new File(builder.publicV6DatabaseFile));
                } else if (builder.publicV6DatabaseStream != null) {
                    publicV6DatabaseReader = buildDataReader(builder.publicV6DatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.privateDatabaseFile)) {
                    privateDatabaseReader = buildDataReader(new File(builder.privateDatabaseFile));
                } else if (builder.privateDatabaseStream != null) {
                    privateDatabaseReader = buildDataReader(builder.privateDatabaseStream);

                }

                if (StringUtil.isNotBlank(builder.privateV4DatabaseFile)) {
                    privateV4DatabaseReader = buildDataReader(new File(builder.privateV4DatabaseFile));
                } else if (builder.privateV4DatabaseStream != null) {
                    privateV4DatabaseReader = buildDataReader(builder.privateV4DatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.privateV6DatabaseFile)) {
                    privateV6DatabaseReader = buildDataReader(new File(builder.privateV6DatabaseFile));
                } else if (builder.privateV6DatabaseStream != null) {
                    privateV6DatabaseReader = buildDataReader(builder.privateV6DatabaseStream);
                }

                asnLookup =  new AsnLookup.Builder(false)
                        .loadDataFile(builder.publicAsnDatabaseFile)
                        .loadDataFile(builder.publicAsnDatabaseStream)
                        .loadDataFileV4(builder.publicAsnV4DatabaseFile)
                        .loadDataFileV4(builder.publicAsnV4DatabaseStream)
                        .loadDataFileV6(builder.publicAsnV6DatabaseFile)
                        .loadDataFileV6(builder.publicAsnV6DatabaseStream)
                        .loadDataFilePrivate(builder.privateAsnDatabaseFile)
                        .loadDataFilePrivate(builder.privateAsnDatabaseStream)
                        .loadDataFilePrivateV4(builder.privateAsnV4DatabaseFile)
                        .loadDataFilePrivateV4(builder.privateAsnV4DatabaseStream)
                        .loadDataFilePrivateV6(builder.privateAsnV6DatabaseFile)
                        .loadDataFilePrivateV6(builder.privateAsnV6DatabaseStream)
                        .build();
            }

        } catch (Exception e) {
            logger.error("Unsupported GeoIP2 *.mmdb database: expected either File path or Stream. " + e);
        }


    }


    private IpLookupV2(Builder builder) {
        init(builder);
    }

    private GalaxyDataBaseReader buildDataReader(File database) throws IOException {
        if (database.exists()) {
            return new GalaxyDataBaseReader.Builder(database).withCache(new CHMCache()).build();
        } else {
            return null;
        }
    }

    private GalaxyDataBaseReader buildDataReader(InputStream stream) throws IOException {
        if (stream != null) {
            return new GalaxyDataBaseReader.Builder(stream).withCache(new CHMCache()).build();
        } else {
            return null;
        }
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
     *   IpLookup ipLookup = new IpLookup.Builder(false).loadDataFileV4("D:\\galaxy-tool\\dat\\ip.mmdb").
     *   loadDataFileV6("D:\\galaxy-tool\\dat\\all_ip_info_v6.mmdb").build();
     *
     * </p>
     */
    public static final class Builder {
        String publicDatabaseFile;
        String publicV4DatabaseFile;
        String publicV6DatabaseFile;

        InputStream publicDatabaseStream;
        InputStream publicV4DatabaseStream;
        InputStream publicV6DatabaseStream;

        String privateDatabaseFile;
        String privateV4DatabaseFile;
        String privateV6DatabaseFile;

        InputStream privateDatabaseStream;
        InputStream privateV4DatabaseStream;
        InputStream privateV6DatabaseStream;

        String publicAsnDatabaseFile;
        String publicAsnV4DatabaseFile;
        String publicAsnV6DatabaseFile;

        InputStream publicAsnDatabaseStream;
        InputStream publicAsnV4DatabaseStream;
        InputStream publicAsnV6DatabaseStream;

        String privateAsnDatabaseFile;
        String privateAsnV4DatabaseFile;
        String privateAsnV6DatabaseFile;

        InputStream privateAsnDatabaseStream;
        InputStream privateAsnV4DatabaseStream;
        InputStream privateAsnV6DatabaseStream;

        boolean isDefaultDB;

        /**
         *
         * @param isDefaultDB <code>false</code> 手动指定IP库路径 <code>true</code> 使用默认路径
         */
        public Builder(boolean isDefaultDB) {
            this.isDefaultDB = isDefaultDB;
        }

        /**
         * @param publicDatabaseFile 加载GeoIP2 database 文件，不区分IPv4/6
         * @return
         */
        public Builder loadDataFile(String publicDatabaseFile) {
            this.publicDatabaseFile = publicDatabaseFile;
            return this;
        }

        /**
         * @param publicDatabaseStream 加载GeoIP2流，不区分IPv4/6
         * @return
         */
        public Builder loadDataFile(InputStream publicDatabaseStream) {
            this.publicDatabaseStream = publicDatabaseStream;
            return this;
        }

        /**
         *
         * @param publicV4DatabaseFile 加载第三方内置GeoIP2 IPv4 database 文件
         * @return
         */
        public Builder loadDataFileV4(String publicV4DatabaseFile) {
            this.publicV4DatabaseFile = publicV4DatabaseFile;
            return this;
        }

        /**
         * @param publicV4DatabaseStream 加载第三方内置GeoIP2 IPv4流
         * @return
         */
        public Builder loadDataFileV4(InputStream publicV4DatabaseStream) {
            this.publicV4DatabaseStream = publicV4DatabaseStream;
            return this;
        }


        /**
         *
         * @param publicV6DatabaseFile 加载第三方内置GeoIP2 IPv6 database 文件
         * @return
         */
        public Builder loadDataFileV6(String publicV6DatabaseFile) {
            this.publicV6DatabaseFile = publicV6DatabaseFile;
            return this;
        }

        /**
         * @param publicV6DatabaseStream 加载第三方内置GeoIP2 IPv6流
         * @return
         */
        public Builder loadDataFileV6(InputStream publicV6DatabaseStream) {
            this.publicV6DatabaseStream = publicV6DatabaseStream;
            return this;
        }


        /**
         *
         * @param privateDatabaseFile 加载自定义GeoIP2 database 文件，不区分IPv4/6
         * @return
         */
        public Builder loadDataFilePrivate(String privateDatabaseFile) {
            this.privateDatabaseFile = privateDatabaseFile;
            return this;
        }

        /**
         *
         * @param privateDatabaseStream 加载自定义GeoIP2 流，不区分IPv4/6
         * @return
         */
        public Builder loadDataFilePrivate(InputStream privateDatabaseStream) {
            this.privateDatabaseStream = privateDatabaseStream;
            return this;
        }

        /**
         *
         * @param privateV4DatabaseFile 加载自定义GeoIP2 IPv4 database 文件
         * @return
         */
        public Builder loadDataFilePrivateV4(String privateV4DatabaseFile) {
            this.privateV4DatabaseFile = privateV4DatabaseFile;
            return this;
        }

        /**
         *
         * @param privateV4DatabaseStream 加载自定义GeoIP2 IPv4 流
         * @return
         */
        public Builder loadDataFilePrivateV4(InputStream privateV4DatabaseStream) {
            this.privateV4DatabaseStream = privateV4DatabaseStream;
            return this;
        }

        /**
         *
         * @param privateV6DatabaseFile 加载自定义GeoIP2 IPv6 database 文件
         * @return
         */
        public Builder loadDataFilePrivateV6(String privateV6DatabaseFile) {
            this.privateV6DatabaseFile = privateV6DatabaseFile;
            return this;
        }

        /**
         *
         * @param privateV6DatabaseStream 加载自定义GeoIP2 IPv6流
         * @return
         */
        public Builder loadDataFilePrivateV6(InputStream privateV6DatabaseStream) {
            this.privateV6DatabaseStream = privateV6DatabaseStream;
            return this;
        }



        /**
         *
         * @param publicAsnDatabaseFile 加载ASN database文件,不区分IPv4/6
         * @return
         */
        public Builder loadAsnDataFile(String publicAsnDatabaseFile) {
            this.publicAsnDatabaseFile = publicAsnDatabaseFile;
            return this;
        }

        /**
         *
         * @param publicAsnDatabaseStream 加载ASN database流,不区分IPv4/6
         * @return
         */
        public Builder loadAsnDataFile(InputStream publicAsnDatabaseStream) {
            this.publicAsnDatabaseStream = publicAsnDatabaseStream;
            return this;
        }


        /**
         *
         * @param publicAsnV4DatabaseFile 加载ASN IPv4 database文件
         * @return
         */
        public Builder loadAsnDataFileV4(String publicAsnV4DatabaseFile) {
            this.publicAsnV4DatabaseFile = publicAsnV4DatabaseFile;
            return this;
        }

        /**
         *
         * @param publicAsnV4DatabaseStream 加载ASN IPv4 database流
         * @return
         */
        public Builder loadAsnDataFileV4(InputStream publicAsnV4DatabaseStream) {
            this.publicAsnV4DatabaseStream = publicAsnV4DatabaseStream;
            return this;
        }


        /**
         *
         * @param publicAsnV6DatabaseFile 加载ASN IPv6 database文件
         * @return
         */
        public Builder loadAsnDataFileV6(String publicAsnV6DatabaseFile) {
            this.publicAsnV6DatabaseFile = publicAsnV6DatabaseFile;
            return this;
        }

        /**
         *
         * @param publicAsnV6DatabaseStream 加载ASN IPv6 database流
         * @return
         */
        public Builder loadAsnDataFileV6(InputStream publicAsnV6DatabaseStream) {
            this.publicAsnV6DatabaseStream = publicAsnV6DatabaseStream;
            return this;
        }


        /**
         *
         * @param privateAsnDatabaseFile 加载ASN database文件,不区分IPv4/6
         * @return
         */
        public Builder loadAsnDataFilePrivate(String privateAsnDatabaseFile) {
            this.privateAsnDatabaseFile = privateAsnDatabaseFile;
            return this;
        }

        /**
         *
         * @param privateAsnDatabaseStream 加载ASN database流,不区分IPv4/6
         * @return
         */
        public Builder loadAsnDataFilePrivate(InputStream privateAsnDatabaseStream) {
            this.privateAsnDatabaseStream = privateAsnDatabaseStream;
            return this;
        }

        /**
         *
         * @param privateAsnV4DatabaseFile 加载ASN IPv4 database文件
         * @return
         */
        public Builder loadAsnDataFilePrivateV4(String privateAsnV4DatabaseFile) {
            this.privateAsnV4DatabaseFile = privateAsnV4DatabaseFile;
            return this;
        }

        /**
         *
         * @param privateAsnV4DatabaseStream 加载ASN IPv4 database流
         * @return
         */
        public Builder loadAsnDataFilePrivateV4(InputStream privateAsnV4DatabaseStream) {
            this.privateAsnV4DatabaseStream = privateAsnV4DatabaseStream;
            return this;
        }


        /**
         *
         * @param privateAsnV6DatabaseFile 加载ASN IPv6 database文件
         * @return
         */
        public Builder loadAsnDataFilePrivateV6(String privateAsnV6DatabaseFile) {
            this.privateAsnV6DatabaseFile = privateAsnV6DatabaseFile;
            return this;
        }

        /**
         *
         * @param privateAsnV6DatabaseStream 加载ASN IPv6 database流
         * @return
         */
        public Builder loadAsnDataFilePrivateV6(InputStream privateAsnV6DatabaseStream) {
            this.privateAsnV6DatabaseStream = privateAsnV6DatabaseStream;
            return this;
        }

        /**
         * 构建对象
         * @return
         * @throws IOException
         */
        public IpLookupV2 build() {
            return new IpLookupV2(this);
        }

    }

    @Override
    public  String countryLookup(String ip) {

        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }

        return StringUtil.setDefaultIfEmpty(response.getCountry(), UNKNOWN).toString() ;
    }

    @Override
    public String cityLookupDetail(String ip) {

        LocationResponseV2 response = getResponse(ip);

        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }

        return
                Joiner.on(LOCATION_SEPARATOR).useForNull("").join(response.getCountry(),
                        response.getSuperAdministrativeArea(), response.getAdministrativeArea());
    }

    @Override
    public String administrativeAreaLookupDetail(String ip) {
        LocationResponseV2 response = getResponse(ip);

        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }

        return  Joiner.on(LOCATION_SEPARATOR).useForNull("").join(response.getCountry(),
                response.getSuperAdministrativeArea(), response.getAdministrativeArea(),
                response.getSubAdministrativeArea());

    }

    @Override
    public String locationLookupDetail(String ip) {
        LocationResponseV2 response = getResponse(ip);

        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }

        return  Joiner.on(LOCATION_SEPARATOR).useForNull("").join(response.getCountry(),
                response.getSuperAdministrativeArea(), response.getAdministrativeArea(),
                response.getSubAdministrativeArea(), response.getLocality(),
                response.getDependentLocality(), response.getDoubleDependentLocality());
    }

    @Override
    public String cityLookup(String ip) {
        LocationResponseV2 response = getResponse(ip);

        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }

        return StringUtil.setDefaultIfEmpty(response.getAdministrativeArea(), UNKNOWN).toString() ;

    }

    @Override
    public String countryCodeLookup(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        return StringUtil.setDefaultIfEmpty(response.getCountryCode(), UNKNOWN).toString() ;
    }

    @Override
    public String regionCodeLookup(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        return StringUtil.setDefaultIfEmpty(response.getAreaCode(), UNKNOWN).toString() ;
    }

    @Override
    public String infoLookupToCSV(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return UNKNOWN;
        }
        StringBuilder ipInfo = new StringBuilder();
        ipInfo.append(
                Joiner.on(LOCATION_SEPARATOR).useForNull("").join(response.getCountry(),
                        response.getSuperAdministrativeArea(), response.getAdministrativeArea(),
                        response.getSubAdministrativeArea(), response.getLocality(),
                        response.getDependentLocality(), response.getDoubleDependentLocality()))
                .append(OBJECT_SEPARATOR).append(
                        StringUtil.setDefaultIfEmpty(response.getIsp(), UNKNOWN).toString()
                ).append(OBJECT_SEPARATOR).append(
                        StringUtil.setDefaultIfEmpty(response.getOrganization(), UNKNOWN).toString()
                ).append(OBJECT_SEPARATOR).append(
                        asnLookup.asnLookup(ip)
                ).append(OBJECT_SEPARATOR).append(
                        Joiner.on(LATLNG_SEPARATOR).skipNulls().join(response.getLatitude(), response.getLongitude())
                );

        return ipInfo.toString();
    }


    @Override
    public String infoLookupToJson(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            response = new LocationResponseV2();
        }
        response.setAsn(asnLookup(ip));
        return JsonMapper.toJsonString(response);
    }


    @Override
    public String provinceLookup(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }
        return StringUtil.setDefaultIfEmpty(response.getSuperAdministrativeArea(), UNKNOWN).toString() ;
    }

    @Override
    public String ispLookup(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }
        return StringUtil.setDefaultIfEmpty(response.getIsp(), UNKNOWN).toString() ;
    }

    @Override
    public String organizationLookup(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }
        return StringUtil.setDefaultIfEmpty(response.getOrganization(), UNKNOWN).toString() ;
    }




    @Override
    public  String latLngLookup(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }

        return Joiner.on(LATLNG_SEPARATOR).skipNulls().join(response.getLatitude(), response.getLongitude());

    }

    @Override
    public  String cityLatLngLookup(String ip) {
        return  Joiner.on(OBJECT_SEPARATOR).skipNulls().join(cityLookup(ip), latLngLookup(ip));
    }

    @Override
    public String asnLookup(String ip) {
       return asnLookup.asnLookup(ip);
    }

    @Override
    public String asnLookupOrganization(String ip) {
        return asnLookup.asnLookupOrganization(ip);
    }
    @Override
    public String asnLookupInfo(String ip) {
        return asnLookup.asnLookupInfo(ip);
    }

    @Override
    public String asnLookupDetail(String ip) {
        return asnLookup.asnLookupDetail(ip);
    }

    private LocationResponseV2 getResponse(String ip) {

        ip = Optional.ofNullable(ip).orElse("").trim();

        if (!IPUtil.isIPAddress(ip)) {
            throw new IllegalArgumentException("Unknown IP format :" + ip );
        }

        LocationResponseV2  response = new Context(new PrivateReader()).executeStrategy(ip);

        if (StringUtil.isEmpty(response)) {
            response =  new Context(new PublicReader()).executeStrategy(ip);
        }

        if (StringUtil.isEmpty(response)) {
            response =  new Context(new InnerReader()).executeStrategy(ip);
        }

        return response;
    }



    private GalaxyDataBaseReader getIpDataBaseReaderV4(String service) {

        if (service.equalsIgnoreCase(ServiceEnum.PRIVATE.name())) {
            if (StringUtil.isNotEmpty(privateV4DatabaseReader)) {
                return privateV4DatabaseReader;
            } else {
                return privateDatabaseReader;
            }
        } else  {
            if (StringUtil.isNotEmpty(publicV4DatabaseReader)) {
                return publicV4DatabaseReader;
            } else {
                return publicDatabaseReader;
            }
        }

    }

    private GalaxyDataBaseReader getIpDataBaseReaderV6(String service) {

        if (service.equalsIgnoreCase(ServiceEnum.PRIVATE.name())) {
            if (StringUtil.isNotEmpty(privateV6DatabaseReader)) {
                return privateV6DatabaseReader;
            } else {
                return privateDatabaseReader;
            }
        } else  {
            if (StringUtil.isNotEmpty(publicV6DatabaseReader)) {
                return publicV6DatabaseReader;
            } else {
                return publicDatabaseReader;
            }
        }

    }

    interface Strategy {
        LocationResponseV2 getResponse(String ip);
    }

    /**
     * 用户自定义库的执行策略
     */
    class PrivateReader implements Strategy {
        @Override
        public LocationResponseV2 getResponse(String ip) {
            GalaxyDataBaseReader dataBaseReader = null;
            LocationResponseV2 response = null;
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                if (IPUtil.isIPv4Address(ip)) {
                    dataBaseReader = getIpDataBaseReaderV4(ServiceEnum.PRIVATE.name());
                } else if (IPUtil.isIPv6Address(ip)) {
                    dataBaseReader = getIpDataBaseReaderV6(ServiceEnum.PRIVATE.name());
                }

                if (StringUtil.isNotEmpty(dataBaseReader)) {
                    response = dataBaseReader.locationV2(ipAddress);
                }

            } catch(AddressNotFoundException addressNotFoundException) {
               logger.debug("Address not found ,ip is :" + ip);
            } catch (Exception e) {
                throw new IllegalArgumentException("IP address :" + ip +", parser error " + e);
            }
            return response;
        }
    }
    /**
     * 公共库(系统内置）库执行策略
     */
    class PublicReader implements Strategy {
        @Override
        public LocationResponseV2 getResponse(String ip) {
            GalaxyDataBaseReader dataBaseReader = null;
            LocationResponseV2 response = null;
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                if (IPUtil.isIPv4Address(ip)) {
                    dataBaseReader = getIpDataBaseReaderV4(ServiceEnum.PUBLIC.name());

                } else if (IPUtil.isIPv6Address(ip)) {
                    dataBaseReader = getIpDataBaseReaderV6(ServiceEnum.PUBLIC.name());

                }

                if (StringUtil.isNotEmpty(dataBaseReader)) {
                    response =  dataBaseReader.locationV2(ipAddress);
                }

            } catch(AddressNotFoundException addressNotFoundException) {
                logger.debug("Address not found ,ip is :" + ip);
            } catch (Exception e) {
                throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
            }
            return response;


        }
    }

    /**
     * 私有IP执行策略
     */
    class InnerReader implements  Strategy {
        @Override
        public LocationResponseV2 getResponse(String ip) {
            LocationResponseV2 response = null;
            if(IPUtil.isIPv4Address(ip) && IPUtil.internalIp(ip)) {
                response = new LocationResponseV2();
                response.setPrivateIP(true);
            }
            return response;
        }
    }


    class Context {
        private Strategy strategy;

        public Context(Strategy strategy) {
            this.strategy = strategy;
        }

        public LocationResponseV2 executeStrategy(String ip) {
            return strategy.getResponse(ip);
        }

    }




}











