package com.busap.vcs.webcomn.log.config;


import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.FileNotFoundException;
import java.net.URL;

public class AbstractLogbackCofigurer {

    /**
     * Initialize log4j from the given file location, with no config file refreshing.
     * Assumes an XML file in case of a ".xml" file extension, and a properties file
     * otherwise.
     *
     * @param location
     *         the location of the config file: either a "classpath:" location
     *         (e.g. "classpath:myLog4j.properties"), an absolute file URL
     *         (e.g. "file:C:/log4j.properties), or a plain absolute path in the file system
     *         (e.g. "C:/log4j.properties")
     * @throws FileNotFoundException
     *         if the location specifies an invalid file path
     */
    public static void initLogging(String location) throws FileNotFoundException {
        String resolvedLocation = SystemPropertyUtils.resolvePlaceholders(location);
        URL url = ResourceUtils.getURL(resolvedLocation);
        // assume SLF4J is bound to logback in the current environment
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(context);
            // Call context.reset() to clear any previous configuration, e.g. default
            // configuration. For multi-step configuration, omit calling context.reset().
            context.reset();
            configurator.doConfigure(url);
        } catch (JoranException je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);

    }

    /**
     * Shut down log4j, properly releasing all file locks.
     * <p>This isn't strictly necessary, but recommended for shutting down
     * log4j in a scenario where the host VM stays alive (for example, when
     * shutting down an application in a J2EE environment).
     */
    public static void shutdownLogging() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.stop();
    }
}
