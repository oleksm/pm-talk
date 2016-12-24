package tech.oleks.pmtalk.guice;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.servlet.GuiceServletContextListener;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import tech.oleks.pmtalk.service.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.IOException;

/**
 * Created by alexm on 12/9/16.
 */
public class MyGuiceServletContextListener extends GuiceServletContextListener {

    @Override protected Injector getInjector() {
        return Guice.createInjector(new MyServletModule());
    }

}
