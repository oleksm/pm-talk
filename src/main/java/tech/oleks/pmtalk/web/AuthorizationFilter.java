package tech.oleks.pmtalk.web;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import tech.oleks.pmtalk.service.GoogleApiService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by alexm on 12/18/16.
 */
@Singleton
public class AuthorizationFilter implements Filter {

    @Inject
    GoogleApiService apiService;

    final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (apiService.getCredential() == null) {
            log.info("Authorization is required");
            ((HttpServletResponse)servletResponse).sendRedirect("/auth");
        }
        else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
