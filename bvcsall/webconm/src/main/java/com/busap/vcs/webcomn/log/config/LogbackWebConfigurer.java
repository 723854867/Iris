package com.busap.vcs.webcomn.log.config;

import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.SystemPropertyUtils;

import javax.servlet.ServletContext;
import java.io.FileNotFoundException;


public class LogbackWebConfigurer {

    /** Parameter specifying the location of the logback config file */
    public static final String CONFIG_LOCATION_PARAM = "logbackConfigLocation";

    /**
     * Initialize logback, including setting the web app root system property.
     *
     * @param servletContext
     *         the current ServletContext
     * @param location
     *         parameter read from web.xml
     * @see WebUtils#setWebAppRootSystemProperty
     */
    public static String initLogging(ServletContext servletContext,
                                     String location) {
        if (location != null) {
            // Perform actual logback initialization; else rely on logback's
            // default
            // initialization.
            try {
                // Return a URL (e.g. "classpath:" or "file:") as-is;
                // consider a plain file path as relative to the web application
                // root directory.
                if (!ResourceUtils.isUrl(location)) {
                    // Resolve system property placeholders before resolving
                    // real path.
                    location = SystemPropertyUtils
                            .resolvePlaceholders(location);
                    location = getRealPath(servletContext, location);
                }

                // Write log message to server log.
                servletContext.log("Initializing logback from [" + location
                        + "]");

                // Initialize logback's.
                AbstractLogbackCofigurer.initLogging(location);
                return location;
            } catch (FileNotFoundException ex) {
                throw new IllegalArgumentException(
                        "Invalid 'logbackConfigLocation' parameter: "
                                + ex.getMessage());
            }
        }
        return null;
    }

    /**
     * Initialize logback, including setting the web app root system property.
     *
     * @param servletContext
     *         the current ServletContext
     * @see WebUtils#setWebAppRootSystemProperty
     */
    public static String initLogging(ServletContext servletContext) {
        // Only perform custom logback initialization in case of a config file.
        String location = servletContext
                .getInitParameter(CONFIG_LOCATION_PARAM);
        return initLogging(servletContext, location);
    }

    /**
     * Shut down logback, properly releasing all file locks and resetting the web
     * app root system property.
     *
     * @param servletContext
     *         the current ServletContext
     * @see WebUtils#removeWebAppRootSystemProperty
     */
    public static void shutdownLogging(ServletContext servletContext) {
        servletContext.log("Shutting down logback");

        AbstractLogbackCofigurer.shutdownLogging();

    }

    /**
     * Return the real path of the given path within the web application,
     * as provided by the servlet container.
     * <p>Prepends a slash if the path does not already start with a slash,
     * and throws a FileNotFoundException if the path cannot be resolved to
     * a resource (in contrast to ServletContext's <code>getRealPath</code>,
     * which returns null).
     *
     * @param servletContext
     *         the servlet context of the web application
     * @param path
     *         the path within the web application
     * @return the corresponding real path
     * @throws FileNotFoundException
     *         if the path cannot be resolved to a resource
     * @see javax.servlet.ServletContext#getRealPath
     */
    public static String getRealPath(ServletContext servletContext, String path) throws FileNotFoundException {
        Assert.notNull(servletContext, "ServletContext must not be null");
        // Interpret location as relative to the web application root directory.
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String realPath = servletContext.getRealPath(path);
        if (realPath == null) {
            throw new FileNotFoundException(
                    "ServletContext resource [" + path + "] cannot be resolved to absolute file path - " +
                            "web application archive not expanded?");
        }
        return realPath;
    }
}
