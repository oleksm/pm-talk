package tech.oleks.pmtalk.service;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import tech.oleks.pmtalk.util.ConfigurationLoader;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * Created by alexm on 12/16/16.
 */
@Singleton
public class ApplicationService extends ManagedService {

    ServletContext servletContext;
    volatile boolean started;

    @Override
    public void start() throws IOException {
        if (!started) {
            Injector injector = (Injector)servletContext.getAttribute(Injector.class.getName());

            injector.getInstance(Key.get(GoogleApiService.class)).start();
            injector.getInstance(Key.get(DriveService.class)).start();
            injector.getInstance(Key.get(CalendarService.class)).start();
            injector.getInstance(Key.get(SheetService.class)).start();
            started = true;
        }
    }

    @Override
    public void stop() {
        started = false;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}
