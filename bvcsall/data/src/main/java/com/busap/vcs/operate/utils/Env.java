package com.busap.vcs.operate.utils;

import org.apache.commons.lang.StringUtils;

public enum Env {
    TEST(null),
    TEST_REAL(null),
    TEST_FOR_TEST(null),
    PROD(null),
    PROD_LIVE(PROD),
    STAGING(PROD),
    MIRROR(null),
    STAGING_NEW(null),
    EXT_PROD_SH(null),
    EXT_PROD_HZ(null),
    EXT_TWS_NC(null);

    private final Env root;
    public static final Env CURRENT;

    private Env(Env root) {
        this.root = (root == null ? this : root);
    }

    public Env getRoot() {
        return this.root;
    }

    public static void main(String[] args) {
        Env c = PROD_LIVE;
        c = PROD;

        if (CURRENT.toString().startsWith("TEST")) ;
        System.out.println(c.toString());

        for (Env env : values())
            System.out.println(env.toString() + ":");
    }

    static {
        Env current = null;

        if (StringUtils.equalsIgnoreCase("true", System.getProperty("NIUX_TEST_DS"))) {
            current = TEST;
        } else if (StringUtils.equalsIgnoreCase("TEST", System.getProperty("NIUX_ENV"))) {
            current = TEST;
        } else if (StringUtils.equalsIgnoreCase("TEST_REAL", System.getProperty("NIUX_ENV"))) {
            current = TEST_REAL;
        } else if (StringUtils.equalsIgnoreCase("TEST_FOR_TEST", System.getProperty("NIUX_ENV"))) {
            current = TEST_FOR_TEST;
        } else if (StringUtils.equalsIgnoreCase("PROD", System.getProperty("NIUX_ENV"))) {
            current = PROD;
        } else if (StringUtils.equalsIgnoreCase("PROD_LIVE", System.getProperty("NIUX_ENV"))) {
            current = PROD_LIVE;
        } else if (StringUtils.equalsIgnoreCase("STAGING", System.getProperty("NIUX_ENV"))) {
            current = STAGING;
        } else if (StringUtils.equalsIgnoreCase("STAGING_NEW", System.getProperty("NIUX_ENV"))) {
            current = STAGING_NEW;
        } else if (StringUtils.equalsIgnoreCase("MIRROR", System.getProperty("NIUX_ENV"))) {
            current = MIRROR;
        } else if ((System.getProperty("NIUX_ENV") != null) && (System.getProperty("NIUX_ENV").toLowerCase().startsWith("ext_"))) {
            current = valueOf(System.getProperty("NIUX_ENV").toUpperCase());
        }
        if (current == null) {
            current = STAGING;
        }
        CURRENT = current;

    }
}