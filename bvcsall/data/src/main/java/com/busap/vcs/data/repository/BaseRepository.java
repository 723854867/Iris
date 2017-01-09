package com.busap.vcs.data.repository;

import com.busap.vcs.base.Filter;
import com.busap.vcs.base.GroupByBean;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.FetchInfo;
import com.busap.vcs.data.entity.JoinInfo;
import com.busap.vcs.data.entity.RootInfo;

import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.LockModeType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by djyin on 7/19/2014.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QueryDslPredicateExecutor<T> {


    /**
     * 按照过滤条件,查询全部
     *
     * @param filters the filters 查询条件
     * @param sort    the orders 排序条件
     * @return the page
     */
    public abstract List<T> findAll(
            List<Filter> filters, Sort sort);

    /**
     * 按照过滤条件,查询全部
     *
     * @param first   从第几个开始返回
     * @param count   返回多少条
     * @param filters the filters 查询条件
     * @param sort    the orders 排序条件
     * @return the page
     */
    public abstract List<T> findAll(Integer first, Integer count,
                                    List<Filter> filters, Sort sort);

    /**
     * 按照过滤条件,分页查询
     *
     * @param pageable the pageable 分页设置, 包含排序条件
     * @param filters  the filters 查询条件
     * @return the page
     */
    public abstract Page<T> findAll(Pageable pageable, List<Filter> filters);



    /**
     * 按照过滤条件,查询数目
     *
     * @param filters the filters 查询条件
     * @return the page
     */
    public abstract long count(List<Filter> filters);

    /**
     * 按照过滤条件,查询数目
     *
     * @param filters the filters 查询条件
     * @return the page
     */
    public abstract long count(Filter... filters);




    /**
     * 获取主键
     *
     * @param entity the entity
     * @return the identifier
     */
    public abstract ID getIdentifier(T entity);

    /**
     * 实体对象是否处于被实体管理器管理中
     *
     * @param entity the entity
     * @return the boolean
     */
    public abstract boolean isManaged(T entity);

    /**
     * 让实体对象脱离实体管理器管理
     *
     * @param entity the entity
     */
    public abstract void detach(T entity);

    /**
     * 锁定对象
     *
     * @param entity       the entity
     * @param lockModeType the lock mode type
     */
    public abstract void lock(T entity, LockModeType lockModeType);

    /**
     * 清理实体管理器
     */
    public abstract void clear();

    /**
     * 强制实体管理器刷新提交
     */
    public abstract void flush();
    
    public Page<T> findAll(Pageable pageable, List<RootInfo> rootList,List<JoinInfo> joinList,List<FetchInfo> fetchList,List<Filter> filters,List<OrderByObject> orderByObjList);
    
    public List getObjectByJpql(StringBuffer  jpql,Integer currentPage,Integer numberPerPage,String defaultClassAlias,List<ParameterBean> paramList,List<GroupByBean> groupByList,List<OrderByBean> orderByList) throws Exception;
    
    public Long getObjectCountByJpql(StringBuffer  countJpql,List<ParameterBean> paramList) throws Exception;
    
    public Session getSession();
}