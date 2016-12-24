package tech.oleks.pmtalk.guice;

import com.google.inject.servlet.ServletModule;
import tech.oleks.pmtalk.PmTalkServlet;
import tech.oleks.pmtalk.web.AuthorizationFilter;
import tech.oleks.pmtalk.web.GoogleApiAuthorizationServlet;
import tech.oleks.pmtalk.web.Oauth2CallbackServlet;
import tech.oleks.pmtalk.web.StartFilter;

/**
 * Created by alexm on 12/10/16.
 */
public class MyServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
        super.configureServlets();
        serve("/").with(PmTalkServlet.class);
        serve("/oauth2callback").with(Oauth2CallbackServlet.class);
        serve("/auth").with(GoogleApiAuthorizationServlet.class);

        filter("/").through(AuthorizationFilter.class);
        filter("/").through(StartFilter.class);
    }
}
