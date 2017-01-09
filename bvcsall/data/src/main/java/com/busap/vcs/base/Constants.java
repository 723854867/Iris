package com.busap.vcs.base;

/**
 * Created by
 * User: djyin
 * Date: 12/6/13
 * Time: 9:56 AM
 * <p/>
 * 记录一些多个包协同使用的常量
 */
public class Constants {
    /**
     * The constant ENTITY_CONSTRAINT_VIOLATIONS.
     * request attribute 中存放数据格式校验的错误集合时,使用的key
     */
    public static final String ENTITY_CONSTRAINT_VIOLATIONS = "baseEntityConstraintViolations";
    /**
     * The constant ENTITY_CUSTUM_VIOLATIONS. custom
     */
    public static final String ENTITY_CUSTOM_VIOLATIONS = "baseEntityCustomViolations";

    /**
     * The constant DATE_PATTERNS.
     * 系统中可能用到的时间格式
     */
    public static final String[] DATE_PATTERNS = {"yyyy-MM-dd", "yyyyMMdd", "yyyy/MM/dd",
            "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss", "yyyy/MM/dd HH:mm:ss", "HH:mm:ss", "HHmmss"};

    /**
     * 用户vip等级状态，暂时支持（普通 0 默认，蓝V 1，黄V 2，绿V 3）
     */
    public static enum USER_VIP_STAT {
        NORMAL(0),
        BLUE(1),
        YELLOW(2),
        GREEN(3);

        private int stat;

        USER_VIP_STAT(int stat) {
            this.stat = stat;
        }

        public int getStat() {
            return stat;
        }

        public void setStat(int stat) {
            this.stat = stat;
        }
    }

    public static final String TASK_KEY = "TASK_CACHE_TYPE_";

    public static final String ALL_TASK_KEY = "ALL_TASK_CACHE";

    public static final String IMAGE_FORMAT = ".jpg.png.gif.jpeg.icon";
}
