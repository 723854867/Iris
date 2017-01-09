package com.busap.vcs.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 辅助 org.apache.commons.io.FileUtils
 * 实现 org.apache.commons.io.FileUtils 中一些没有的方法
 * Created by
 * User: djyin
 * Date: 12/9/13
 * Time: 9:32 AM
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * The constant FILE_SEPARATOR.
     */
    public static final char FILE_SEPARATOR = '/';
    /**
     * The constant FILE_SEPARATOR_STR.
     */
    public static final String FILE_SEPARATOR_STR = Character.toString('/');

    /**
     * 无用的函数,javadoc才重要
     */
    public static void listExample() {

    }

    /**
     * 删除空目录.
     *
     * @param delFrom
     *         从xxx子目录,或者子文件,此文件将被删除
     * @param delTo
     *         删除到to父目录为止
     */
    public static void deleteEmpty(File delFrom, File delTo) {
        // 递归判断文件夹是否为空,删除空余文件夹
        File parent = delFrom.getParentFile();

        while (true) {
            File needDelete = parent;
            parent = parent.getParentFile();
            if (delTo.equals(needDelete)) {
                break;
            }
            if (needDelete.list() != null
                    && needDelete.list().length < 1) {
                org.apache.commons.io.FileUtils.deleteQuietly(needDelete);
            }
        }
    }

    /**
     * 根据已经存在的文件,格式化成文件或者目录的路径
     *
     * @param filePath
     *         the file path
     * @return the string
     */
    public static String formatExistFile(String filePath) {
        filePath = FilenameUtils.normalize(filePath, true);
        File f = new File(filePath);
        if (f.exists()) {
            if (f.isDirectory() && !filePath.endsWith("/")) {

                filePath = filePath + "/";

            } else if (f.isFile() && filePath.endsWith("/")) {
                filePath = filePath.substring(0, filePath.length() - 1);
            }
        }
        return filePath;

    }

    /**
     * 格式化成目录的路径
     *
     * @param directory
     *         the directory
     * @return the string linux风格的目录名,并以'/'结尾
     */
    public static String formatDirectory(String directory) {
        directory = FilenameUtils.normalize(directory, true);
        if (directory == null) {
            return null;
        }
        if (!directory.endsWith("/")) {
            directory = directory + "/";
        }
        return directory;
    }

    /**
     * 格式化成文件的路径
     *
     * @param filePath
     *         the directory
     * @return the string linux风格的路径名,无'/'结尾
     */
    public static String formatFile(String filePath) {
        filePath = FilenameUtils.normalize(filePath, true);
        if (filePath == null) {
            return null;
        }
        if (filePath.endsWith("/")) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        return filePath;
    }

    /**
     * 路径减目录,比如/FTP/upload/20130403-1808/动物世界/动物世界第344期 - /FTP/upload
     * = 20130403-1808/动物世界/动物世界第344期
     *
     * @param filePath
     *         the path
     * @param subed
     *         the subed
     * @return the string
     */
    public static String sub(String filePath, String subed) {
        filePath = FilenameUtils.normalize(filePath, true);
        subed = formatDirectory(subed);
        return filePath.substring(filePath.lastIndexOf(subed)
                + subed.length());
    }

    /**
     * 列出一个目录下所有子目录的名字(注意不是全路径),不递归
     *
     * @param filePath
     *         the file path
     * @return the string [ ]
     */
    public static String[] listSubDirectorys(String filePath) {
        File dir = new File(filePath);
        String[] files = dir.list(DirectoryFileFilter.INSTANCE);
        return files;
    }

    /**
     * 列出一个目录下所有子目录的名字(注意不是全路径),不递归
     *
     * @param filePath
     *         the file path
     * @param wildcard
     *         the wildcard 通配符,比如"*commercial*"
     * @return the string [ ]
     */
    public static String[] listSubDirectorys(String filePath, String wildcard) {
        File dir = new File(filePath);

        String[] files = dir.list(new AndFileFilter(DirectoryFileFilter.INSTANCE, new WildcardFileFilter(wildcard)));
        return files;
    }

    /**
     * 列出一个目录下所有子文件的名字(注意不是全路径),不递归
     *
     * @param filePath
     *         the file path
     * @return the string [ ]
     */
    public static String[] listSubFiles(String filePath) {
        File dir = new File(filePath);
        String[] files = dir.list(FileFileFilter.FILE);
        return files;
    }

    /**
     * 列出一个目录下所有子文件的名字(注意不是全路径),不递归
     *
     * @param filePath
     *         the file path
     * @param wildcard
     *         the wildcard 通配符,比如"*commercial*"
     * @return the string [ ]
     */
    public static String[] listSubFils(String filePath, String wildcard) {
        File dir = new File(filePath);
        String[] files = dir.list(new AndFileFilter(FileFileFilter.FILE, new WildcardFileFilter(wildcard)));
        return files;
    }


    /**
     * 处理路径中的{var}格式的变量
     * FTP/{channnelName}/upload/ 替换成 FTP/CIKTV1/upload
     *
     * @param filePath
     *         the file path
     * @param varName
     *         the var name
     * @param varValue
     *         the var value
     * @return the string
     */
    public static String replaceWithPattenVarValues(String filePath, String varName, String varValue) {
        return filePath.replaceAll("{" + varName + "}", varValue);
    }

    /**
     * List directorys.递归列出目录下全部目录,支持通配符*
     *
     * @param path
     *         the path
     * @return the collection
     */
    public static Collection<File> listDirectorys(String path) {
        if (path == null) { // no * at all
            return null;
        }
        Collection<File> fixeds = new ArrayList<File>();
        if (!path.contains("*")) {
            fixeds.add(new File(path));
            return fixeds;
        }
        // 定位扫描根目录
        File root = null;
        String[] items = path.split("/");
        StringBuffer buf = new StringBuffer();
        int i = 0;
        for (; i < items.length; i++) {
            String item = items[i];
            if (item.contains("*")) {
                root = new File(buf.toString());
                break;
            } else {
                if (item.equals("")) {
                    buf.append("/");
                } else {
                    buf.append(item);
                    buf.append("/");
                }
            }
        }
        if (root == null) {
            root = new File(buf.toString());
        }
        buf = new StringBuffer();
        buf.append("^");
        for (; i < items.length; i++) {
            String item = items[i];
            buf.append(item);
            buf.append("/");
        }

        // 自己过滤

        String rootPath = FileUtils.formatDirectory(root.getPath());
        if(buf.charAt(buf.length()-1)!='/'){
            buf.append("/");
        }
        buf.append("{1}");
        buf.append("$");
        String patten = buf.toString();
        patten = patten.replaceAll("\\*", "[^/]*?");
        Collection<File> files = FileUtils.listFilesAndDirs(root, FalseFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        Pattern p = Pattern.compile(patten);
        for (File file : files) {
            String fName = FileUtils.formatDirectory(file.getPath());
            String relativePath = FileUtils.sub(fName,rootPath);
            if(relativePath.equals("")){
                relativePath = "/";
            }
            if(p.matcher(relativePath).find()){
                fixeds.add(file);
            }
        }

        return fixeds;
    }

    /**
     * List directorys.递归列出目录下全部文件,支持通配符*
     *
     * @param path
     *         the path
     * @return the collection
     */
    public static Collection<File> listFiles(String path) {
        if (path == null) { // no * at all
            return null;
        }
        Collection<File> fixeds = new ArrayList<File>();
        if (!path.contains("*")) {
            fixeds.add(new File(path));
            return fixeds;
        }
        // 定位扫描根目录
        File root = null;
        String[] items = path.split("/");
        StringBuffer buf = new StringBuffer();
        int i = 0;
        for (; i < items.length; i++) {
            String item = items[i];
            if (item.contains("*")) {
                root = new File(buf.toString());
                break;
            } else {
                if (item.equals("")) {
                    buf.append("/");
                } else {
                    buf.append(item);
                    buf.append("/");
                }
            }
        }
        if (root == null) {
            root = new File(buf.toString());
        }
        buf = new StringBuffer();
        for (; i < items.length; i++) {
            String item = items[i];
            buf.append(item);
            buf.append("/");
        }
        // 找到所有文件,自己过滤
        String rootPath = FileUtils.formatFile(root.getPath());

        buf.append("$");
        String patten = buf.toString();
        patten = patten.replaceAll("\\*", "[^/]*?");

        Collection<File> dirs = FileUtils.listFilesAndDirs(root, TrueFileFilter.INSTANCE, FalseFileFilter.INSTANCE);
        Pattern p = Pattern.compile(patten);
        for (File dir : dirs) {
            String fName = FileUtils.formatFile(dir.getPath());
            String relativePath = FileUtils.sub(fName,rootPath);
            if(p.matcher(relativePath).find()){
                fixeds.add(dir);
            }
        }

        return fixeds;
    }


    /**
     * 扫描一个路径下所有的文件列表(非文件夹),一些合法的路径名:<br/>
     * /FTP/频道-{channelId}/upload/<br/>
     * FTP/频道-{channelId}/upload/<br/>
     * ~/FTP/频道-{channelId}/upload/{yyyyMMdd}/<br/>
     * ~/FTP/频*道/upload/{yyyyMMdd}/<br/>
     *
     * @param pathPatten
     *         扫描路径的模式,<br/>
     *         支持通配符*表示所有目录<br/>
     *         支持变量,格式是{变量名}<br/>
     *         不支持一个目录级别配置多个变量,即 FTP/频道-{channelName}-{channelId}/upload/<br/>
     *         <p/>
     *         不支持一个目录级别通配符*和变量一起使用,即 FTP/频道-*-{channelId}/upload/<br/>
     *         eg, /FTP/频道-{channelId}/upload/ 扫描 /FTP目录下/所有能匹配到channelId变量的目录/只有upload目录/下面全部子目录,~/FTP/{channelname}/upload/
     * @param pattenVarValues
     *         扫描路径的模式使用的变量名和变量值的集合,变量值支持两种格式,一个是String或者List<String>
     * @return List<Object> List<Object>[0] 是一个List<String>,包含所有扫面出来的路径, List<String>[1] 是一个Map<KeyValueEntry<String,String>,List<String>> 包含扫描出来的路径在拼接过程中使用的是那些变量
     */
    public static List<String> listDirectoryByPathPatten(String pathPatten, Map<String, Object> pattenVarValues) {
        List<String> listedDirectories = new ArrayList<String>();
        listedDirectories.add(pathPatten);
        listedDirectories = matchPathPattenVar(listedDirectories, pattenVarValues);
        List<String> dirs = new ArrayList<String>();

        for (String dir : listedDirectories) {
            Collection<File> files = listDirectorys(dir);
            for (File f : files) {
                dirs.add(formatDirectory(f.getPath()));
            }

        }
        return dirs;
    }

    /**
     * List directory by path patten v 2.
     *
     * @param pathPattens
     *         the path pattens
     * @param pattenVarValues
     *         the patten var values
     * @return the list
     */

    @SuppressWarnings("rawtypes")
	private static List<String> matchPathPattenVar(List<String> pathPattens, Map<String, Object> pattenVarValues) {
        if (pathPattens == null || pathPattens.isEmpty()) {
            return null;
        }
        if (pattenVarValues == null || pattenVarValues.isEmpty()) {
            return pathPattens;
        }
        List<String> listedDirectories = pathPattens;
        //匹配{var}模式

        //第一步,先将所有的路径拼出来
        for (Map.Entry<String, Object> varEntry : pattenVarValues.entrySet()) {
            String varName = varEntry.getKey();
            Object var = varEntry.getValue();
			if (var == null) {
				continue;
			}
            Pattern p = Pattern.compile("(\\{)" + varName + "(\\})");
            if (!p.matcher(pathPattens.get(0)).find()) { //路径中不存在此变量替代符
                continue;
            }
            
            if (var instanceof Collection) {  //填充变量是个List<String>,需要扩充列表长度
				Collection casted = (Collection) var;
				if (!casted.isEmpty()) {
					List<String> nListed = new ArrayList<String>();
					for (int j = 0; j < listedDirectories.size(); j++) {
						String patten = listedDirectories.get(j);
						for (Object item : casted) {
							String modPatten = patten.replaceFirst("\\{"
									+ varName + "\\}", item.toString());
							nListed.add(modPatten);
						}
					}
					listedDirectories = nListed;
				}
            } else {  //填充变量是个单个变量,只要替换全部字符串即可
                for (int j = 0; j < listedDirectories.size(); j++) {
                    String newPath = listedDirectories.get(j).replaceFirst("\\{" + varName + "\\}", var.toString());
                    listedDirectories.add(j, newPath);
                }
            }

        }
        Pattern p = Pattern.compile("(\\{)(.*?)(\\})");
        if (pathPattens.get(0) != null) { //检查是不是要递归继续替换
            Matcher m = p.matcher(pathPattens.get(0));
            boolean nextrun = false;
            while (m.find()) {
            	String key = m.group(2);
                if (pattenVarValues.containsKey(key)) {
                	Object value = pattenVarValues.get(key);
					if (value == null){
						continue;
					}
					if (value instanceof Collection
							&& ((Collection) value).isEmpty()) {
						continue;
					}
                    nextrun = true;
                }
            }
            if (nextrun) {
                return matchPathPattenVar(listedDirectories, pattenVarValues);
            }

        }
        return listedDirectories;
    }


    /**
     * 扫描一个路径下所有的文件列表(非文件夹),一些合法的路径名:<br/>
     * /FTP/频道-{channelId}/upload/<br/>
     * FTP/频道-{channelId}/upload/<br/>
     * ~/FTP/频道-{channelId}/upload/{yyyyMMdd}/<br/>
     * ~/FTP/频*道/upload/{yyyyMMdd}/<br/>
     *
     * @param pathPatten
     *         扫描路径的模式,<br/>
     *         支持通配符*表示所有目录<br/>
     *         支持变量,格式是{变量名}<br/>
     *         不支持一个目录级别配置多个变量,即 FTP/频道-{channelName}-{channelId}/upload/<br/>
     *         <p/>
     *         不支持一个目录级别通配符*和变量一起使用,即 FTP/频道-*-{channelId}/upload/<br/>
     *         eg, /FTP/频道-{channelId}/upload/ 扫描 /FTP目录下/所有能匹配到channelId变量的目录/只有upload目录/下面全部子目录,~/FTP/{channelname}/upload/
     * @param pattenVarValues
     *         扫描路径的模式使用的变量名和变量值的集合,变量值支持两种格式,一个是String或者List<String>
     * @return List<Object> List<Object>[0] 是一个List<String>,包含所有扫面出来的路径, List<String>[1] 是一个Map<KeyValueEntry<String,String>,List<String>> 包含扫描出来的路径在拼接过程中使用的是那些变量
     */

    @SuppressWarnings("rawtypes")
	public static List<String> listDirectoryByPathPattenNoUse(String pathPatten, Map<String, Object> pattenVarValues) {
        // TODO 应该利用WildcardFileFilter来实现....
        // TODO 当文件夹很多的时候,内存堆栈会是个问题 ...
        pathPatten = FileUtils.formatDirectory(pathPatten);
        String[] pattens = pathPatten.split(FileUtils.FILE_SEPARATOR_STR);

        List<String> listedDirectories = new ArrayList<String>();
        //匹配{var}模式
        Pattern p = Pattern.compile("(\\{)(.*?)(\\})");

        for (int i = 0; i < pattens.length; i++) {
            String patten = pattens[i];
            if (StringUtils.isBlank(patten)) { // eg:"/"
                listedDirectories.add("/");
            } else if ("~".equals(patten)) { //  eg:"~/"
                listedDirectories.add("~/");
            } else if (patten.contains("*")) { //  eg:"xxx*/"
                List<String> nScanList = new ArrayList<String>();
                for (int j = 0; j < listedDirectories.size(); j++) {
                    String[] scanedFolders =
                            FileUtils.listSubDirectorys(listedDirectories.get(j), patten);
                    for (String s : scanedFolders) {
                        nScanList.add(FileUtils.formatDirectory(listedDirectories.get(j) + s + FileUtils.FILE_SEPARATOR_STR));
                    }
                }
                listedDirectories = nScanList;
            } else {
                //检查是否存在变量{var}的模式
                Matcher matcher = p.matcher(patten);
                if (!matcher.find()) { //不存在变量,则为固定写死的目录,不会导致needScanFolders总数上涨
                    if (listedDirectories.isEmpty()) {//第一个目录
                        listedDirectories.add(patten + FileUtils.FILE_SEPARATOR_STR);
                    } else {
                        List<String> nScanList = new ArrayList<String>();
                        for (int j = 0; j < listedDirectories.size(); j++) {
                            nScanList.add(j, listedDirectories.get(j) + patten + FileUtils.FILE_SEPARATOR_STR);
                        }
                        listedDirectories = nScanList;
                    }

                } else {//如果定义了{var}格式的变量,则在入参pattenVars中又没有输入的情况下,这个目录下则变成全部扫描
                    String pVarName = matcher.group(2);
                    Object pVarValue = pattenVarValues != null ? pattenVarValues.get(pVarName) : null;
                    if (pVarValue == null) { //根据扫描目录拼接路径列表,需要扩充列表长度
                        List<String> nScanList = new ArrayList<String>();
                        for (int j = 0; j < listedDirectories.size(); j++) {
                            String[] scanedFolders =
                                    FileUtils.listSubDirectorys(listedDirectories.get(j));
                            for (String s : scanedFolders) {
                                nScanList.add(FileUtils.formatDirectory(listedDirectories.get(j) + s + FileUtils.FILE_SEPARATOR_STR));
                            }
                        }
                        listedDirectories = nScanList;

                    } else { //根据填充的变量拼接
                        if (pVarValue instanceof List) { //填充变量是个List<String>,需要扩充列表长度
                            List pVarValueList = (List) pVarValue;
                            List<String> nScanList = new ArrayList<String>();
                            for (Object item : pVarValueList) {
                                String modPatten = new String(patten);
                                List<String> historyList = new ArrayList<String>();
                                modPatten = p.matcher(modPatten).replaceFirst(item.toString());
                                for (int j = 0; j < listedDirectories.size(); j++) {
                                    String newPath = listedDirectories.get(j) + modPatten + FileUtils.FILE_SEPARATOR_STR;
                                    historyList.add(newPath);
                                    nScanList.add(newPath);
                                }
                                listedDirectories = nScanList;
                            }
                        } else {//填充变量是个String,不需要扩充列表长度
                            String modPatten = new String(patten);
                            modPatten = p.matcher(modPatten).replaceFirst(pVarValue.toString());
                            List<String> nScanList = new ArrayList<String>();
                            for (int j = 0; j < listedDirectories.size(); j++) {
                                nScanList.add(j, listedDirectories.get(j) + modPatten + FileUtils.FILE_SEPARATOR_STR);
                            }
                            listedDirectories = nScanList;
                        }

                    }
                }

            }
        }
        return listedDirectories;

    }

    /**
     * 匹配 路径模式和真实路径
     *
     * @param varName
     *         the var name
     * @param varValue
     *         the var value
     * @param pathPatten
     *         the path patten
     * @param path
     *         the input
     * @return the boolean
     */
    public static boolean isPathUsePattenVar(String varName, String varValue, String pathPatten, String path) {
        String fixedPathPatten = getRealStrPattenFromPathPatten(varName, varValue, pathPatten);
        return matchFixedPathPattenAndPath(fixedPathPatten, path);
    }

    /**
     * 匹配 路径模式和真实路径
     *
     * @param fixedPathPatten
     *         the path patten
     * @param path
     *         the input
     * @return the boolean
     */
    private static boolean matchFixedPathPattenAndPath(String fixedPathPatten, String path) {

        Pattern p = Pattern.compile(fixedPathPatten);
        Matcher matcher = p.matcher(path);
        boolean find = matcher.find();
        if (find) {
            log.debug("search {} from {}, result is {}, search content is {}", new Object[]{
                    fixedPathPatten, path, find, matcher.group()
            });
        } else {
            log.debug("search {} from {}, result is {}", new Object[]{
                    fixedPathPatten, path, find
            });
        }

        return find;
    }


    /**
     * Gets fixed path patten.
     *
     * @param varName
     *         the var name
     * @param varValue
     *         the var value
     * @param pathPatten
     *         the path patten
     * @return the fixed path patten
     */
    private static String getRealStrPattenFromPathPatten(String varName, String varValue, String pathPatten) {
        String fixedPathPatten = null;
        // 第一步,处理通配符,*-->.*?
        String patten = new String(pathPatten);
        patten = patten.replaceAll("\\*", ".*?");
        // 第二步,找到变量名字被使用的第一个位置,截取前面部分
        Pattern p = Pattern.compile("(.*?)(\\{" + varName + "\\})(.*?/)(.*)");
        Matcher m = p.matcher(patten);
        if (m.find()) {
            // 第三步,将其他变量{var}替换成".*?"
            String bPatten = "^" + m.group(1).replaceAll("\\{.*?\\}", ".*?");
            // 第四步,将变量名字{varName}替换成变量值varValue
            fixedPathPatten = bPatten + varValue + m.group(3);
        }
        return fixedPathPatten;


    }

    public static String absolutePath(String file){
        return new File(file).getAbsolutePath();
    }

    /**
     * Main void.
     *
     * @param args
     *         the args
     */
    public static void main(String[] args) {
        Map<String, Object> vars = new HashMap<String, Object>();
        List<String> channelNames = new ArrayList<String>();
        channelNames.add("CIKFOOTV");
        channelNames.add("CIKTV1");
        channelNames.add("CIKTV2");
        channelNames.add("CEC");
        vars.put("channelName", channelNames);
        List<String> dirs = listDirectoryByPathPatten("/FTP/{channelName}/upload/", vars);
        for (String dir : dirs) {
            System.out.println(dir);
        }
        System.out.println("*******************************************");
        Collection<File> files2 = listDirectorys("/FTP/CIKFOOTV/upload/");
        for (File dir : files2) {
            System.out.println(dir.getPath());
        }
        System.out.println("*******************************************");
        Collection<File> files3 = listDirectorys("/FTP/CIKFOOTV/upload/*");
        for (File dir : files3) {
            System.out.println(dir.getPath());
        }
        System.out.println("*******************************************");
        Collection<File> files = listDirectorys("/FTP/*/upload/*/plugins/*");
        for (File dir : files) {
            System.out.println(dir.getPath());
        }

    }

}
