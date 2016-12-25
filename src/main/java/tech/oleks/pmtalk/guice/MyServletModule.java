package tech.oleks.pmtalk.guice;

import com.google.inject.servlet.ServletModule;
import tech.oleks.pmtalk.PmTalkServlet;
import tech.oleks.pmtalk.web.*;

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
        serve("/worker").with(WorkerServlet.class);

        filter("/", "/worker").through(AuthorizationFilter.class);
        filter("/", "/worker").through(StartFilter.class);
    }
}
