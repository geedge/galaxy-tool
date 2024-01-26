package com.geedgenetworks.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.geedgenetworks.model.LocationResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Optional;
/**
 * @ClassName AsnLookup
 * @Description  IP查询AS号
 * @Author 中电积至有限公司 darnell
 * @Date 2020-05-09 13:45
 * @Version 1.0.3
 **/
public class AsnLookup extends AbstractDatabaseReader {
    private Map<String, List<GalaxyDataBaseReader>> availableDBReaders = Maps.newHashMap();
    private GalaxyDataBaseReader publicDatabaseReader;
    private GalaxyDataBaseReader publicV4DatabaseReader;
    private GalaxyDataBaseReader publicV6DatabaseReader;
    private GalaxyDataBaseReader privateDatabaseReader;
    private GalaxyDataBaseReader privateV4DatabaseReader;
    private GalaxyDataBaseReader privateV6DatabaseReader;

    synchronized void init(Builder builder) {
        initializeDatabaseReaders(builder);
        initializeAvailableDatabaseReaders();
    }
    private void initializeDatabaseReaders(Builder builder) {
        try {
            if (builder.isDefaultDB) {
                //Initialize IP MMDBs using default paths
                File[] publicDatabaseFiles = buildMMDBFilesUseWorkDirectory(DEFAULT_DB_ASN_PUBLIC, DEFAULT_DB_ASN_PUBLIC_V4, DEFAULT_DB_ASN_PUBLIC_V6);
                File[] privateDatabaseFiles = buildMMDBFilesUseWorkDirectory(DEFAULT_DB_ASN_PRIVATE, DEFAULT_DB_ASN_PRIVATE_V4, DEFAULT_DB_ASN_PRIVATE_V6);
                updateFileIfNotExists(publicDatabaseFiles);
                updateFileIfNotExists(privateDatabaseFiles);
                publicDatabaseReader = constructDatabaseReader(publicDatabaseFiles[0]);
                publicV4DatabaseReader = constructDatabaseReader(publicDatabaseFiles[1]);
                publicV6DatabaseReader = constructDatabaseReader(publicDatabaseFiles[2]);
                privateDatabaseReader = constructDatabaseReader(privateDatabaseFiles[0]);
                privateV4DatabaseReader = constructDatabaseReader(privateDatabaseFiles[1]);
                privateV6DatabaseReader = constructDatabaseReader(privateDatabaseFiles[2]);
            } else {
                //Initialize IP MMDBs using custom paths/streams
                publicDatabaseReader  = constructDatabaseReader(builder.publicAsnDatabaseFile, builder.publicAsnDatabaseStream);
                publicV4DatabaseReader = constructDatabaseReader(builder.publicAsnV4DatabaseFile, builder.publicAsnV4DatabaseStream);
                publicV6DatabaseReader = constructDatabaseReader(builder.publicAsnV6DatabaseFile, builder.publicAsnV6DatabaseStream);
                privateDatabaseReader = constructDatabaseReader(builder.privateAsnDatabaseFile, builder.privateAsnDatabaseStream);
                privateV4DatabaseReader = constructDatabaseReader(builder.privateAsnV4DatabaseFile, builder.privateAsnV4DatabaseStream);
                privateV6DatabaseReader = constructDatabaseReader(builder.privateAsnV6DatabaseFile, builder.privateAsnV6DatabaseStream);
            }
        } catch (Exception e) {
            logger.error("Unsupported GeoIP2 *.mmdb database: expected either File path or Stream. " + e);
        }
    }

    private void initializeAvailableDatabaseReaders() {
        List<GalaxyDataBaseReader> ipv4DbReaders = Lists.newArrayList();
        List<GalaxyDataBaseReader> ipv6DbReaders = Lists.newArrayList();

        addIfNotEmpty(ipv4DbReaders, privateV4DatabaseReader);
        addIfNotEmpty(ipv6DbReaders, privateV6DatabaseReader);
        //Add Private IP library
        addIfNotEmpty(ipv4DbReaders, privateDatabaseReader);
        addIfNotEmpty(ipv6DbReaders, privateDatabaseReader);

        addIfNotEmpty(ipv4DbReaders, publicV4DatabaseReader);
        addIfNotEmpty(ipv6DbReaders, publicV6DatabaseReader);
        //Add Public IP library
        addIfNotEmpty(ipv4DbReaders, publicDatabaseReader);
        addIfNotEmpty(ipv6DbReaders, publicDatabaseReader);

        availableDBReaders.put(AbstractDatabaseReader.IpVersion.IPv4.name(), ipv4DbReaders);
        availableDBReaders.put(AbstractDatabaseReader.IpVersion.IPv6.name(), ipv6DbReaders);
    }

    private AsnLookup(AsnLookup.Builder builder) {
        init(builder);
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
         * @param databaseFile  加载ASN database文件,不区分IPv4/6
         * @return
         */
        public Builder loadDataFile(String databaseFile) {
            this.publicAsnDatabaseFile = databaseFile;
            return this;
        }

        /**
         *
         * @param databaseStream 加载ASN database流,不区分IPv4/6
         * @return
         */
        public Builder loadDataFile(InputStream databaseStream) {
            this.publicAsnDatabaseStream = databaseStream;
            return this;
        }

        /**
         *
         * @param databaseFile  加载ASN IPv4 database文件
         * @return
         */
        public Builder loadDataFileV4(String databaseFile) {
            this.publicAsnV4DatabaseFile = databaseFile;
            return this;
        }

        /**
         *
         * @param databaseStream 加载ASN IPv4 database流
         * @return
         */
        public Builder loadDataFileV4(InputStream databaseStream) {
            this.publicAsnV4DatabaseStream = databaseStream;
            return this;
        }



        /**
         *
         * @param databaseFile 加载ASN IPv6 database文件
         * @return
         */
        public Builder loadDataFileV6(String databaseFile) {
            this.publicAsnV6DatabaseFile = databaseFile;
            return this;
        }

        /**
         *
         * @param databaseStream 加载ASN IPv6 database流
         * @return
         */
        public Builder loadDataFileV6(InputStream databaseStream) {
            this.publicAsnV6DatabaseStream = databaseStream;
            return this;
        }

        /**
         *
         * @param databaseFile 加载ASN database文件,不区分IPv4/6
         * @return
         */
        public Builder loadDataFilePrivate(String databaseFile) {
            this.privateAsnDatabaseFile = databaseFile;
            return this;
        }

        /**
         *
         * @param databaseStream 加载ASN database流,不区分IPv4/6
         * @return
         */
        public Builder loadDataFilePrivate(InputStream databaseStream) {
            this.privateAsnDatabaseStream = databaseStream;
            return this;
        }

        /**
         *
         * @param databaseFile 加载ASN IPv4 database文件
         * @return
         */
        public Builder loadDataFilePrivateV4(String databaseFile) {
            this.privateAsnV4DatabaseFile = databaseFile;
            return this;
        }

        /**
         *
         * @param databaseStream 加载ASN IPv4 database流
         * @return
         */
        public Builder loadDataFilePrivateV4(InputStream databaseStream) {
            this.privateAsnV4DatabaseStream = databaseStream;
            return this;
        }

        /**
         *
         * @param databaseFile 加载ASN IPv6 database文件
         * @return
         */
        public Builder loadDataFilePrivateV6(String databaseFile) {
            this.privateAsnV6DatabaseFile = databaseFile;
            return this;
        }
        /**
         *
         * @param databaseStream 加载ASN IPv6 database流
         * @return
         */
        public Builder loadDataFilePrivateV6(InputStream databaseStream) {
            this.privateAsnV6DatabaseStream = databaseStream;
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

        LocationResponse response = getResponse(ip);

        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }

        return StringUtil.setDefaultIfEmpty(response.getAsn(), UNKNOWN).toString() ;

    }

    public String asnLookupOrganization(String ip) {
        LocationResponse response = getResponse(ip);
        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }
        return response.getOrganization();
    }

    public String asnLookupInfo(String ip) {
        LocationResponse response = getResponse(ip);
        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }
        return Joiner.on(DEFAULT_SEPARATOR).useForNull("").join(response.getCountryCode(), response.getOrganization());

    }




    public String asnLookupDetail(String ip) {
        LocationResponse response = getResponse(ip);

        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }
        return Joiner.on(DEFAULT_SEPARATOR).useForNull("").join(response.getAsn(), response.getCountryCode(), response.getOrganization());
    }

    private LocationResponse getResponse(String ip) {
        try {
            ip = Optional.ofNullable(ip).orElse("").trim();
            InetAddress ipAddress = InetAddress.getByName(ip);
            if (ipAddress instanceof Inet4Address) {
                //Iterate through each 'dbReader' and execute them in the following order: private v4 -> private -> public v4 -> public.
                for (GalaxyDataBaseReader reader : availableDBReaders.get(AbstractDatabaseReader.IpVersion.IPv4.name())) {
                    LocationResponse response = reader.location(ipAddress);
                    if(StringUtil.isNotEmpty(response)) {
                        return response;
                    }
                }
            } else if (ipAddress instanceof Inet6Address) {
                //Iterate through each 'dbReader' and execute them in the following order: private v6 -> private -> public v6 -> public.
                for(GalaxyDataBaseReader reader : availableDBReaders.get(AbstractDatabaseReader.IpVersion.IPv6.name())) {
                    LocationResponse response = reader.location(ipAddress);
                    if(StringUtil.isNotEmpty(response)) {
                        return  response;
                    }
                }
            } else {
                throw new IllegalArgumentException("IP address <" + ip +">. ");
            }
            return null;
        }  catch (Exception e) {
            throw new IllegalArgumentException("IP address <" + ip +">. " + e);
        }


    }
}
