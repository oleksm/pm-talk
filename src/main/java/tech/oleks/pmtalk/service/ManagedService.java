package tech.oleks.pmtalk.service;

import com.google.api.client.util.DateTime;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;

/**
 * Created by alexm on 12/10/16.
 */
@Singleton
public class ManagedService {

    @Inject Configuration config;

    public void start() throws IOException {}
    public void stop() {}

    String notNull(String v, String name) {
        if (v == null || v.isEmpty()) {
            throw new IllegalArgumentException("'" + name + "' file is null");
        }
        return v;
    }

    /**
     *
     * @param t
     * @return
     */
    DateTime toDateTime(org.joda.time.DateTime t) {
        return new DateTime(t.toString("yyyy-MM-dd'T'HH:mm:ssZZ"));
    }
}
