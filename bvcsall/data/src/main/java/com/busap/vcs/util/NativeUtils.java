package com.busap.vcs.util;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.OS;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA and by
 * User: djyin
 * DateTime: 11/21/13 10:16 AM
 * <p/>
 * 利用commons-exec执行一些系统命令
 */
public class NativeUtils {

    /**
     * 执行命令
     *
     * @param exec
     *         the exec
     * @param args
     *         the args 传递给脚本的参数
     * @param quotedArgs
     *         the quoted args 传递给脚本的参数,需要用引号括起来
     * @return the int
     * @throws java.io.IOException
     *         the iO exception
     */

    public static int exec(String exec, Map<String, Object> args, Map<String, Object> quotedArgs) throws IOException {
        CommandLine commandLine = null;
        if (OS.isFamilyUnix()) { // linux or unix
            commandLine = new CommandLine("sh");
        } else if (OS.isFamilyWindows()) { // windows
            commandLine = new CommandLine("cmd /c");
        } else {
            throw new IOException("unsupported system : " + System.getProperty("os.name"));
        }
        for (Map.Entry<String, Object> arg : args.entrySet()) {
            if (quotedArgs != null && quotedArgs.containsKey(arg.getKey())) { //重复
                continue;
            }
            commandLine.addArgument("${" + arg.getKey() + "}");
        }

        commandLine.addArgument(exec);

        for (Map.Entry<String, Object> arg : quotedArgs.entrySet()) {
            commandLine.addArgument("${" + arg.getKey() + "}", true);
        }
        args.putAll(args);
        commandLine.setSubstitutionMap(args);
        DefaultExecutor executor = new DefaultExecutor();
        return executor.execute(commandLine);


    }

    /**
     * Exec int. 执行一个可执行的程序
     *
     * @param exec
     *         the exec
     * @param args
     *         the args 传递给脚本的参数
     * @param quotedArgs
     *         the quoted args 传递给脚本的参数,需要用引号括起来
     * @return the int
     * @throws java.io.IOException
     *         the iO exception
     */
    public static int exec(File exec, Map<String, Object> args, Map<String, Object> quotedArgs) throws IOException {
        //检查文件夹是否存在
        if (!exec.exists()) {
            throw new IOException("exec file : " + exec.getPath() + " do not exists.");
        }
        if (!exec.canRead()) {
            throw new IOException("exec file : " + exec.getPath() + " cannot be read.");
        }
        if (!exec.canExecute()) {
            throw new IOException("exec file : " + exec.getPath() + " cannot be executed, please check privilege and file permission.");
        }

        CommandLine commandLine = null;
        if (OS.isFamilyUnix()) { // linux or unix
            commandLine = new CommandLine("sh");
        } else if (OS.isFamilyWindows()) { // windows
            commandLine = new CommandLine("cmd /c");
        } else {
            throw new IOException("unsupported system : " + System.getProperty("os.name"));
        }
        for (Map.Entry<String, Object> arg : args.entrySet()) {
            if (quotedArgs != null && quotedArgs.containsKey(arg.getKey())) { //重复
                continue;
            }
            commandLine.addArgument("${" + arg.getKey() + "}");
        }

        commandLine.addArgument(exec.getPath());

        for (Map.Entry<String, Object> arg : quotedArgs.entrySet()) {
            commandLine.addArgument("${" + arg.getKey() + "}", true);
        }
        args.putAll(args);
        commandLine.setSubstitutionMap(args);
        DefaultExecutor executor = new DefaultExecutor();
        return executor.execute(commandLine);
    }

    /**
     * 执行脚本
     *
     * @param sh
     *         the sh 脚本的文件
     * @param args
     *         the args 传递给脚本的参数
     * @param quotedArgs
     *         the quoted args 传递给脚本的参数,需要用引号括起来
     * @return the int 脚本的返回值
     * @throws java.io.IOException
     *         the iO exception
     */
    public static int sh(File sh, Map<String, Object> args, Map<String, Object> quotedArgs) throws IOException {
        //检查文件夹是否存在
        if (!sh.exists()) {
            throw new IOException("sh file : " + sh.getPath() + " do not exists.");
        }
        if (!sh.canRead()) {
            throw new IOException("sh file : " + sh.getPath() + " cannot be read.");
        }
        if (!sh.canExecute()) {
            throw new IOException("exec file : " + sh.getPath() + " cannot be executed, please check privilege and file permission.");
        }

        CommandLine commandLine = null;
        if (OS.isFamilyUnix()) { // linux or unix
            commandLine = new CommandLine("sh");
        } else if (OS.isFamilyWindows()) { // windows
            commandLine = new CommandLine("cmd /c");
        } else {
            throw new IOException("unsupported system : " + System.getProperty("os.name"));
        }

        commandLine.addArgument(sh.getPath());

        for (Map.Entry<String, Object> arg : args.entrySet()) {
            if (quotedArgs != null && quotedArgs.containsKey(arg.getKey())) { //重复
                continue;
            }
            commandLine.addArgument("${" + arg.getKey() + "}");
        }
        for (Map.Entry<String, Object> arg : quotedArgs.entrySet()) {
            commandLine.addArgument("${" + arg.getKey() + "}", true);
        }
        args.putAll(args);
        commandLine.setSubstitutionMap(args);
        DefaultExecutor executor = new DefaultExecutor();
        return executor.execute(commandLine);

    }

    /**
     * Nativecp void.
     * 利用系统原生命令进行文件拷贝
     *
     * @param srcFile
     *         the src
     * @param targetFile
     *         the target
     * @param force
     *         the force mkdir 当目标所在的文件夹不存在时,是否强制创建目标文件夹
     * @throws java.io.IOException
     *         the iO exception
     */
    public static void cp(File srcFile, File targetFile, boolean force) throws IOException {
        check(srcFile, targetFile, force);


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("srcFile", srcFile);
        map.put("targetFile", targetFile);
        CommandLine commandLine = null;
        if (OS.isFamilyUnix()) { // linux or unix
            commandLine = new CommandLine("\\cp");//new CommandLine("sh");
            commandLine.addArgument("-rf");
            commandLine.addArgument("${srcFile}", true);
            commandLine.addArgument("${targetFile}", true);
            commandLine.setSubstitutionMap(map);


        } else if (OS.isFamilyWindows()) { // windows
            commandLine = new CommandLine("cmd /c xcopy");
            commandLine.addArgument("/Y");
            commandLine.addArgument("/E");
            commandLine.addArgument("${srcFile}", true);
            commandLine.addArgument("${targetFile}", true);
            commandLine.setSubstitutionMap(map);

        } else {
            throw new IOException("unsupported system : " + System.getProperty("os.name"));

        }
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.execute(commandLine);


    }


    /**
     * 利用系统原生命令进行文件移动
     *
     * @param srcFile
     *         the src file
     * @param targetFile
     *         the target file
     * @param force
     *         the force
     * @throws java.io.IOException
     *         the iO exception
     */
    public static void mv(File srcFile, File targetFile, boolean force) throws IOException {
        //检查文件夹是否存在
        check(srcFile, targetFile, force);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("srcFile", srcFile);
        map.put("targetFile", targetFile);
        CommandLine commandLine = null;
        if (OS.isFamilyUnix()) { // linux or unix  using sh
            commandLine = new CommandLine("mv");//new CommandLine("sh");
            commandLine.addArgument("-f");
            commandLine.addArgument("${srcFile}", true);
            commandLine.addArgument("${targetFile}", true);
            commandLine.setSubstitutionMap(map);
        } else if (OS.isFamilyWindows()) { // windows using cmd /c
            commandLine = new CommandLine("cmd");
            commandLine.addArgument("/c");
            commandLine.addArgument("move");
            commandLine.addArgument("/Y");
            commandLine.addArgument("${srcFile}", true);
            commandLine.addArgument("${targetFile}", true);
            commandLine.setSubstitutionMap(map);

        } else {
            throw new IOException("unsupported system : " + System.getProperty("os.name"));

        }
        DefaultExecutor executor = new DefaultExecutor();
        executor.setExitValue(0);
        executor.execute(commandLine);

    }

    private static void check(File srcFile, File targetFile, boolean force) throws IOException {
        //检查文件夹是否存在
        if (!srcFile.exists()) {
            throw new IOException("src file : " + srcFile.getPath() + " do not exists.");
        }
        if (!srcFile.canRead()) {
            throw new IOException("src file : " + srcFile.getPath() + " cannot be read.");
        }

        //target是文件夹的情况

        File parentFile = targetFile.getParentFile();
        if (!parentFile.exists()) {
            //不存在
            if (force) {// 强制创建
                FileUtils.forceMkdir(parentFile);
            } else {//异常
                throw new IOException("target file : " + targetFile.getPath() + " do not exists.");
            }
        }

        if (!parentFile.isDirectory()) {
            throw new IOException("target file's parent : " + targetFile.getPath() + " is not a directory. ");
        }

    }
}
