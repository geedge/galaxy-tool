package com.geedgenetworks.utils;

import com.maxmind.db.*;
import com.geedgenetworks.domain.LocationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName GalaxyDataBaseReader
 * @Description TODO
 * @Author dell
 * @Date 2018-10-8 18:30
 * @Version 1.0
 **/
public class GalaxyDataBaseReader  {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Reader reader;
    private GalaxyDataBaseReader(Builder builder) throws IOException {
        if (builder.stream != null) {
            this.reader = new Reader(builder.stream, builder.cache);
        } else if (builder.database != null) {
            this.reader = new Reader(builder.database, builder.mode, builder.cache);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported Builder configuration: expected either File or URL");
        }
    }

    /**
     * <p>
     * Constructs a Builder for the {@code DatabaseReader}. The file passed to
     * it must be a valid GeoIP2 database file.
     * </p>
     * <p>
     * {@code Builder} creates instances of {@code DatabaseReader}
     * from values set by the methods.
     * </p>
     * <p>
     * Only the values set in the {@code Builder} constructor are required.
     * </p>
     */
    public static final class Builder {
        final File database;
        final InputStream stream;

        List<String> locales = Collections.singletonList("en");
        Reader.FileMode mode = Reader.FileMode.MEMORY_MAPPED;
        NodeCache cache = NoCache.getInstance();

        /**
         * @param stream the stream containing the GeoIP2 database to use.
         */
        public Builder(InputStream stream) {
            this.stream = stream;
            this.database = null;
        }

        /**
         * @param database the GeoIP2 database file to use.
         */
        public Builder(File database) {
            this.database = database;
            this.stream = null;
        }

        /**
         * @param val List of locale codes to use in name property from most
         *            preferred to least preferred.
         * @return Builder object
         */
        public Builder locales(List<String> val) {
            this.locales = val;
            return this;
        }

        /**
         * @param cache backing cache instance
         * @return Builder object
         */
        public Builder withCache(NodeCache cache) {
            this.cache = cache;
            return this;
        }

        /**
         * @param val The file mode used to open the GeoIP2 database
         * @return Builder object
         * @throws java.lang.IllegalArgumentException if you initialized the Builder with a URL, which uses
         *                                            {@link Reader.FileMode#MEMORY}, but you provided a different
         *                                            FileMode to this method.
         */
        public Builder fileMode(Reader.FileMode val) {
            if (this.stream != null && Reader.FileMode.MEMORY != val) {
                throw new IllegalArgumentException(
                        "Only FileMode.MEMORY is supported when using an InputStream.");
            }
            this.mode = val;
            return this;
        }

        /**
         * @return an instance of {@code DatabaseReader} created from the
         * fields set on this builder.
         * @throws IOException if there is an error reading the database
         */
        public GalaxyDataBaseReader build() throws IOException {
            return new GalaxyDataBaseReader(this);
        }
    }

    public LocationResponse locationV2(InetAddress ipAddress) throws IOException {
        return reader.get(ipAddress, LocationResponse.class);

    }

    public void close() throws IOException {
        if (this.reader != null) {
            this.reader.close();
        }
    }


    /**
     * @return the metadata for the open MaxMind DB file.
     */
    public Metadata getMetadata() {
        return this.reader.getMetadata();
    }
}
