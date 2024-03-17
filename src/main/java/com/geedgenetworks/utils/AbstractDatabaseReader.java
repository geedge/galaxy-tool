package com.geedgenetworks.utils;

import com.geedgenetworks.shaded.com.google.common.collect.ObjectArrays;
import com.maxmind.db.CHMCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * @ClassName AbstractIpLookup
 * @Description 对IP定位查找抽象类，规定可执行方法
 * @Author darnell
 * @Date 2018-10-9 19:20
 * @Version 1.0
 **/
public abstract class AbstractDatabaseReader {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String UNKNOWN  =  "";
    public static final String LOCATION_SEPARATOR = ".";
    public static final String LAT_LNG_SEPARATOR = ",";

    public  static final String DEFAULT_SEPARATOR = ",";
    public static final String OBJECT_SEPARATOR = "\t";
    public static final String PRIVATE_IP = "Private IP";

    protected static final String DEFAULT_DATABASE_PATH = "dat";
    protected static final String DEFAULT_DB_IP_PUBLIC = "ip.mmdb";
    protected static final String DEFAULT_DB_IP_PUBLIC_V4 = "ip_v4_built_in.mmdb";
    protected static final String DEFAULT_DB_IP_PUBLIC_V6 = "ip_v6.mmdb";
    protected static final String DEFAULT_DB_IP_PRIVATE = "ip_private.mmdb";
    protected static final String DEFAULT_DB_IP_PRIVATE_V4 = "ip_private_v4.mmdb";
    protected static final String DEFAULT_DB_IP_PRIVATE_V6 = "ip_private_v6.mmdb";

    protected static final String DEFAULT_DB_ASN_PUBLIC = "asn.mmdb";
    protected static final String DEFAULT_DB_ASN_PUBLIC_V4 = "asn_v4.mmdb";
    protected static final String DEFAULT_DB_ASN_PUBLIC_V6 = "asn_v6.mmdb";
    protected static final String DEFAULT_DB_ASN_PRIVATE = "asn_private.mmdb";
    protected static final String DEFAULT_DB_ASN_PRIVATE_V4 = "asn_private_v4.mmdb";
    protected static final String DEFAULT_DB_ASN_PRIVATE_V6 = "asn_private_v6.mmdb";

    public enum Type {
        PRIVATE, PUBLIC
    }
    public enum IpVersion {
        IPv4, IPv6
    }

    protected GalaxyDataBaseReader constructDatabaseReader(String filePath, InputStream stream) {
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

    protected GalaxyDataBaseReader constructDatabaseReader(File file) throws IOException {
        return file.exists() ? new GalaxyDataBaseReader.Builder(file).withCache(new CHMCache()).build() : null;
    }

    protected GalaxyDataBaseReader constructDatabaseReader(InputStream stream) throws IOException {
        return stream != null ? new GalaxyDataBaseReader.Builder(stream).withCache(new CHMCache()).build() : null;
    }

    protected void addIfNotEmpty(List<GalaxyDataBaseReader> dbReaders, GalaxyDataBaseReader dbReader) {
        if (StringUtil.isNotEmpty(dbReader)) {
            dbReaders.add(dbReader);
        }

    }

    protected File[] buildMMDBFilesUseWorkDirectory(String... files) {
        File[] dbFiles = ObjectArrays.newArray(File.class, files.length);
        for (int i = 0; i < files.length; i++) {
            dbFiles[i] = new File(System.getProperty("user.dir") + File.separator + DEFAULT_DATABASE_PATH + File.separator + files[i]);
        }
        return dbFiles;
    }

    protected void updateFileIfNotExists(File[] files) {
        for (File file : files) {
            if (!file.exists()) {
                URL url = IpLookupV2.class.getResource("/"+ DEFAULT_DATABASE_PATH + "/" + file.getName());
                if (url != null) {
                    file = new File(url.getPath());
                }
            }
        }

    }




}
