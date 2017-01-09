package com.busap.vcs.operate.spring;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import com.busap.vcs.operate.utils.ServerMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.util.StringValueResolver;

public class EnvPropertyPlaceholderConfigurer extends PropertyResourceConfigurer
        implements BeanNameAware, BeanFactoryAware {
    private BeanFactory beanFactory;
    private String beanName;
    private static final String SERVER_NAME = "server_name";

    public void setBeanFactory(BeanFactory beanFactory)
            throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setBeanName(String name) {
        this.beanName = name;
    }

    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        StringValueResolver stringValueResolver = new EnvStringValueResolver();
        BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(stringValueResolver);
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String name : beanNames)
            if ((!StringUtils.equals(name, this.beanName)) && (beanFactory.equals(this.beanFactory))) {
                BeanDefinition bd = beanFactory.getBeanDefinition(name);
                try {
                    visitor.visitBeanDefinition(bd);
                } catch (BeanDefinitionStoreException ex) {
                    throw new BeanDefinitionStoreException(bd.getResourceDescription(), name, ex.getMessage());
                }
            }
    }

    private class EnvStringValueResolver implements StringValueResolver {
        private EnvStringValueResolver() {
        }

        public String resolveStringValue(String strVal) throws BeansException {
            String value = parseStringValue(strVal);
            return StringUtils.isBlank(value) ? null : value;
        }

        private String parseStringValue(String strVal) {
            if ((StringUtils.startsWith(strVal, "${")) && (StringUtils.endsWith(strVal, "}"))) {
                String placeholder = StringUtils.remove(strVal, "${");
                placeholder = StringUtils.remove(placeholder, "}");
                return resolvePlaceholder(placeholder);
            }
            return strVal;
        }

        private String resolvePlaceholder(String placeholder) {
            String resolveVal = System.getProperty(placeholder);
            if (StringUtils.isBlank(resolveVal)) {
                if (StringUtils.equalsIgnoreCase(placeholder, "server_name")) {
                    try {
                        return InetAddress.getLocalHost().getHostName();
                    } catch (UnknownHostException e) {

                        return "";
                    }
                }
                if (StringUtils.isBlank(resolveVal)) {
                    resolveVal = StringUtils.join(ServerMapper.getInstance().getUrls(placeholder), ",");
                }

            }

            return resolveVal;
        }
    }
}