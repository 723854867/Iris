package com.busap.vcs.operate.utils;

import com.busap.vcs.operate.utils.impl.UrlServerMappingFactory;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public final class ServerMapper {
    private final Logger logger = LoggerFactory.getLogger(ServerMapper.class);

    private static final ServerMapper INSTANCE = new ServerMapper();

    private final ServerMappingFactory serverMappingFactory = new UrlServerMappingFactory();
    private volatile ServerMapping serverMapping;
    private final Set<Refreshable> refreshAbles = Collections.synchronizedSet(new HashSet());

    public static ServerMapper getInstance() {
        return INSTANCE;
    }

    public String getUrl(String url) {
        return getUrl(url, null);
    }

    public String getUrl(String url, Refreshable refreshable) {
        return (String) getUrls(url, refreshable).get(0);
    }

    public List<String> getUrls(String url) {
        return getUrls(url, null);
    }

    public List<String> getUrls(String url, Refreshable refreshable) {
        if (this.serverMapping == null) {
            init();
        }
        if (refreshable != null) {
            this.refreshAbles.add(refreshable);
        }
        List<String> mappedUrls = this.serverMapping.getUrls(url);
        if (this.logger.isInfoEnabled()) {
            for (String mappedUrl : mappedUrls) {
                this.logger.info("server_mapping: " + url + " => " + mappedUrl);
            }
        }
        return mappedUrls;
    }

    private synchronized void init() throws RuntimeException {
        if (this.serverMapping == null) {
            this.serverMapping = getServerMappingWithRetry(3);
            installTimerTask();
        }
    }

    private void installTimerTask() {
        Date now = new Date();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        long initialDelay = TimeUnit.MINUTES.toMillis(1L) - DateUtils.getFragmentInMilliseconds(now, 12);
        long period = TimeUnit.MINUTES.toMillis(1L);
        executor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                ServerMapper.this.logger.debug("start to refresh.");
                try {
                    ServerMapper.this.refresh();
                } catch (Exception e) {
                    ServerMapper.this.logger.error("failed to refresh.");
                }
            }
        }
                , initialDelay, period, TimeUnit.MILLISECONDS);
    }

    private synchronized void refresh() {
        ServerMapping newServerMapping = getServerMappingWithRetry(3);
        if (!newServerMapping.equals(this.serverMapping)) {
            this.serverMapping = newServerMapping;
            this.logger.info("The server mapping is changed. notify all listener now.");
            notifyListeners();
        }
    }

    private void notifyListeners() {
        try {
            for (Refreshable refreshable : this.refreshAbles)
                refreshable.refresh();
        } catch (Exception e) {
            this.logger.error("Failed to make a listener refresh", e);
        }
    }

    private ServerMapping getServerMappingWithRetry(int retries) {
        for (int i = 0; i < retries; i++) {
            try {
                return this.serverMappingFactory.getServerMapping();
            } catch (IOException e) {
                this.logger.error("Failed to load the server mapping", e);
            }
        }
        throw new RuntimeException("Cannot load the server mapping");
    }
}