package com.busap.vcs.operate.utils.impl;


import com.busap.vcs.operate.utils.Env;
import com.busap.vcs.operate.utils.Pair;
import com.busap.vcs.operate.utils.ServerMapping;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class ServerMappingImpl
        implements ServerMapping {
    private final Map<Pair<Env, String>, List<String>> configuredVirtualHosts = new HashMap();

    private final Set<String> knownVirtualHosts = new HashSet();

    protected void addConfig(Env env, String virtualHost, String realHost) {
        Pair key = new Pair(env, virtualHost);
        List values = (List) this.configuredVirtualHosts.get(key);
        if (values == null) {
            values = new ArrayList();
        }
        values.add(realHost);
        this.configuredVirtualHosts.put(key, values);
        this.knownVirtualHosts.add(virtualHost);
    }

    public boolean equals(Object o) {
        if ((o instanceof ServerMappingImpl)) {
            ServerMappingImpl smi = (ServerMappingImpl) o;
            return (this.configuredVirtualHosts.equals(smi.configuredVirtualHosts)) && (this.knownVirtualHosts.equals(smi.knownVirtualHosts));
        }

        return false;
    }

    public List<String> getUrls(String url) {
        for (String virtualHost : this.knownVirtualHosts) {
            if (StringUtils.contains(url, virtualHost)) {
                List result = getSystemPropertyUrls(url, virtualHost);
                if (result != null) {
                    return result;
                }
                result = getConfiguredUrls(url, virtualHost, Env.CURRENT);
                if (result != null) {
                    return result;
                }
                throw new IllegalArgumentException("The configuration for virtual host [" + virtualHost + "] is not found");
            }
        }
        return Arrays.asList(new String[]{url});
    }

    private List<String> getSystemPropertyUrls(String url, String virtualHost) {
        String realHost = System.getProperty(virtualHost);
        if (StringUtils.isBlank(realHost)) {
            return null;
        }
        String mappedUrl = StringUtils.replace(url, virtualHost, realHost);
        return Arrays.asList(new String[]{mappedUrl});
    }

    private List<String> getConfiguredUrls(String url, String virtualHost, Env env) {
        List<String> realHosts = getConfiguredHost(virtualHost, env);
        if (realHosts == null) {
            return null;
        }
        List mappedUrls = new ArrayList();
        for (String realHost : realHosts) {
            mappedUrls.add(StringUtils.replace(url, virtualHost, realHost));
        }
        return mappedUrls;
    }

    private List<String> getConfiguredHost(String virtualHost, Env env) {
        List realHosts = (List) this.configuredVirtualHosts.get(new Pair(env, virtualHost));
        if ((realHosts == null) && (env != null)) {
            realHosts = (List) this.configuredVirtualHosts.get(new Pair(env.getRoot(), virtualHost));
        }
        return realHosts;
    }
}
