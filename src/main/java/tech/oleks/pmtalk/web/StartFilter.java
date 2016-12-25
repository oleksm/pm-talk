package tech.oleks.pmtalk.web;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Singleton;
import tech.oleks.pmtalk.service.ApplicationService;
import tech.oleks.pmtalk.service.Configuration;
import tech.oleks.pmtalk.util.ConfigurationLoader;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by alexm on 12/16/16.
 */
@Singleton
public class StartFilter implements Filter {

    @Inject ApplicationService application;
    @Inject Configuration config;
    Object lock = new Object();

    final Logger log = Logger.getLogger(getClass().getName());


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            ConfigurationLoader.load(config, "/configuration.properties");
        } catch (IOException e) {
            throw new ServletException(e);
        }
        filterConfig.getServletContext().setAttribute("config", config);
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!application.isStarted()) {
            synchronized (lock) {
                log.info("Initializing Application...");
                if (!application.isStarted()) {
                    application.setServletContext(((HttpServletRequest)request).getSession().getServletContext());
                    application.start();
                    log.info("Initializing Application... DONE.");
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
