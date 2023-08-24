package com.geedgenetworks.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ObjectArrays;
import com.maxmind.db.CHMCache;
import com.geedgenetworks.domain.LocationResponse;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.URL;
import java.util.*;


/**
 * @ClassName IpLookupV2
 * @Description  The IPLookupv2 class enriches information with geographic fields, given an ip address. It works with User-defined Maxmind's GeoIP binary database.
 * @Author darnell
 * @Date 2023-08-22 20:00  Geedge Networks
 * @Version 1.2
 **/
public final class IpLookupV2 extends AbstractIpLookup {
    private AsnLookup asnLookup;
    private final Map<String, List<GalaxyDataBaseReader>> availableDBReaders = Maps.newHashMap();

    private GalaxyDataBaseReader publicDatabaseReader;
    private GalaxyDataBaseReader publicV4DatabaseReader;;
    private GalaxyDataBaseReader publicV6DatabaseReader;;
    private GalaxyDataBaseReader privateDatabaseReader;;
    private GalaxyDataBaseReader privateV4DatabaseReader;;
    private GalaxyDataBaseReader privateV6DatabaseReader;;

    private IpLookupV2(Builder builder) {
        init(builder);
    }

    // Initialization of variables
    synchronized void init(Builder builder) {
        initializeDatabaseReaders(builder);
        initializeAsnLookup(builder);
        initializeAvailableDatabaseReaders();
    }

    private void initializeDatabaseReaders(Builder builder) {
        try {
            if (builder.isDefaultDB) {
                //Initialize IP MMDBs using default paths
                File[] publicDatabaseFiles = buildMMDBFilesUseWorkDirectory(DEFAULT_DB_IP_PUBLIC, DEFAULT_DB_IP_PUBLIC_V4, DEFAULT_DB_IP_PUBLIC_V6);
                File[] privateDatabaseFiles = buildMMDBFilesUseWorkDirectory(DEFAULT_DB_IP_PRIVATE, DEFAULT_DB_IP_PRIVATE_V4, DEFAULT_DB_IP_PRIVATE_V6);
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
                publicDatabaseReader  = constructDatabaseReader(builder.publicDatabaseFile, builder.publicDatabaseStream);
                publicV4DatabaseReader = constructDatabaseReader(builder.publicV4DatabaseFile, builder.publicV4DatabaseStream);
                publicV6DatabaseReader = constructDatabaseReader(builder.publicV6DatabaseFile, builder.publicV6DatabaseStream);
                privateDatabaseReader = constructDatabaseReader(builder.privateDatabaseFile, builder.privateDatabaseStream);
                privateV4DatabaseReader = constructDatabaseReader(builder.privateV4DatabaseFile, builder.privateV4DatabaseStream);
                privateV6DatabaseReader = constructDatabaseReader(builder.privateV6DatabaseFile, builder.privateV6DatabaseStream);
            }
        } catch (Exception e) {
            logger.error("Unsupported GeoIP2 *.mmdb database: expected either File path or Stream. " + e);
        }
    }
    private void initializeAsnLookup(Builder builder) {
        if (builder.isDefaultDB) {
            asnLookup =  new AsnLookup.Builder(true).build();
        } else {
            // Initialize ASN MMDBs using custom paths/streams
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

        availableDBReaders.put(IpVersion.IPv4.name(), ipv4DbReaders);
        availableDBReaders.put(IpVersion.IPv6.name(), ipv6DbReaders);
    }


    private void addIfNotEmpty(List<GalaxyDataBaseReader> dbReaders, GalaxyDataBaseReader dbReader) {
        if (StringUtil.isNotEmpty(dbReader)) {
            dbReaders.add(dbReader);
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
         * @return Builder
         */
        public Builder(boolean isDefaultDB) {
            this.isDefaultDB = isDefaultDB;
        }

        /**
         * @param databaseFile 加载GeoIP2 database 文件，不区分IPv4/6
         * @return
         */
        public Builder loadDataFile(String databaseFile) {
            this.publicDatabaseFile = databaseFile;
            return this;
        }

        /**
         * @param databaseStream 加载GeoIP2流，不区分IPv4/6
         * @return
         */
        public Builder loadDataFile(InputStream databaseStream) {
            this.publicDatabaseStream = databaseStream;
            return this;
        }

        /**
         *
         * @param databaseFile 加载第三方内置GeoIP2 IPv4 database 文件
         * @return
         */
        public Builder loadDataFileV4(String databaseFile) {
            this.publicV4DatabaseFile = databaseFile;
            return this;
        }

        /**
         * @param databaseStream 加载第三方内置GeoIP2 IPv4流
         * @return
         */
        public Builder loadDataFileV4(InputStream databaseStream) {
            this.publicV4DatabaseStream = databaseStream;
            return this;
        }


        /**
         *
         * @param databaseFile 加载第三方内置GeoIP2 IPv6 database 文件
         * @return
         */
        public Builder loadDataFileV6(String databaseFile) {
            this.publicV6DatabaseFile = databaseFile;
            return this;
        }

        /**
         * @param databaseStream 加载第三方内置GeoIP2 IPv6流
         * @return
         */
        public Builder loadDataFileV6(InputStream databaseStream) {
            this.publicV6DatabaseStream = databaseStream;
            return this;
        }


        /**
         *
         * @param databaseFile 加载自定义GeoIP2 database 文件，不区分IPv4/6
         * @return
         */
        public Builder loadDataFilePrivate(String databaseFile) {
            this.privateDatabaseFile = databaseFile;
            return this;
        }

        /**
         *
         * @param databaseStream 加载自定义GeoIP2 流文件，不区分IPv4/6
         * @return
         */
        public Builder loadDataFilePrivate(InputStream databaseStream) {
            this.privateDatabaseStream = databaseStream;
            return this;
        }

        /**
         *
         * @param databaseFile 加载自定义GeoIP2 IPv4 database 文件
         * @return
         */
        public Builder loadDataFilePrivateV4(String databaseFile) {
            this.privateV4DatabaseFile = databaseFile;
            return this;
        }

        /**
         *
         * @param databaseStream 加载自定义GeoIP2 IPv4 流
         * @return
         */
        public Builder loadDataFilePrivateV4(InputStream databaseStream) {
            this.privateV4DatabaseStream = databaseStream;
            return this;
        }

        /**
         *
         * @param databaseFile 加载自定义GeoIP2 IPv6 database 文件
         * @return
         */
        public Builder loadDataFilePrivateV6(String databaseFile) {
            this.privateV6DatabaseFile = databaseFile;
            return this;
        }

        /**
         *
         * @param databaseStream 加载自定义GeoIP2 IPv6流
         * @return
         */
        public Builder loadDataFilePrivateV6(InputStream databaseStream) {
            this.privateV6DatabaseStream = databaseStream;
            return this;
        }



        /**
         *
         * @param asnDatabaseFile 加载ASN database文件,不区分IPv4/6
         * @return
         */
        public Builder loadAsnDataFile(String asnDatabaseFile) {
            this.publicAsnDatabaseFile = asnDatabaseFile;
            return this;
        }

        /**
         *
         * @param asnDatabaseStream 加载ASN database流,不区分IPv4/6
         * @return
         */
        public Builder loadAsnDataFile(InputStream asnDatabaseStream) {
            this.publicAsnDatabaseStream = asnDatabaseStream;
            return this;
        }


        /**
         *
         * @param asnDatabaseFile 加载ASN IPv4 database文件
         * @return
         */
        public Builder loadAsnDataFileV4(String asnDatabaseFile) {
            this.publicAsnV4DatabaseFile = asnDatabaseFile;
            return this;
        }

        /**
         *
         * @param asnDatabaseStream 加载ASN IPv4 database流
         * @return
         */
        public Builder loadAsnDataFileV4(InputStream asnDatabaseStream) {
            this.publicAsnV4DatabaseStream = asnDatabaseStream;
            return this;
        }


        /**
         *
         * @param asnDatabaseFile 加载ASN IPv6 database文件
         * @return
         */
        public Builder loadAsnDataFileV6(String asnDatabaseFile) {
            this.publicAsnV6DatabaseFile = asnDatabaseFile;
            return this;
        }

        /**
         *
         * @param asnDatabaseStream 加载ASN IPv6 database流
         * @return
         */
        public Builder loadAsnDataFileV6(InputStream asnDatabaseStream) {
            this.publicAsnV6DatabaseStream = asnDatabaseStream;
            return this;
        }


        /**
         *
         * @param asnDatabaseFile 加载ASN database文件,不区分IPv4/6
         * @return
         */
        public Builder loadAsnDataFilePrivate(String asnDatabaseFile) {
            this.privateAsnDatabaseFile = asnDatabaseFile;
            return this;
        }

        /**
         *
         * @param asnDatabaseStream 加载ASN database流,不区分IPv4/6
         * @return
         */
        public Builder loadAsnDataFilePrivate(InputStream asnDatabaseStream) {
            this.privateAsnDatabaseStream = asnDatabaseStream;
            return this;
        }

        /**
         *
         * @param asnDatabaseFile 加载ASN IPv4 database文件
         * @return
         */
        public Builder loadAsnDataFilePrivateV4(String asnDatabaseFile) {
            this.privateAsnV4DatabaseFile = asnDatabaseFile;
            return this;
        }

        /**
         *
         * @param asnDatabaseStream 加载ASN IPv4 database流
         * @return
         */
        public Builder loadAsnDataFilePrivateV4(InputStream asnDatabaseStream) {
            this.privateAsnV4DatabaseStream = asnDatabaseStream;
            return this;
        }


        /**
         *
         * @param asnDatabaseFile 加载ASN IPv6 database文件
         * @return Builder
         */
        public Builder loadAsnDataFilePrivateV6(String asnDatabaseFile) {
            this.privateAsnV6DatabaseFile = asnDatabaseFile;
            return this;
        }

        /**
         *
         * @param asnDatabaseStream 加载ASN IPv6 database流
         * @return
         */
        public Builder loadAsnDataFilePrivateV6(InputStream asnDatabaseStream) {
            this.privateAsnV6DatabaseStream = asnDatabaseStream;
            return this;
        }

        /**
         * Build IPLookupV2 instance
         * @return
         * @throws IOException
         */
        public IpLookupV2 build() {
            return new IpLookupV2(this);
        }

    }

    public Map<String, List<GalaxyDataBaseReader>> getDbReaders() {
        return availableDBReaders;
    }

    @Override
    public  String countryLookup(String ip) {
        LocationResponse response = getResponse(ip);
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

        LocationResponse response = getResponse(ip);

        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        if (response.isPrivateIP()) {
            return PRIVATE_IP;
        }

        return Joiner.on(LOCATION_SEPARATOR).useForNull("").join(response.getCountry(),
                        response.getSuperAdministrativeArea(), response.getAdministrativeArea());
    }

    @Override
    public String administrativeAreaLookupDetail(String ip) {
        LocationResponse response = getResponse(ip);

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
        LocationResponse response = getResponse(ip);

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
        LocationResponse response = getResponse(ip);

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
        LocationResponse response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        return StringUtil.setDefaultIfEmpty(response.getCountryCode(), UNKNOWN).toString() ;
    }

    @Override
    public String regionCodeLookup(String ip) {
        LocationResponse response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return  UNKNOWN;
        }
        return StringUtil.setDefaultIfEmpty(response.getAreaCode(), UNKNOWN).toString() ;
    }

    @Override
    public String infoLookupToCSV(String ip) {
        LocationResponse response = getResponse(ip);
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
                        Joiner.on(LAT_LNG_SEPARATOR).skipNulls().join(response.getLatitude(), response.getLongitude())
                );
        return ipInfo.toString();
    }

    @Override
    public String infoLookupToJSONString(String ip) {
        LocationResponse response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return UNKNOWN;
        }
        return JSON.toJSONString(response, JSONWriter.Feature.WriteNullStringAsEmpty);
    }

    @Override
    public Object infoLookupToJSON(String ip) {
        LocationResponse response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return UNKNOWN;
        }
        return JSONObject.from(response);
    }

    @Override
    public Object infoLookup(String ip) {
        LocationResponse response = getResponse(ip);
        if (StringUtil.isEmpty(response)) {
            return UNKNOWN;
        }
        return response;
    }

    @Override
    public String provinceLookup(String ip) {
        LocationResponse response = getResponse(ip);
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
        LocationResponse response = getResponse(ip);
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
        LocationResponse response = getResponse(ip);
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
        LocationResponse response = getResponse(ip);
        if (StringUtil.isEmpty(response) || response.isPrivateIP()) {
            return  UNKNOWN;
        }

        return Joiner.on(LAT_LNG_SEPARATOR).skipNulls().join(response.getLatitude(), response.getLongitude());

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


    private LocationResponse getResponse(String ip) {
        try {
            ip = Optional.ofNullable(ip).orElse("").trim();
            InetAddress ipAddress = InetAddress.getByName(ip);
            if (ipAddress instanceof Inet4Address) {

                //Iterate through each 'dbReader' and execute them in the following order: private v4 -> private -> public v4 -> public.
                for (GalaxyDataBaseReader reader : availableDBReaders.get(IpVersion.IPv4.name())) {
                   LocationResponse response = reader.locationV2(ipAddress);
                   if(StringUtil.isNotEmpty(response)) {
                       return response;
                   }
                }
                //Is it a private IP
                if (IPUtil.internalIp(ip)) {
                    return new LocationResponse(true);
                }

            } else if (ipAddress instanceof Inet6Address) {
                //Iterate through each 'dbReader' and execute them in the following order: private v6 -> private -> public v6 -> public.
                for(GalaxyDataBaseReader reader : availableDBReaders.get(IpVersion.IPv6.name())) {
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











