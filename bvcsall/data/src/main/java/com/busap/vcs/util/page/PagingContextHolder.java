package com.busap.vcs.util.page;


/**
 * 上下文管理器
 *
 * @author huoshanwei
 */
public class PagingContextHolder {

    private static final ThreadLocal<Page> localPage = new ThreadLocal<Page>();

    public static void setPage(Page page) {
        localPage.set(page);
    }

    public static Page getPage() {
        return localPage.get();
    }

    public static void removePage() {
        localPage.remove();
    }

}
