package com.busap.vcs.webcomn;

import com.busap.vcs.util.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * Created by
 * User: djyin
 * Date: 12/26/13
 * Time: 8:42 AM
 * 扩展spring的ReloadableResourceBundleMessageSource，支持Wildcard格式的文件名配置 。
 * 并强制打开 useCodeAsDefaultMessage （即不可关闭）
 * 初步：同一个目录下的
 * 进一步：
 */
public class WildcardResourceBundleMessageSource extends ReloadableResourceBundleMessageSource
        implements ResourceLoaderAware {

    private static final Logger log = LoggerFactory.getLogger(WildcardResourceBundleMessageSource.class);
    // 拼接后缀名
    private static Set<String> localeSuffixs = new HashSet<String>();

    @Autowired
    ServletContext servletContext;

    {
        // 这样做集合内容太多了
//        Locale[] locales = Locale.getAvailableLocales();
//        for (Locale l : locales) {
//            localeSuffixs.add("_" + l + ".properties");
//            log.debug("_" + l + ".properties");
//        }
        // 简化列表


        localeSuffixs.add("_en.properties");
        localeSuffixs.add("_zh_CN.properties");
    }


    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
    }

    /**
     * 强制打开 useCodeAsDefaultMessage
     *
     * @param code
     *         the code
     * @return the default message
     */
    @Override
    protected String getDefaultMessage(String code) {
        return code;
    }
    @Override
    public void setBasenames(String... basenames) {
        // 分析
        String[] fixedBasenames = null;

        if (basenames != null) {
            List<String> fixed = new ArrayList<String>();

            for (int i = 0; i < basenames.length; i++) {
                String basename = basenames[i];
                Assert.hasText(basename, "Basename must not be empty");
                basename = FileUtils.formatFile(basename);
                // 解析 wildcard 模式
                if (basename.contains("*")) {
                    // 将文件名解析成目录+文件名的格式
                    String path = FilenameUtils.getFullPath(basename);
                    String fileName = FilenameUtils.getName(basename);
                    boolean shifted = false;
                    if (path.startsWith("/WEB-INF/") && servletContext != null) { // servlet容器环境下，需要修正地址
                        shifted = true;
                    }

                    File dir = shifted ? new File(servletContext.getRealPath(path)) : new File(path);
                    if (!dir.exists()) {
                        continue;
                    }
                    WildcardFileFilter wildcardFilter = new WildcardFileFilter(fileName);
                    SuffixFileFilter suffixFilter = new SuffixFileFilter(".properties");
                    FileFilter andFilter = new AndFileFilter(suffixFilter, wildcardFilter);
                    File[] files = dir.listFiles(andFilter);
                    if (files == null) {
                        continue;
                    }
                    for (File f : files) {
                        // 过滤掉 多国语言的后缀名，比如以 _zh_CN.properties 结尾的文件名
                        boolean skip = false;
                        for (String suffix : localeSuffixs) {
                            if (f.getPath().contains(suffix)) {
                                skip = true;
                                break;
                            }
                        }
                        if (skip) {
                            log.debug("message source is {}", f);
                        } else {
                            // 把路径修正后的修正回来
                            String nPath =  f.getPath();
                            // 转换绝对路径
                            if (shifted) {
                                String[] ss = nPath.split("WEB-INF");
                                nPath = "/WEB-INF" + FileUtils.formatFile(ss[1]);
                            }
                            // 要删除后缀名
                            nPath = StringUtils.removeEnd(nPath,".properties");
                            fixed.add(nPath);
                            log.debug("message source is {}", f);
                        }

                    }
                } else {
                    fixed.add(basename);
                    log.debug("message source is {}", basename);
                }
            }
            fixedBasenames = fixed.toArray(new String[]{});
        }
        super.setBasenames(fixedBasenames);
    }


}
