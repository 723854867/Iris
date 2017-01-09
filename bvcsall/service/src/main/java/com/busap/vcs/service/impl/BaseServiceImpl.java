package com.busap.vcs.service.impl;


import com.busap.vcs.base.Filter;
import com.busap.vcs.base.GroupByBean;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.constants.BicycleConstants;
import com.busap.vcs.service.*;
import com.busap.vcs.data.entity.FetchInfo;
import com.busap.vcs.data.entity.JoinInfo;
import com.busap.vcs.data.entity.RootInfo;
import com.busap.vcs.data.repository.BaseRepository;

import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @param <T>
 * @param <ID>
 */
@Transactional
public class BaseServiceImpl<T, ID extends Serializable> implements
        BaseService<T, ID> {
    /**
     * 默认过滤掉的数据型,调用update类函数时,不会更新
     */
    private static final String[] BaseIgnoreProperties = {"id", "createDate",
            "modifyDate"};
    private Map<String, PropertyDescriptor> propertyDescriptors;

    public void setBaseRepository(BaseRepository<T, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    protected BaseRepository<T, ID> baseRepository;

    @Resource(name="jedisService")
    private JedisService jedisService;

    /**
     * The Entity class. service对应的Entity的类
     */
    private Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl() {
        Type type = getClass().getGenericSuperclass();
        Type[] parameterizedType = ((ParameterizedType) type)
                .getActualTypeArguments();
        entityClass = ((Class<T>) parameterizedType[0]);
        PropertyDescriptor[] ps = BeanUtils
                .getPropertyDescriptors(entityClass);
        propertyDescriptors = new HashMap<String, PropertyDescriptor>();
        // 建立反向查询PropertyDescriptor的table,属性名是大小写不敏感的
        for (PropertyDescriptor pd : ps) {
            propertyDescriptors.put(pd.getName().toLowerCase(), pd);
        }
    }

    /**
     * Get entity class. service对应的Entity的类
     *
     * @return the class
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    public Map<String, PropertyDescriptor> getEntityPropertyDescriptors() {
        return propertyDescriptors;
    }

    public PropertyDescriptor getEntityPropertyDescriptor(String ignoredPropertyName) {
        PropertyDescriptor pd = this.propertyDescriptors.get(ignoredPropertyName);
        if (pd == null) {
            pd = this.propertyDescriptors.get(ignoredPropertyName.toLowerCase());
        }
        return pd;
    }

    @Override
    @Transactional(readOnly = true)
    public T find(ID id) {
        return baseRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(ID... ids) {
        return baseRepository.findAll(Arrays.asList(ids));
    }


    @Override
    @Transactional(readOnly = true)
    public List<T> findAll(Integer first, Integer count, List<Filter> filters,
                           Sort sort) {
        return baseRepository.findAll(first, count, filters, sort);
    }

    @Override
    public Page<T> findAll(Pageable paramPageable, List<Filter> filters) {
        return baseRepository.findAll(paramPageable, filters);
    }
    
    public Page<T> findAll(Pageable pageable, List<RootInfo> rootList,List<JoinInfo> joinList,List<FetchInfo> fetchList,List<Filter> filters,List<OrderByObject> orderByObjList) {
        return baseRepository.findAll(pageable, rootList, joinList, fetchList, filters, orderByObjList);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<T> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return count(new Filter[0]);
    }

    @Override
    @Transactional(readOnly = true)
    public long count(Filter... filters) {
        return baseRepository.count(filters);
    }

    @Override
    public long count(List<Filter> filters) {
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return baseRepository.exists(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Filter... filters) {
        return baseRepository.count(filters) > 0L;
    }

    @Transactional(readOnly = true)
    public boolean exists(List<Filter> filters) {
        return baseRepository.count(filters) > 0L;
    }

    @Override
    @Transactional
    public void save(T entity) {
        baseRepository.save(entity);
    }

    @Override
    @Transactional
    public T update(T entity) {
        return baseRepository.save(entity);
    }

    @Override
    @Transactional
    public T update(T entity, String... ignoreProperties) {
        Assert.notNull(entity);
        if (baseRepository.isManaged(entity)) {
            throw new IllegalArgumentException("Entity must not be managed");
        }
        T t = baseRepository.findOne(baseRepository.getIdentifier(entity));
        if (t != null) {
            copyProperties(entity, t, (String[]) ArrayUtils.addAll(
                    ignoreProperties, BaseIgnoreProperties));
            return update(t);
        }
        return update(entity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        delete(baseRepository.findOne(id));
    }

    @Override
    @Transactional
    public void delete(ID... ids) {
        if (ids != null) {
            for (ID id : ids) {
                delete(baseRepository.findOne(id));
            }
        }
    }

    @Override
    @Transactional
    public void delete(T entity) {
        if (entity == null) return;
        baseRepository.delete(entity);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void copyProperties(Object source, Object target,
                                String[] ignoreProperties) {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        PropertyDescriptor[] propertyDescriptors = BeanUtils
                .getPropertyDescriptors(target.getClass());
        List<String> ignorePropertieList = ignoreProperties != null ? Arrays
                .asList(ignoreProperties) : null;
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            if ((propertyDescriptor.getWriteMethod() != null)
                    && ((ignoreProperties == null) || (!ignorePropertieList
                    .contains(propertyDescriptor.getName())))) {
                PropertyDescriptor sourcePropertyDescriptor = BeanUtils
                        .getPropertyDescriptor(source.getClass(),
                                propertyDescriptor.getName());
                if ((sourcePropertyDescriptor != null)
                        && (sourcePropertyDescriptor.getReadMethod() != null)) {
                    try {
                        Method sourceReadMethod = sourcePropertyDescriptor
                                .getReadMethod();
                        if (!Modifier.isPublic(sourceReadMethod
                                .getDeclaringClass().getModifiers())) {
                            sourceReadMethod.setAccessible(true);
                        }
                        Object sourceObject = sourceReadMethod.invoke(source,
                                new Object[0]);
                        Object targetObject = sourceReadMethod.invoke(target,
                                new Object[0]);
                        if ((sourceObject != null) && (targetObject != null)
                                && ((targetObject instanceof Collection))) {
                            Collection<?> targetCollection = (Collection<?>) targetObject;
                            targetCollection.clear();
                            targetCollection.addAll((Collection) sourceObject);
                        } else {
                            Method targetWriteMethod = propertyDescriptor
                                    .getWriteMethod();
                            if (!Modifier.isPublic(targetWriteMethod
                                    .getDeclaringClass().getModifiers())) {
                                targetWriteMethod.setAccessible(true);
                            }
                            targetWriteMethod.invoke(target,
                                    new Object[]{targetObject});
                        }
                    } catch (Throwable t) {
                        throw new FatalBeanException(
                                "Could not copy properties from source to target",
                                t);
                    }
                }
            }
        }
    }
    
    public List getObjectByJpql(StringBuffer  jpql,Integer currentPage,Integer numberPerPage,String defaultClassAlias,List<ParameterBean> paramList,List<GroupByBean> groupByList,List<OrderByBean> orderByList) throws Exception{
    	return baseRepository.getObjectByJpql(jpql, currentPage, numberPerPage, defaultClassAlias, paramList, groupByList, orderByList);
    }
    
    public Long getObjectCountByJpql(StringBuffer  countJpql,List<ParameterBean> paramList) throws Exception{
    	return baseRepository.getObjectCountByJpql(countJpql, paramList);
    }
    
    public Session getSession() {
    	return baseRepository.getSession();
    }

    /**
     * 是否存在黑名单中
     * @param uid 用户ID
     * @param userId 用户ID 判断是否加入黑名单的用户ID
     * @return boolean true 加入黑名单 false 否
     */
    @Override
    public Boolean isExistBlackList(String uid, String userId) {
        if (jedisService.isSetMemberInShard(BicycleConstants.BLACK_LIST_USER_ID + uid, String.valueOf(userId))) {
            return true;
        } else {
            return false;
        }
    }
}
