package tech.oleks.pmtalk.web;

import com.google.api.client.auth.oauth2.*;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.http.GenericUrl;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tech.oleks.pmtalk.service.Configuration;
import tech.oleks.pmtalk.service.GoogleApiService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by alexm on 12/18/16.
 */
@Singleton
public class GoogleApiAuthorizationServlet extends AbstractAuthorizationCodeServlet {

    @Inject
    GoogleApiService apiService;

    @Inject
    Configuration config;

    final Logger log = Logger.getLogger(getClass().getName());


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.info("Credential Loaded from storage");
        Credential credential = getCredential();

        if (credential.getExpiresInSeconds() < 1) {
            log.info("Credential has expired, refreshing token...");
            log.info("Credential token refreshed: " + credential.refreshToken());
        }

        apiService.setCredential(credential);
        response.sendRedirect("/");
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
        GenericUrl url = new GenericUrl(req.getRequestURL().toString());
        url.setRawPath("/oauth2callback");
        return url.build();
    }

    @Override
    protected AuthorizationCodeFlow initializeFlow() throws IOException {
        return apiService.initializeFlow();
    }

    @Override
    protected String getUserId(HttpServletRequest req) throws ServletException, IOException {
        return config.getDefaultUserId();
    }
}
