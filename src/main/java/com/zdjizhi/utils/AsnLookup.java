package com.zdjizhi.utils;

import com.google.common.base.Joiner;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.zdjizhi.domain.LocationResponseV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;

/**
 * @ClassName AsnLookup
 * @Description  IP查询AS号
 * @Author 中电积至有限公司 darnell
 * @Date 2020-05-09 13:45
 * @Version 1.0.3
 **/
public class AsnLookup {
    enum ServiceEnum {
        PRIVATE, PUBLIC
    }
    protected static final String UNKNOWN  =  "";
    protected  static final String SEPARATOR = ",";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final static String DEFAULT_DATABASE_PATH = "dat";
    private final static String DEFAULT_DB_ASN_PUBLIC = "asn.mmdb";
    private final static String DEFAULT_DB_ASN_PUBLIC_V4 = "asn_v4.mmdb";
    private final static String DEFAULT_DB_ASN_PUBLIC_V6 = "asn_v6.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE = "asn_private.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE_V4 = "asn_private_v4.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE_V6 = "asn_private_v6.mmdb";

    private static GalaxyDataBaseReader publicDatabaseReader;
    private static GalaxyDataBaseReader publicV4DatabaseReader;
    private static GalaxyDataBaseReader publicV6DatabaseReader;

    private static GalaxyDataBaseReader privateDatabaseReader;
    private static GalaxyDataBaseReader privateV4DatabaseReader;
    private static GalaxyDataBaseReader privateV6DatabaseReader;


    synchronized void init(AsnLookup.Builder builder) {
        try {

            if (builder.isDefaultDB) {

                File publicDatabaseFile =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PUBLIC);
                if (!publicDatabaseFile.exists()) {
                    URL url = AsnLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PUBLIC);
                    if (url != null) {
                        publicDatabaseFile =  new File(url.getPath());
                    }

                }


                File asnDbPublicV4File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PUBLIC_V4);
                if (!asnDbPublicV4File.exists()) {
                    URL url = AsnLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PUBLIC_V4);
                    if (url != null) {
                        asnDbPublicV4File =  new File(url.getPath());
                    }

                }

                File asnDbPublicV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PUBLIC_V6);
                if (!asnDbPublicV6File.exists()) {
                    URL url = AsnLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PUBLIC_V6);
                    if (url != null) {
                        asnDbPublicV6File =  new File(url.getPath());
                    }
                }

                File asnDbPrivateFile =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PRIVATE);
                if (!asnDbPrivateFile.exists()) {
                    URL url = AsnLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PRIVATE);
                    if (url != null) {
                        asnDbPrivateFile =  new File(url.getPath());
                    }

                }

                File asnDbPrivateV4File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PRIVATE_V4);
                if (!asnDbPrivateV4File.exists()) {
                    URL url = AsnLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PRIVATE_V4);
                    if (url != null) {
                        asnDbPrivateV4File =  new File(url.getPath());
                    }

                }


                File asnDbPrivateV6File =  new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator
                        + DEFAULT_DB_ASN_PRIVATE_V6);
                if (!asnDbPrivateV6File.exists()) {
                    URL url = AsnLookup.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + DEFAULT_DB_ASN_PRIVATE_V6);
                    if (url != null) {
                        asnDbPrivateV6File =  new File(url.getPath());
                    }
                }

                publicDatabaseReader = buildDataReader(publicDatabaseFile);
                publicV4DatabaseReader = buildDataReader(asnDbPublicV4File);
                publicV6DatabaseReader = buildDataReader(asnDbPublicV6File);
                privateDatabaseReader = buildDataReader(asnDbPrivateFile);
                privateV4DatabaseReader = buildDataReader(asnDbPrivateV4File);
                privateV6DatabaseReader = buildDataReader(asnDbPrivateV6File);

            } else {

                if (StringUtil.isNotBlank(builder.publicAsnDatabaseFile)) {
                    publicDatabaseReader = buildDataReader(new File(builder.publicAsnDatabaseFile));
                 } else if (builder.publicAsnDatabaseStream != null) {
                    publicDatabaseReader = buildDataReader(builder.publicAsnDatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.publicAsnV4DatabaseFile)) {
                    publicV4DatabaseReader = buildDataReader((new File(builder.publicAsnV4DatabaseFile)));
                } else if (builder.publicAsnV4DatabaseStream != null) {
                    publicV4DatabaseReader = buildDataReader(builder.publicAsnV4DatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.publicAsnV6DatabaseFile)) {
                    publicV6DatabaseReader = buildDataReader(new File(builder.publicAsnV6DatabaseFile));
                } else if (builder.publicAsnV6DatabaseStream != null) {
                    publicV6DatabaseReader = buildDataReader(builder.publicAsnV6DatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.privateAsnDatabaseFile)) {
                    privateDatabaseReader = buildDataReader(new File(builder.privateAsnDatabaseFile));
                } else if (builder.privateAsnDatabaseStream != null) {
                    privateDatabaseReader = buildDataReader(builder.privateAsnDatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.privateAsnV4DatabaseFile)) {
                    privateV4DatabaseReader = buildDataReader(new File(builder.privateAsnV4DatabaseFile));
                } else if (builder.privateAsnV4DatabaseStream != null) {
                    privateV4DatabaseReader = buildDataReader(builder.privateAsnV4DatabaseStream);
                }

                if (StringUtil.isNotBlank(builder.privateAsnV6DatabaseFile)) {
                    privateV6DatabaseReader = buildDataReader(new File(builder.privateAsnV6DatabaseFile));
                } else if (builder.privateAsnV6DatabaseStream != null) {
                    privateV6DatabaseReader = buildDataReader(builder.privateAsnV6DatabaseStream);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unsupported GeoIP2 *.mmdb database: expected either File path or Stream.");
        }


    }



    private AsnLookup(AsnLookup.Builder builder) {
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
     * 1.默认记载ASN库，使用方式如下:
     * AsnLookup asnLookup = new AsnLookup.Builder(true).build();
     * 自动加载应用dat目录下的库文件，命名为:asn_v4.asn_v6.mmdb库。
     *
     * 2.手动加载IP库，使用方式如下：
     * <p>
     *   AsnLookup asnLookup = new AsnLookup.Builder(false).loadDataFileV4("D:\\galaxy-tool\\dat\\asn_v4_201901.mmdb").
     *   loadDataFileV6("D:\\galaxy-tool\\dat\\asn_v6_201901.mmdb").build();
     *
     * </p>
     */
    public static final class Builder {
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
         *
         * @param publicAsnDatabaseFile  加载ASN database文件,不区分IPv4/6
         * @return
         */
        public Builder loadDataFile(String publicAsnDatabaseFile) {
            this.publicAsnDatabaseFile = publicAsnDatabaseFile;
            return this;
        }

        /**
         *
         * @param publicAsnDatabaseStream 加载ASN database流,不区分IPv4/6
         * @return
         */
        public Builder loadDataFile(InputStream publicAsnDatabaseStream) {
            this.publicAsnDatabaseStream = publicAsnDatabaseStream;
            return this;
        }

        /**
         *
         * @param publicAsnV4DatabaseFile  加载ASN IPv4 database文件
         * @return
         */
        public Builder loadDataFileV4(String publicAsnV4DatabaseFile) {
            this.publicAsnV4DatabaseFile = publicAsnV4DatabaseFile;
            return this;
        }

        /**
         *
         * @param publicAsnV4DatabaseStream 加载ASN IPv4 database流
         * @return
         */
        public Builder loadDataFileV4(InputStream publicAsnV4DatabaseStream) {
            this.publicAsnV4DatabaseStream = publicAsnV4DatabaseStream;
            return this;
        }



        /**
         *
         * @param publicAsnV6DatabaseFile 加载ASN IPv6 database文件
         * @return
         */
        public Builder loadDataFileV6(String publicAsnV6DatabaseFile) {
            this.publicAsnV6DatabaseFile = publicAsnV6DatabaseFile;
            return this;
        }

        /**
         *
         * @param publicAsnV6DatabaseStream 加载ASN IPv6 database流
         * @return
         */
        public Builder loadDataFileV6(InputStream publicAsnV6DatabaseStream) {
            this.publicAsnV6DatabaseStream = publicAsnV6DatabaseStream;
            return this;
        }

        /**
         *
         * @param privateAsnDatabaseFile 加载ASN database文件,不区分IPv4/6
         * @return
         */
        public Builder loadDataFilePrivate(String privateAsnDatabaseFile) {
            this.privateAsnDatabaseFile = privateAsnDatabaseFile;
            return this;
        }

        /**
         *
         * @param privateAsnDatabaseStream 加载ASN database流,不区分IPv4/6
         * @return
         */
        public Builder loadDataFilePrivate(InputStream privateAsnDatabaseStream) {
            this.privateAsnDatabaseStream = privateAsnDatabaseStream;
            return this;
        }



        /**
         *
         * @param privateAsnV4DatabaseFile 加载ASN IPv4 database文件
         * @return
         */
        public Builder loadDataFilePrivateV4(String privateAsnV4DatabaseFile) {
            this.privateAsnV4DatabaseFile = privateAsnV4DatabaseFile;
            return this;
        }

        /**
         *
         * @param privateAsnV4DatabaseStream 加载ASN IPv4 database流
         * @return
         */
        public Builder loadDataFilePrivateV4(InputStream privateAsnV4DatabaseStream) {
            this.privateAsnV4DatabaseStream = privateAsnV4DatabaseStream;
            return this;
        }

        /**
         *
         * @param privateAsnV6DatabaseFile 加载ASN IPv6 database文件
         * @return
         */
        public Builder loadDataFilePrivateV6(String privateAsnV6DatabaseFile) {
            this.privateAsnV6DatabaseFile = privateAsnV6DatabaseFile;
            return this;
        }
        /**
         *
         * @param privateAsnV6DatabaseStream 加载ASN IPv6 database流
         * @return
         */
        public Builder loadDataFilePrivateV6(InputStream privateAsnV6DatabaseStream) {
            this.privateAsnV6DatabaseStream = privateAsnV6DatabaseStream;
            return this;
        }


        /**
         * 构建对象
         * @return
         * @throws IOException
         */
        public AsnLookup build() {
            return new AsnLookup(this);
        }



    }


    public String asnLookup(String ip) {

        LocationResponseV2 response = getResponse(ip);

        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }

        return StringUtil.setDefaultIfEmpty(response.getAsn(), UNKNOWN).toString() ;


    }

    public String asnLookupOrganization(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }
        return response.getOrganization();
    }

    public String asnLookupInfo(String ip) {
        LocationResponseV2 response = getResponse(ip);
        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }
        return Joiner.on(SEPARATOR).useForNull("").join(response.getCountryCode(), response.getOrganization());

    }




    public String asnLookupDetail(String ip) {
        LocationResponseV2 response = getResponse(ip);

        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }
        return Joiner.on(SEPARATOR).useForNull("").join(response.getAsn(), response.getCountryCode(), response.getOrganization());
    }






    private LocationResponseV2 getResponse(String ip) {
        LocationResponseV2 response = null;
        if (IPUtil.isIPv4Address(ip) || IPUtil.isIPv6Address(ip)) {
            response = new Context(new PrivateReader()).executeStragey(ip);
            if (StringUtil.isEmpty(response)) {
                response =  new Context(new PublicReader()).executeStragey(ip);
            }
        } else {
            throw new IllegalArgumentException("Unknown IP format :" + ip );
        }
        return response;
    }




    private GalaxyDataBaseReader getAsnDataBaseReaderV4(String service) {

        if (service.equalsIgnoreCase(ServiceEnum.PRIVATE.name())) {
            if (StringUtil.isNotEmpty(privateV4DatabaseReader)) {
                return privateV4DatabaseReader;
            } else  {
                return privateDatabaseReader;
            }
        } else  {
            if (StringUtil.isNotEmpty(publicV4DatabaseReader)) {
                return publicV4DatabaseReader;
            } else{
                return publicDatabaseReader;
            }
        }
    }

    private GalaxyDataBaseReader getAsnDataBaseReaderV6(String service) {

        if (service.equalsIgnoreCase(ServiceEnum.PRIVATE.name())) {
            if (StringUtil.isNotEmpty(privateV6DatabaseReader)) {
                return privateV6DatabaseReader;
            } else  {
                return privateDatabaseReader;
            }
        } else  {
            if (StringUtil.isNotEmpty(publicV6DatabaseReader)) {
                return publicV6DatabaseReader;
            } else{
                return publicDatabaseReader;
            }
        }
    }



    interface Strategy {
        LocationResponseV2 getResponse(String ip);
    }

    /**
     * 用户自定义IP库的执行策略
     */
    class PrivateReader implements Strategy {
        @Override
        public LocationResponseV2 getResponse(String ip) {
            GalaxyDataBaseReader dataBaseReader = null;
            LocationResponseV2 response = null;
            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                if (IPUtil.isIPv4Address(ip)) {
                    dataBaseReader = getAsnDataBaseReaderV4(ServiceEnum.PRIVATE.name());
                } else if (IPUtil.isIPv6Address(ip)) {
                    dataBaseReader = getAsnDataBaseReaderV6(ServiceEnum.PRIVATE.name());
                }

                if (StringUtil.isNotEmpty(dataBaseReader)) {
                    response = dataBaseReader.locationV2(ipAddress);
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
     * 第三方服务库的执行策略，例如maxmind、IPDAT
     */
    class PublicReader implements Strategy {
        @Override
        public LocationResponseV2 getResponse(String ip) {
            GalaxyDataBaseReader dataBaseReader = null;
            LocationResponseV2 response = null;

            try {
                InetAddress ipAddress = InetAddress.getByName(ip);

                if (IPUtil.isIPv4Address(ip)) {
                    dataBaseReader = getAsnDataBaseReaderV4(ServiceEnum.PUBLIC.name());

                } else if (IPUtil.isIPv6Address(ip)) {
                    dataBaseReader = getAsnDataBaseReaderV6(ServiceEnum.PUBLIC.name());

                }

                if (StringUtil.isNotEmpty(dataBaseReader)) {
                    response = dataBaseReader.locationV2(ipAddress);
                }

            } catch(AddressNotFoundException addressNotFoundException) {
                logger.debug("Address not found ,ip is :" + ip);
            } catch (Exception e) {
                throw new IllegalArgumentException("ip address :" + ip +", parser error " + e);
            }
            return response;


        }
    }



    class Context {
        private Strategy strategy;

        public Context(Strategy strategy) {
            this.strategy = strategy;
        }

        public LocationResponseV2 executeStragey(String ip) {

            return strategy.getResponse(ip);
        }

    }






}
