package com.busap.vcs.hlsmedia.http.transcode;

/**
 * Created by djyin on 8/6/2014.
 */
public interface ShellableExecutorResultHandler {

    /**
     * @param exitValue
     * @param out
     * @param err
     */
    public void onProcessComplete(final int exitValue, String out, String err);

    /**
     * @param e
     * @param out
     * @param err
     */
    public void onProcessFailed(final Throwable e, String out, String err);

    /**
     * @param e
     * @param out
     * @param err
     */
    //public void onException(final Throwable e, String out, String err);
}
