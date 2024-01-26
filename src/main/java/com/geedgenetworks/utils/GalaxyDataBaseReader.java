package com.geedgenetworks.utils;

import com.maxmind.db.*;
import com.geedgenetworks.model.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GalaxyDataBaseReader implements Closeable {
    protected final Logger logger = LoggerFactory.getLogger(GalaxyDataBaseReader.class);
    private final Reader reader;
    private final List<String> locales;
    private final int databaseType;
    private GalaxyDataBaseReader(Builder builder) throws IOException {
        if (builder.stream != null) {
            this.reader = new Reader(builder.stream, builder.cache);
        } else if (builder.database != null) {
            this.reader = new Reader(builder.database, builder.mode, builder.cache);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported Builder configuration: expected either File or URL");
        }
        this.locales = builder.locales;
        this.databaseType = this.getDatabaseType();
    }

    private int getDatabaseType() {
        String databaseType = this.getMetadata().getDatabaseType();
        int type = 0;
        if (databaseType.contains("IPLOCATOR")) {
            type |= DatabaseType.IPLOCATOR.type;
        }
        if (type == 0) {
            throw new UnsupportedOperationException("Invalid attempt to open an unknown database type: " + databaseType);
        } else {
            return type;
        }
    }

    /**
     * @deprecated use {@link #getLocation(InetAddress)} instead
     * @param ipAddress
     * @return
     * @throws IOException
     */
    @Deprecated
    public  Optional<LocationResponse> location(InetAddress ipAddress) throws IOException {
        LocationResponse response= reader.get(ipAddress, LocationResponse.class);
        return response == null ? Optional.empty() : Optional.of(response);
    }

   public Optional<LocationResponse> getLocation(InetAddress ipAddress) throws IOException {
        DatabaseRecord<LocationResponse> record = this.get(ipAddress, LocationResponse.class, DatabaseType.IPLOCATOR);
        LocationResponse response = record.getData();
        if (response == null) {
            return Optional.empty();
        } else {
            response.setLocales(this.locales);
            response.setNetwork(record.getNetwork());
            response.setIpAddress(ipAddress.getHostAddress());
            return Optional.of(response);
        }
    }

    private <T> DatabaseRecord<T> get(InetAddress ipAddress, Class<T> cls, DatabaseType expectedType) throws IOException {
        if ((this.databaseType & expectedType.type) == 0) {
            String caller = Thread.currentThread().getStackTrace()[3].getMethodName();
            String var10002 = this.getMetadata().getDatabaseType();
            throw new UnsupportedOperationException("Invalid attempt to open a " + var10002 + " database using the " + caller + " method");
        } else {
            return reader.getRecord(ipAddress, cls);
        }
    }
    public void close() throws IOException {
        if (this.reader != null) {
            this.reader.close();
        }
    }

    public Metadata getMetadata() {
        return this.reader.getMetadata();
    }

    public static final class Builder {
        final File database;
        final InputStream stream;

        List<String> locales = Collections.singletonList("en");
        Reader.FileMode mode;
        NodeCache cache;

        public Builder(InputStream stream) {
            this.mode = Reader.FileMode.MEMORY_MAPPED;
            this.cache = NoCache.getInstance();
            this.stream = stream;
            this.database = null;
        }

        /**
         * @param database the GeoIP2 database file to use.
         */
        public Builder(File database) {
            this.mode = Reader.FileMode.MEMORY_MAPPED;
            this.cache = NoCache.getInstance();
            this.database = database;
            this.stream = null;
        }

        public Builder locales(List<String> val) {
            this.locales = val;
            return this;
        }

        public Builder withCache(NodeCache cache) {
            this.cache = cache;
            return this;
        }

        public Builder fileMode(Reader.FileMode val) {
            if (this.stream != null && Reader.FileMode.MEMORY != val) {
                throw new IllegalArgumentException(
                        "Only FileMode.MEMORY is supported when using an InputStream.");
            } else {
                this.mode = val;
                return this;
            }
        }

        public GalaxyDataBaseReader build() throws IOException {
            return new GalaxyDataBaseReader(this);
        }
    }
    private static enum DatabaseType {
        IPLOCATOR;

        final int type = 1 << this.ordinal();

        private DatabaseType() {
        }
    }

}
