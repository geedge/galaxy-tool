package com.geedgenetworks.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.maxmind.db.CHMCache;
import com.geedgenetworks.domain.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.geedgenetworks.utils.AbstractIpLookup.UNKNOWN;

/**
 * @ClassName AsnLookup
 * @Description  IP查询AS号
 * @Author 中电积至有限公司 darnell
 * @Date 2020-05-09 13:45
 * @Version 1.0.3
 **/
public class AsnLookup {

    public enum Type {
        PRIVATE, PUBLIC
    }
    protected  static final String SEPARATOR = ",";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final static String DEFAULT_DATABASE_PATH = "dat";
    private final static String DEFAULT_DB_ASN_PUBLIC = "asn.mmdb";
    private final static String DEFAULT_DB_ASN_PUBLIC_V4 = "asn_v4.mmdb";
    private final static String DEFAULT_DB_ASN_PUBLIC_V6 = "asn_v6.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE = "asn_private.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE_V4 = "asn_private_v4.mmdb";
    private final static String DEFAULT_DB_ASN_PRIVATE_V6 = "asn_private_v6.mmdb";
    private final Map<String, List<GalaxyDataBaseReader>> availableDBReaders = Maps.newHashMap();
    private static GalaxyDataBaseReader publicDatabaseReader;
    private static GalaxyDataBaseReader publicV4DatabaseReader;
    private static GalaxyDataBaseReader publicV6DatabaseReader;

    private static GalaxyDataBaseReader privateDatabaseReader;
    private static GalaxyDataBaseReader privateV4DatabaseReader;
    private static GalaxyDataBaseReader privateV6DatabaseReader;

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

    private File[] buildMMDBFilesUseWorkDirectory(String... files) {
        File[] dbFiles = ObjectArrays.newArray(File.class, files.length);
        for (int i = 0; i < files.length; i++) {
            dbFiles[i] = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator + files[i]);
        }
        return dbFiles;
    }

    private void updateFileIfNotExists(File[] files) {
        for (File file : files) {
            if (!file.exists()) {
                URL url = IpLookupV2.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + file.getName());
                if (url != null) {
                    file = new File(url.getPath());
                }
            }
        }

    }


    private GalaxyDataBaseReader constructDatabaseReader(String filePath, InputStream stream)  {
        try {
            if (StringUtil.isNotBlank(filePath) && new File(filePath).exists()) {
                return constructDatabaseReader(new File(filePath));
            } else if (stream != null) {
                return constructDatabaseReader(stream);
            } else {
                return null;
            }
        } catch (IOException ie) {
            logger.error("Unsupported GeoIP2 *.mmdb database: expected either File path or Stream. " + ie);
        }
        throw new UnsupportedOperationException("Unsupported GeoIP2 *.mmdb database: expected either File path or Stream. " );
    }

    private GalaxyDataBaseReader constructDatabaseReader(File file) throws IOException {
        return file.exists() ? new GalaxyDataBaseReader.Builder(file).withCache(new CHMCache()).build() : null;
    }

    private GalaxyDataBaseReader constructDatabaseReader(InputStream stream) throws IOException {
        return stream != null ? new GalaxyDataBaseReader.Builder(stream).withCache(new CHMCache()).build() : null;
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

        availableDBReaders.put(AbstractIpLookup.IpVersion.IPv4.name(), ipv4DbReaders);
        availableDBReaders.put(AbstractIpLookup.IpVersion.IPv6.name(), ipv6DbReaders);
    }

    private void addIfNotEmpty(List<GalaxyDataBaseReader> dbReaders, GalaxyDataBaseReader dbReader) {
        if (StringUtil.isNotEmpty(dbReader)) {
            dbReaders.add(dbReader);
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
        return Joiner.on(SEPARATOR).useForNull("").join(response.getCountryCode(), response.getOrganization());

    }




    public String asnLookupDetail(String ip) {
        LocationResponse response = getResponse(ip);

        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }
        return Joiner.on(SEPARATOR).useForNull("").join(response.getAsn(), response.getCountryCode(), response.getOrganization());
    }


    private LocationResponse getResponse(String ip) {
        try {
            ip = Optional.ofNullable(ip).orElse("").trim();
            InetAddress ipAddress = InetAddress.getByName(ip);
            if (ipAddress instanceof Inet4Address) {
                //Iterate through each 'dbReader' and execute them in the following order: private v4 -> private -> public v4 -> public.
                for (GalaxyDataBaseReader reader : availableDBReaders.get(AbstractIpLookup.IpVersion.IPv4.name())) {
                    LocationResponse response = reader.locationV2(ipAddress);
                    if(StringUtil.isNotEmpty(response)) {
                        return response;
                    }
                }
            } else if (ipAddress instanceof Inet6Address) {
                //Iterate through each 'dbReader' and execute them in the following order: private v6 -> private -> public v6 -> public.
                for(GalaxyDataBaseReader reader : availableDBReaders.get(AbstractIpLookup.IpVersion.IPv6.name())) {
                    LocationResponse response = reader.locationV2(ipAddress);
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
