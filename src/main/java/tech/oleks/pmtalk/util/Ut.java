package tech.oleks.pmtalk.util;

import com.google.api.client.util.DateTime;

/**
 * Created by alexm on 12/25/16.
 */
public class Ut {
    public static String getLink(String t, String id) {
        return t.replaceAll("%FILE_ID%", id);
    }

    public static DateTime toDateTime(org.joda.time.DateTime t) {
        return new DateTime(t.toString("yyyy-MM-dd'T'HH:mm:ssZZ"));
    }

    public static String notNull(String v, String name) {
        if (v == null || v.isEmpty()) {
            throw new IllegalArgumentException("'" + name + "' file is null");
        }
        return v;
    }
}
