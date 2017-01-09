package com.busap.vcs.operate.utils;

import java.util.List;

/**
 *  Created by yangxinyu on 14-4-3.
 */
public abstract interface ServerMapping {
    public abstract List<String> getUrls(String paramString);
}
