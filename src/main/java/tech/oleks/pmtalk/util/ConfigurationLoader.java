package tech.oleks.pmtalk.util;

import tech.oleks.pmtalk.service.Configuration;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by alexm on 12/23/16.
 */
public class ConfigurationLoader {
    public static void load(Configuration config, String f) throws IOException {
        Properties props = new Properties();
        props.load(ConfigurationLoader.class.getResourceAsStream(f));

        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(config.getClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            String value = props.getProperty(propertyDescriptor.getName());
            if (value != null && propertyDescriptor.getWriteMethod() != null) {
                try {
                    propertyDescriptor.getWriteMethod().invoke(config, value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
