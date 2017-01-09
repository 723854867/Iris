package com.busap.vcs.hlsmedia.http.transcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by djyin on 8/6/2014.
 */
public class ShellableExecutor {

    private static final Logger log = LoggerFactory.getLogger(ShellableExecutor.class);

    public void executor(Shellable shellable, ShellableExecutorResultHandler resultHandler,
                         String argsTemplate, Object... args) throws IOException {
        // 通过fixed

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(2048);
        final ByteArrayOutputStream errsStream = new ByteArrayOutputStream(2048);
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errsStream);


        final CommandLine commandLine = new CommandLine(shellable.toString());
        if (argsTemplate != null) {
            String arguments = String.format(argsTemplate, args);
            commandLine.addArguments(arguments);
        }
        log.info(String.format("[shell command]: %s",commandLine.toString()) );
        // create the executor and consider the exitValue '1' as success
        final Executor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.setStreamHandler(streamHandler);
        ResultHandler handler = new ResultHandler(executor, resultHandler, outputStream, errsStream);
        executor.execute(commandLine, handler);

    }

    private static class ResultHandler extends DefaultExecuteResultHandler {

        private ShellableExecutorResultHandler transcodeResultHandler;
        final ByteArrayOutputStream outputStream;
        final ByteArrayOutputStream errsStream;
        final Executor executor;

        public ResultHandler(final Executor executor,
                             final ShellableExecutorResultHandler transcodeResultHandler,
                             final ByteArrayOutputStream outputStream,
                             final ByteArrayOutputStream errsStream) {
            super();
            this.executor = executor;
            this.transcodeResultHandler = transcodeResultHandler;
            this.outputStream = outputStream;
            this.errsStream = errsStream;

        }

        @Override
        public void onProcessComplete(final int exitValue) {
            super.onProcessComplete(exitValue);
            ExecuteWatchdog dog =
                    executor.getWatchdog();
            if (dog != null) {
                dog.destroyProcess();
            }
            String encoding = System.getProperty("sun.jnu.encoding");
            try {
                String error = errsStream.toString(encoding);
                String out = outputStream.toString(encoding);
                transcodeResultHandler.onProcessComplete(exitValue, out, error);
            } catch (UnsupportedEncodingException e) {
                // ignored
            } finally {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(errsStream);
            }

        }

        @Override
        public void onProcessFailed(final ExecuteException e) {
            super.onProcessFailed(e);
            ExecuteWatchdog dog =
                    executor.getWatchdog();
            if (dog != null) {
                dog.destroyProcess();
            }
            String encoding = System.getProperty("sun.jnu.encoding");
            try {
                String error = errsStream.toString(encoding);
                String out = outputStream.toString(encoding);
                transcodeResultHandler.onProcessFailed(e, out, error);

            } catch (UnsupportedEncodingException ue) {
                // ignored
            } finally {
                IOUtils.closeQuietly(outputStream);
                IOUtils.closeQuietly(errsStream);
            }
        }
    }
}
