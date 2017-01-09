package com.busap.vcs.operate.utils.impl;



import com.busap.vcs.operate.utils.Env;
import com.busap.vcs.operate.utils.ServerMappingFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlServerMappingFactory
        implements ServerMappingFactory {
    private static final int VERSION = 1;
    private static final String CONF_LOCATION = "http://nuomiconf.d.xiaonei.com/servers.conf?version=1";

    public ServerMappingImpl getServerMapping()
            throws IOException {
        URL url = new URL("http://nuomiconf.d.xiaonei.com/servers.conf?version=1");
        List inputs = getLinesFromUrl(url);
        return parseConf(inputs);
    }

    public List<String> getLinesFromUrl(URL url) throws IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            return IOUtils.readLines(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public ServerMappingImpl parseConf(List<String> lines) {
        Pattern p = Pattern.compile("^(\\S+)\\s*\\+=\\s*(\\S+)$");
        ServerMappingImpl result = new ServerMappingImpl();
        Env currentEnv = null;
        for (String l : lines) {
            String line = StringUtils.trimToEmpty(l);
            if ((!StringUtils.isBlank(line)) &&
                    (!line.startsWith("#"))) {
                for (Env env : Env.values()) {
                    if (line.equals(env.toString() + ":")) {
                        currentEnv = env;
                        break;
                    }
                }
                if (currentEnv != null) {
                    Matcher m = p.matcher(line);
                    if (m.matches())
                        result.addConfig(currentEnv, m.group(1), m.group(2));
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String line = "TEST_REAL:";
        line = StringUtils.trimToEmpty(line);
        Env currentEnv = null;
        for (Env env : Env.values()) {
            if (line.equals(env.toString() + ":")) {
                currentEnv = env;
                break;
            }
        }
        System.out.println(currentEnv);
    }
}