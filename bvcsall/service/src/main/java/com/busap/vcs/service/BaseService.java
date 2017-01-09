package com.busap.vcs.service;


import com.busap.vcs.base.Filter;
import com.busap.vcs.base.GroupByBean;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.FetchInfo;
import com.busap.vcs.data.entity.JoinInfo;
import com.busap.vcs.data.entity.RootInfo;
import com.busap.vcs.data.repository.BaseRepository;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract interface BaseService<T, ID extends Serializable> {

    /**
     * Gets entity class.
     *
     * @return the entity class
     */
    public Class<T> getEntityClass();

    /**
     * Gets entity property descriptors.
     *
     * @return the entity property descriptors
     */
    public Map<String, PropertyDescriptor> getEntityPropertyDescriptors();

    /**
     * Gets entity property descriptor.
     *
     * @param ignoredPropertyName the ignored property name
     * @return the entity property descriptor
     */
    public PropertyDescriptor getEntityPropertyDescriptor(String ignoredPropertyName);

    /**
     * 根据ID查找
     *
     * @param paramID the param iD
     * @return the t
     */
    public abstract T find(ID paramID);

    /**
     * 查找全部
     *
     * @return the list
     */
    public abstract List<T> findAll();

    /**
     * 按ID集合查找
     *
     * @param ids the ids
     * @return the list
     */
    public abstract List<T> findAll(ID... ids);


    /**
     * 按照过滤条件查找
     *
     * @param start   元素开始的index下标,注意不是页码
     * @param length  返回集合的长度,相当于分页大小
     * @param filters the filters 过滤条件
     * @param sort  the orders 排序条件
     * @return the list
     */
    public abstract List<T> findAll(Integer start,
                                    Integer length, List<Filter> filters,
                                    Sort sort);


    /**
     * 按照过滤条件查找
     *
     * @param paramPageable the param 分页和排序条件
     * @param filters the filters 过滤条件
     * @return the list
     */
    public abstract Page<T> findAll(Pageable paramPageable, List<Filter> filters);


    /**
     * 按照过滤条件(在分页条件对象中)查找
     *
     * @param paramPageable the param pageable
     * @return the page
     */
    public abstract Page<T> findAll(Pageable paramPageable);

    /**
     * 计数
     *
     * @return the long
     */
    public abstract long count();

    /**
     * 按照条件计数
     *
     * @param filters the filters
     * @return the long
     */
    public abstract long count(Filter... filters);

    /**
     * 按照条件计数
     *
     * @param filters the filters
     * @return the long
     */
    public abstract long count(List<Filter> filters);

    /**
     * 查询满足条件ID的条目是否存在
     *
     * @param id the id
     * @return the boolean
     */
    public abstract boolean exists(ID id);

    /**
     * 查询满足条件的条目是否存在
     *
     * @param filters the filters
     * @return the boolean
     */
    public abstract boolean exists(Filter... filters);

    /**
     * 查询满足条件的条目是否存在
     *
     * @param filters the filters
     * @return the boolean
     */
    public abstract boolean exists(List<Filter> filters);

    /**
     * 创建
     *
     * @param entity the entity
     */
    public abstract void save(T entity);

    /**
     * 更新
     *
     * @param entity the entity
     * @return the t
     */
    public abstract T update(T entity);

    /**
     * 更新部分元素
     *
     * @param entity           the entity
     * @param skipPropertyName the skip property name
     * @return the t
     * @throws IllegalArgumentException entity如果是已经被实体管理器管理的
     */
    public abstract T update(T entity, String... skipPropertyName);

    /**
     * 删除
     *
     * @param id the id
     */
    public abstract void delete(ID id);

    /**
     * 删除全部
     *
     * @param ids the ids
     */
    public abstract void delete(ID... ids);

    /**
     * 删除
     * @param entity the entity
     */
    public abstract void delete(T entity);

    
    /**
     * 
     */
    public abstract void setBaseRepository(BaseRepository<T,ID> baseRepository);
    
    public Page<T> findAll(Pageable pageable, List<RootInfo> rootList,List<JoinInfo> joinList,List<FetchInfo> fetchList,List<Filter> filters,List<OrderByObject> orderByObjList);
    
    public List getObjectByJpql(StringBuffer  jpql,Integer currentPage,Integer numberPerPage,String defaultClassAlias,List<ParameterBean> paramList,List<GroupByBean> groupByList,List<OrderByBean> orderByList) throws Exception;
    
    public Long getObjectCountByJpql(StringBuffer  countJpql,List<ParameterBean> paramList) throws Exception;
    
    public Session getSession();

    /**
     * 是否存在黑名单中
     * @param uid 用户ID
     * @param userId 用户ID 判断是否加入黑名单的用户ID
     * @return boolean true 加入黑名单 false 否
     */
    public Boolean isExistBlackList(String uid,String userId);


}
