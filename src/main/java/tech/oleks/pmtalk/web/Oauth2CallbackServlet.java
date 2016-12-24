package tech.oleks.pmtalk.web;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeCallbackServlet;
import com.google.api.client.http.GenericUrl;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import tech.oleks.pmtalk.service.Configuration;
import tech.oleks.pmtalk.service.GoogleApiService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by alexm on 12/18/16.
 */
@Singleton
public class Oauth2CallbackServlet extends AbstractAuthorizationCodeCallbackServlet {

    @Inject
    GoogleApiService apiService;

    @Inject
    Configuration config;


    @Override
    protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential)
            throws ServletException, IOException {
        apiService.setCredential(credential);
        System.out.println(credential.getExpirationTimeMilliseconds());
        System.out.println(credential.getExpiresInSeconds());
        System.out.println("Successful Authorization!");
        resp.sendRedirect("/");
    }

    @Override
    protected void onError(
            HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
            throws ServletException, IOException {
        throw new IOException(errorResponse.getErrorDescription());
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