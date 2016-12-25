package tech.oleks.pmtalk.service;

import com.google.api.client.util.DateTime;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by alexm on 12/10/16.
 */
@Singleton
public class ManagedService {

    @Inject Configuration config;

    final Logger log = Logger.getLogger(getClass().getName());

    public void start() throws IOException {}
    public void stop() {}
}
