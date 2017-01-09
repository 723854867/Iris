package com.busap.vcs.operate.utils;


import java.io.IOException;

public abstract interface ServerMappingFactory {
    public abstract ServerMapping getServerMapping()
            throws IOException;
}