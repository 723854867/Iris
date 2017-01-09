package com.busap.vcs.data.repository.impl;

import com.busap.vcs.base.Filter;
import com.busap.vcs.base.GroupByBean;
import com.busap.vcs.base.OrderByBean;
import com.busap.vcs.base.OrderByObject;
import com.busap.vcs.base.ParameterBean;
import com.busap.vcs.data.entity.FetchInfo;
import com.busap.vcs.data.entity.JoinInfo;
import com.busap.vcs.data.entity.OrderEntity;
import com.busap.vcs.data.entity.RootInfo;
import com.busap.vcs.data.repository.BaseRepository;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.path.PathBuilder;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.persistence.criteria.*;

import java.io.Serializable;
import java.util.*;

/**
 * Created by djyin on 7/19/2014.
 * 使用 jpa的criteria功能, 增加一些默认的查询函数.
 * 配合controller实现默认的curd
 */
//TODO 用QueryDSL的替代掉JPA Criteria的代码
@SuppressWarnings({"unchecked","rawtypes"})
@NoRepositoryBean
public class BaseRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements BaseRepository<T, ID> {

	protected EntityManager entityManager;

    protected final Class<T> entityClass;

    private static volatile long COUNT = 0L;

    protected final JpaEntityInformation<T, ID> entityInformation;


    //All instance variables are available in super, but they are private
    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

    // QueryDSL相关的对象
    private final EntityPath<T> entityPath;
    private final PathBuilder<T> pathBuilder;
    protected final Querydsl querydsl;

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given
     * {@link JpaEntityInformation}.
     *
     * @param entityInformation
     * @param entityManager
     */


    public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given
     * domain type.
     *
     * @param entityInformation
     * @param entityManager
     * @param resolver
     */
    public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager,
                              EntityPathResolver resolver) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityClass = entityInformation.getJavaType();
        this.entityInformation = entityInformation;
        this.entityPath = resolver.createPath(entityInformation.getJavaType());
        this.pathBuilder = new PathBuilder<T>(entityPath.getType(), entityPath.getMetadata());
        this.querydsl = new Querydsl(entityManager, pathBuilder);
    }


    @Override
    public List<T> findAll(List<Filter> filters, Sort sort) {
        return findAll(null, null, filters, sort);
    }

    @Override
    public List<T> findAll(Integer first, Integer count, List<Filter> filters, Sort sort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder
                .createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        List<T> content = getListByPositionFilterOrder(criteriaQuery, first, count,
                filters, sort);
        return content;
    }

    @Override
    public Page<T> findAll(Pageable pageable, List<Filter> filters) {
        Assert.notNull(pageable);
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder
                .createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return getPageByPageablePositionFilterOrder(criteriaQuery, pageable, filters);
    }

    @Override
    public long count(List<Filter> filters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder
                .createQuery(entityClass);
        criteriaQuery.select(criteriaQuery.from(entityClass));
        Map<ParameterExpression<Date>, Date> extractedDateFilters = new HashMap<ParameterExpression<Date>, Date>();
        modifyQueryByFilter(criteriaQuery, filters, extractedDateFilters);
        long totalCount = getCountByFilter(criteriaQuery, filters, extractedDateFilters).longValue();
        return totalCount;
    }

    @Override
    public long count(Filter... filters) {
        return count(Arrays.asList(filters));
    }

    @Override
    public ID getIdentifier(T entity) {
        Assert.notNull(entity);
        return (ID) entityManager.getEntityManagerFactory()
                .getPersistenceUnitUtil().getIdentifier(entity);
    }

    @Override
    public boolean isManaged(T entity) {
        return entityManager.contains(entity);
    }

    @Override
    public void detach(T entity) {
        entityManager.detach(entity);
    }

    @Override
    public void lock(T entity, LockModeType lockModeType) {
        if ((entity != null) && (lockModeType != null)) {
            entityManager.lock(entity, lockModeType);
        }
    }

    @Override
    public void clear() {
        entityManager.clear();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    protected List<T> getListByPositionFilterOrder(
            CriteriaQuery<T> criteriaQuery, Integer first, Integer count,
            List<Filter> filters, Sort sort) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Root<T> root = correctResultClass(criteriaQuery);
        Map<ParameterExpression<Date>, Date> extractedDateFilters = new HashMap<ParameterExpression<Date>, Date>();
        modifyQueryByFilter(criteriaQuery, filters, extractedDateFilters);
        setOrder(criteriaQuery, sort);
        if (criteriaQuery.getOrderList().isEmpty()) {
            if (OrderEntity.class.isAssignableFrom(entityClass)) {
                criteriaQuery
                        .orderBy(new javax.persistence.criteria.Order[]{criteriaBuilder
                                .asc(root.get("order"))});
            } else {
                criteriaQuery
                        .orderBy(new javax.persistence.criteria.Order[]{criteriaBuilder
                                .desc(root.get("createDate"))});
            }
        }
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery)
                .setFlushMode(FlushModeType.COMMIT);
        for (Map.Entry<ParameterExpression<Date>, Date> entry : extractedDateFilters.entrySet()) {
            typedQuery.setParameter(entry.getKey(), entry.getValue(), TemporalType.TIMESTAMP);
        }
        if (first != null) {
            typedQuery.setFirstResult(first.intValue());
        }
        if (count != null) {
            typedQuery.setMaxResults(count.intValue());
        }
        return typedQuery.getResultList();
    }

    protected Page<T> getPageByPageablePositionFilterOrder(CriteriaQuery<T> criteriaQuery,
                                                           Pageable pageable, List<Filter> filters) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Root root = correctResultClass(criteriaQuery);
        Map<ParameterExpression<Date>, Date> extractedDateFilters = new HashMap<ParameterExpression<Date>, Date>();
        modifyQueryByFilter(criteriaQuery, filters, extractedDateFilters);
        setOrder(criteriaQuery, pageable.getSort());
        if (criteriaQuery.getOrderList().isEmpty()) {
            if (OrderEntity.class.isAssignableFrom(entityClass)) {
                criteriaQuery
                        .orderBy(new javax.persistence.criteria.Order[]{criteriaBuilder
                                .asc(root.get("order"))});
            } else {
                criteriaQuery
                        .orderBy(new javax.persistence.criteria.Order[]{criteriaBuilder
                                .desc(root.get("createDate"))});
            }
        }
        long totalCount = getCountByFilter(criteriaQuery, filters, extractedDateFilters).longValue();
        TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery)
                .setFlushMode(FlushModeType.COMMIT);
        for (Map.Entry<ParameterExpression<Date>, Date> entry : extractedDateFilters.entrySet()) {
            typedQuery.setParameter(entry.getKey(), entry.getValue(), TemporalType.DATE);
        }
        typedQuery.setFirstResult(((pageable.getPageNumber()-1)<0?0:(pageable.getPageNumber()-1))* pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        Page<T> page = new PageImpl<T>(typedQuery.getResultList(), pageable, totalCount);
        return page;
    }

    protected Long getCountByFilter(CriteriaQuery<T> criteriaQuery,
                                    List<Filter> filters, Map<ParameterExpression<Date>, Date> extractedDateFilters) {
        Assert.notNull(criteriaQuery);
        Assert.notNull(criteriaQuery.getSelection());
        Assert.notEmpty(criteriaQuery.getRoots());
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        if (extractedDateFilters == null) {
            extractedDateFilters = new HashMap<ParameterExpression<Date>, Date>();
        }
        modifyQueryByFilter(criteriaQuery, filters, extractedDateFilters);
        CriteriaQuery<Long> criteriaQueryTotal = criteriaBuilder
                .createQuery(Long.class);
        Iterator iterator = criteriaQuery.getRoots().iterator();
        while (iterator.hasNext()) {
            Root root = (Root) iterator.next();

            // Create and add a search root corresponding to the given entity,
            // forming a cartesian product with any existing roots.
            Root rootTotal = criteriaQueryTotal.from(root.getJavaType());

            // Assigns an alias to the selection item. Once assigned, an alias
            // cannot be changed or reassigned. Returns the same selection item.
            rootTotal.alias(generatedAlias(root));
            rootJoin(root, rootTotal);
        }
        Root totalRoot = correctResultClass(criteriaQueryTotal,
                criteriaQuery.getResultType());
        criteriaQueryTotal.select(criteriaBuilder.count(totalRoot));
        if (criteriaQuery.getGroupList() != null) {
            criteriaQueryTotal.groupBy(criteriaQuery.getGroupList());
        }
        if (criteriaQuery.getGroupRestriction() != null) {
            criteriaQueryTotal.having(criteriaQuery.getGroupRestriction());
        }
        if (criteriaQuery.getRestriction() != null) {
            criteriaQueryTotal.where(criteriaQuery.getRestriction());
        }
        TypedQuery query = entityManager.createQuery(criteriaQueryTotal)
                .setFlushMode(FlushModeType.COMMIT);
        for (Map.Entry<ParameterExpression<Date>, Date> entry : extractedDateFilters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue(), TemporalType.DATE);
        }

        return (Long) query.getSingleResult();
    }

    // Assigns an alias to the selection item. Once assigned, an alias cannot be
    // changed or reassigned. Returns the same selection item.
    private synchronized String generatedAlias(Selection<?> selection) {
        if (selection != null) {
            String str = selection.getAlias();
            if (str == null) {
                if (COUNT >= 1000L) {
                    COUNT = 0L;
                }
                str = "GeneratedAlias" + COUNT++;
                selection.alias(str);
            }
            return str;
        }
        return null;
    }

    // Perform a typecast upon the expression, returning a new expression
    // object. This method does not cause type conversion:
    // the runtime type is not changed. Warning: may result in a runtime
    // failure.
    private Root<T> correctResultClass(CriteriaQuery<T> criteriaQuery) {
        if (criteriaQuery != null) {
            return correctResultClass(criteriaQuery,
                    criteriaQuery.getResultType());
        }
        return null;
    }

    // Perform a typecast upon the expression, returning a new expression
    // object. This method does not cause type conversion:
    // the runtime type is not changed. Warning: may result in a runtime
    // failure.
    private Root<T> correctResultClass(CriteriaQuery<?> criteriaQuery,
                                       Class<T> resultClass) {
        if ((criteriaQuery != null) && (criteriaQuery.getRoots() != null)
                && (resultClass != null)) {
            Iterator iterator = criteriaQuery.getRoots().iterator();
            while (iterator.hasNext()) {
                Root root = (Root) iterator.next();
                if (resultClass.equals(root.getJavaType())) {
                    return (Root<T>) root.as(resultClass);
                }
            }
        }
        return null;
    }

    private void rootJoin(From<?, ?> root, From<?, ?> rootTotal) {
        Iterator iterator = root.getJoins().iterator();
        while (iterator.hasNext()) {
            Join join = (Join) iterator.next();
            Join joinTotal = rootTotal.join(join.getAttribute().getName(),
                    join.getJoinType());
            joinTotal.alias(generatedAlias((Selection) join));
            rootJoin((From) join, (From) joinTotal);
        }
        iterator = root.getFetches().iterator();
        while (iterator.hasNext()) {
            Fetch fetch = (Fetch) iterator.next();
            Fetch fetchTotal = rootTotal.fetch(fetch.getAttribute().getName());
            fetchJoin(fetch, fetchTotal);
        }
    }

    // Create a fetch join to the specified attribute using an inner join.
    private void fetchJoin(Fetch<?, ?> fetch, Fetch<?, ?> fetchTotal) {
        Iterator iterator = fetch.getFetches().iterator();
        while (iterator.hasNext()) {
            Fetch fetch1 = (Fetch) iterator.next();
            Fetch fetch2 = fetchTotal.fetch(fetch1.getAttribute().getName());
            fetchJoin(fetch1, fetch2);
        }
    }

    // Modify the search to restrict the search result according to the specified
    // boolean expression.
    private void modifyQueryByFilter(CriteriaQuery<T> criteriaQuery,
                                     List<Filter> filters, Map<ParameterExpression<Date>, Date> extractedDateFilters) {
        if ((criteriaQuery == null) || (filters == null) || (filters.isEmpty())) {
            return;
        }
        Root root = correctResultClass(criteriaQuery);
        if (root == null) {
            return;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Predicate predicate = criteriaQuery.getRestriction() != null ? criteriaQuery
                .getRestriction() : criteriaBuilder.conjunction();
        Iterator<Filter> iterator = filters.iterator();
        while (iterator.hasNext()) {
            Filter filter = iterator.next();
            if ((filter != null)
                    && (!StringUtils.isEmpty(filter.getProperty()))) {
                if ((filter.getOperator() == Filter.Operator.eq)
                        && (filter.getValue() != null)) {
                    if ((filter.getIgnoreCase() != null)
                            && (filter.getIgnoreCase().booleanValue())
                            && ((filter.getValue() instanceof String))) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.equal(criteriaBuilder
                                                .lower(root.get(filter.getProperty())),
                                        ((String) filter.getValue())
                                                .toLowerCase()));
                    } else {
                        predicate = criteriaBuilder.and(
                                predicate,
                                criteriaBuilder.equal(
                                        root.get(filter.getProperty()),
                                        filter.getValue()));
                    }
                } else if ((filter.getOperator() == Filter.Operator.ne)
                        && (filter.getValue() != null)) {
                    if ((filter.getIgnoreCase() != null)
                            && (filter.getIgnoreCase().booleanValue())
                            && ((filter.getValue() instanceof String))) {
                        predicate = criteriaBuilder.and(predicate,
                                criteriaBuilder.notEqual(criteriaBuilder
                                                .lower(root.get(filter.getProperty())),
                                        ((String) filter.getValue())
                                                .toLowerCase()));
                    } else {
                        predicate = criteriaBuilder.and(
                                predicate,
                                criteriaBuilder.notEqual(
                                        root.get(filter.getProperty()),
                                        filter.getValue()));
                    }
                } else if ((filter.getOperator() == Filter.Operator.gt)
                        && (filter.getValue() != null)) {
                    if (filter.getValue() instanceof Date) { //特殊处理Date

                        ParameterExpression<Date> ped = criteriaBuilder.parameter(Date.class);
                        // 取反, 其他的动态语句生成的SQL都是 属性 操作符 变量 形式,而 这里生成的却是 变量 操作符 属性 的形式
                        //Predicate datePredicate = criteriaBuilder.greaterThan(ped, root.get(filter.getProperty()));
                        Predicate datePredicate = criteriaBuilder.lessThan(ped, root.get(filter.getProperty()));
                        predicate = criteriaBuilder.and(predicate, datePredicate);
                        extractedDateFilters.put(ped, (Date) filter.getValue());
                    } else {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder
                                .gt(root.get(filter.getProperty()),
                                        (Number) filter.getValue()));
                    }


                } else if ((filter.getOperator() == Filter.Operator.lt)
                        && (filter.getValue() != null)) {
                    if (filter.getValue() instanceof Date) { //特殊处理Date
                        ParameterExpression<Date> ped = criteriaBuilder.parameter(Date.class);
                        // 取反, 其他的动态语句生成的SQL都是 属性 操作符 变量 形式,而 这里生成的却是 变量 操作符 属性 的形式
                        //Predicate datePredicate = criteriaBuilder.lessThan(ped, root.get(filter.getProperty()));
                        Predicate datePredicate = criteriaBuilder.greaterThan(ped, root.get(filter.getProperty()));
                        predicate = criteriaBuilder.and(predicate, datePredicate);
                        extractedDateFilters.put(ped, (Date) filter.getValue());
                    } else {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder
                                .lt(root.get(filter.getProperty()),
                                        (Number) filter.getValue()));
                    }
                } else if ((filter.getOperator() == Filter.Operator.ge)
                        && (filter.getValue() != null)) {
                    if (filter.getValue() instanceof Date) { //特殊处理Date
                        ParameterExpression<Date> ped = criteriaBuilder.parameter(Date.class);
                        // 取反, 其他的动态语句生成的SQL都是 属性 操作符 变量 形式,而 这里生成的却是 变量 操作符 属性 的形式
                        //Predicate datePredicate = criteriaBuilder.greaterThanOrEqualTo(ped, root.get(filter.getProperty()));
                        Predicate datePredicate = criteriaBuilder.lessThanOrEqualTo(ped, root.get(filter.getProperty()));
                        predicate = criteriaBuilder.and(predicate, datePredicate);
                        extractedDateFilters.put(ped, (Date) filter.getValue());
                    } else {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder
                                .ge(root.get(filter.getProperty()),
                                        (Number) filter.getValue()));
                    }
                } else if ((filter.getOperator() == Filter.Operator.le)
                        && (filter.getValue() != null)) {
                    if (filter.getValue() instanceof Date) { //特殊处理Date
                        ParameterExpression<Date> ped = criteriaBuilder.parameter(Date.class);
                        // 取反, 其他的动态语句生成的SQL都是 属性 操作符 变量 形式,而 这里生成的却是 变量 操作符 属性 的形式
                        //Predicate datePredicate = criteriaBuilder.lessThanOrEqualTo(ped, root.get(filter.getProperty()));
                        Predicate datePredicate = criteriaBuilder.greaterThanOrEqualTo(ped, root.get(filter.getProperty()));
                        predicate = criteriaBuilder.and(predicate, datePredicate);
                        extractedDateFilters.put(ped, (Date) filter.getValue());
                    } else {
                        predicate = criteriaBuilder.and(predicate, criteriaBuilder
                                .le(root.get(filter.getProperty()),
                                        (Number) filter.getValue()));
                    }
                } else if ((filter.getOperator() == Filter.Operator.like)
                        && (filter.getValue() != null)
                        && ((filter.getValue() instanceof String))) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder
                            .like(root.get(filter.getProperty()),
                                    (String) filter.getValue()));
                } else if ((filter.getOperator() == Filter.Operator.in)
                        && (filter.getValue() != null)) {
                    Collection<Object> values = (Collection<Object>) filter.getValue();
                    predicate = criteriaBuilder.and(
                            predicate,
                            root.get(filter.getProperty()).in(values.toArray(new Object[]{})));
                } else if (filter.getOperator() == Filter.Operator.isnull) {
                    predicate = criteriaBuilder.and(predicate,
                            root.get(filter.getProperty()).isNull());
                } else if (filter.getOperator() == Filter.Operator.isnotnull) {
                    predicate = criteriaBuilder.and(predicate,
                            root.get(filter.getProperty()).isNotNull());
                }
            }
        }
        criteriaQuery.where(predicate);
    }


    // Specify the ordering expressions that are used to order the search
    // results.
    private void setOrder(CriteriaQuery<T> criteriaQuery, Sort sort) {
        if ((criteriaQuery == null) || sort == null) {
            return;
        }
        Root root = correctResultClass(criteriaQuery);
        if (root == null) {
            return;
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<javax.persistence.criteria.Order> orderList = new ArrayList<javax.persistence.criteria.Order>();
        if (!criteriaQuery.getOrderList().isEmpty()) {
            orderList.addAll(criteriaQuery.getOrderList());
        }
        Iterator<Sort.Order> iterator = sort.iterator();
        while (iterator.hasNext()) {
            Sort.Order order = iterator.next();
            if (order.isAscending()) {
                orderList
                        .add(criteriaBuilder.asc(root.get(order.getProperty())));
            } else {
                orderList
                        .add(criteriaBuilder.desc(root.get(order.getProperty())));
            }
        }
        criteriaQuery.orderBy(orderList);
    }
    
    
    public Page<T> findAll(Pageable pageable, List<RootInfo> rootList,List<JoinInfo> joinList,List<FetchInfo> fetchList,List<Filter> filters,List<OrderByObject> orderByObjList) {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
        
//		criteriaQuery.select(criteriaQuery.from(entityClass));
		
		Map<String,Root> rootMap=new HashMap<String,Root>();
		Map<String,Join> joinMap=new HashMap<String,Join>();
		List<Selection> multiSelectList=new ArrayList<Selection>();
		
		for(int i=0;i<rootList.size();i++){
			RootInfo rInfo=rootList.get(i);
			Root rootTemp = criteriaQuery.from(rInfo.getClassType());
			rootMap.put(rInfo.getName(), rootTemp);
			multiSelectList.add(rootTemp);
//			criteriaQuery.select(rootTemp);
		}
		
		criteriaQuery.multiselect(multiSelectList);
//		criteriaQuery.multiselect(selections);
//		criteriaBuilder.construct(resultClass, selections);
		
		for(int i=0;i<joinList.size();i++){
			JoinInfo jInfo=joinList.get(i);
			if(jInfo.getCollectionType().equals("set")){
				Root rootTemp=rootMap.get(jInfo.getRootName());
				if(rootTemp!=null){
					SetJoin setJoin = rootTemp.join(rootTemp.getModel().getSet(jInfo.getName()) , jInfo.getJoinType());
					joinMap.put(jInfo.getName(), setJoin);
				}
			}else if(jInfo.getCollectionType().equals("one")){
				Root rootTemp=rootMap.get(jInfo.getRootName());
				if(rootTemp!=null){
					Join join = rootTemp.join(rootTemp.getModel().getSingularAttribute(jInfo.getName()));
					joinMap.put(jInfo.getName(), join);
				}
			}
		}
		
		Map<String, Date> extractedDateFilters = new HashMap<String, Date>();
		modifyQueryByFilter(criteriaQuery, filters, extractedDateFilters,rootMap,joinMap);
		setOrderBy(criteriaBuilder, criteriaQuery, rootMap, orderByObjList);

		long totalCount = getCount(rootList,joinList,fetchList, filters, extractedDateFilters).longValue();
		
		TypedQuery<T> typedQuery = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		for (Map.Entry<String, Date> entry : extractedDateFilters.entrySet()) {
			typedQuery.setParameter(entry.getKey(), entry.getValue(), TemporalType.DATE);
		}
		typedQuery.setFirstResult(((pageable.getPageNumber()-1)<0?0:(pageable.getPageNumber()-1))* pageable.getPageSize());
		typedQuery.setMaxResults(pageable.getPageSize());
//		Page<T> page = new PageImpl<T>(typedQuery.getResultList(), pageable, totalCount);
		Page page = new PageImpl(typedQuery.getResultList(), pageable, totalCount);
		return page;
	}
    
	protected Long getCount(List<RootInfo> rootList,List<JoinInfo> joinList,List<FetchInfo> fetchList,List<Filter> filters,Map<String, Date> extractedDateFilters) {
		
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(Long.class);
        
//		criteriaQuery.select(criteriaQuery.from(entityClass));
		
		Map<String,Root> rootMap=new HashMap<String,Root>();
		Map<String,Join> joinMap=new HashMap<String,Join>();
		List<Selection> multiSelectList=new ArrayList<Selection>();
		
		for(int i=0;i<rootList.size();i++){
			RootInfo rInfo=rootList.get(i);
			Root rootTemp = criteriaQuery.from(rInfo.getClassType());
			rootMap.put(rInfo.getName(), rootTemp);
			multiSelectList.add(rootTemp);
//			criteriaQuery.select(rootTemp);
		}
		
		
//		criteriaQuery.multiselect(selections);
//		criteriaBuilder.construct(resultClass, selections);
		
		for(int i=0;i<joinList.size();i++){
			JoinInfo jInfo=joinList.get(i);
			if(jInfo.getCollectionType().equals("set")){
				Root rootTemp=rootMap.get(jInfo.getRootName());
				if(rootTemp!=null){
					SetJoin setJoin = rootTemp.join(rootTemp.getModel().getSet(jInfo.getName()) , jInfo.getJoinType());
					joinMap.put(jInfo.getName(), setJoin);
				}
			}else if(jInfo.getCollectionType().equals("one")){
				Root rootTemp=rootMap.get(jInfo.getRootName());
				if(rootTemp!=null){
					Join join = rootTemp.join(rootTemp.getModel().getSingularAttribute(jInfo.getName()));
					joinMap.put(jInfo.getName(), join);
				}
			}
		}
		
		modifyQueryByFilter(criteriaQuery, filters, extractedDateFilters,rootMap,joinMap);

		
		if(multiSelectList!=null&&multiSelectList.size()>0){
			Root rt=(Root) multiSelectList.get(0);
			criteriaQuery.select(criteriaBuilder.count(rt));
		}
		
		
//		criteriaQuery.multiselect(criteriaBuilder.count(rt));
		
		TypedQuery query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		for (Map.Entry<String, Date> entry : extractedDateFilters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue(),TemporalType.DATE);
		}

		return (Long) query.getSingleResult();
		
	}
    
	private void modifyQueryByFilter(CriteriaQuery<T> criteriaQuery,List<Filter> filters,Map<String, Date> extractedDateFilters,Map<String,Root> rootMap,Map<String,Join> joinMap) {
		if ((criteriaQuery == null) || (filters == null) || (filters.isEmpty())) {
			return;
		}
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		Predicate predicate = criteriaQuery.getRestriction() != null ? criteriaQuery
				.getRestriction() : criteriaBuilder.conjunction();
		Iterator<Filter> iterator = filters.iterator();
		while (iterator.hasNext()) {
			Root root = null;
			Join join =null;
			
			Filter filter = iterator.next();
			if ((filter != null)&& (!StringUtils.isEmpty(filter.getProperty()))) {

				if (!StringUtils.isEmpty(filter.getRoot())) {
					root=rootMap.get(filter.getRoot());
				}

				if (!StringUtils.isEmpty(filter.getJoin())) {
					join=joinMap.get(filter.getJoin());
				}
				
				if(root==null&&join==null){
					continue;
				}
				
				if ((filter.getOperator() == Filter.Operator.eq)&& (filter.getValue() != null)) {
					if ((filter.getIgnoreCase() != null)&& (filter.getIgnoreCase().booleanValue())
							&& ((filter.getValue() instanceof String))
							) {
						if(root!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(criteriaBuilder.lower(root.get(filter.getProperty())),((String) filter.getValue()).toLowerCase()));
						}else if(join!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(criteriaBuilder.lower(join.get(filter.getProperty())),((String) filter.getValue()).toLowerCase()));
						}
					} else {
						if(root!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get(filter.getProperty()),filter.getValue()));
						}else if(join!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(join.get(filter.getProperty()),filter.getValue()));
						}
							
					}
				} else if ((filter.getOperator() == Filter.Operator.ne)
						&& (filter.getValue() != null)) {
					if ((filter.getIgnoreCase() != null)
							&& (filter.getIgnoreCase().booleanValue())
							&& ((filter.getValue() instanceof String))
							) {
						if(root!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.notEqual(criteriaBuilder.lower(root.get(filter.getProperty())),((String) filter.getValue()).toLowerCase()));
						}else if(join!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.notEqual(criteriaBuilder.lower(join.get(filter.getProperty())),((String) filter.getValue()).toLowerCase()));
						}
						
					} else {
						if(root!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.notEqual(root.get(filter.getProperty()),filter.getValue()));
						}else if(join!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.notEqual(join.get(filter.getProperty()),filter.getValue()));
						}
					}
				} else if ((filter.getOperator() == Filter.Operator.gt)
						&& (filter.getValue() != null)) {
					if (filter.getValue() instanceof Date) { // 特殊处理Date

						ParameterExpression<Date> ped = criteriaBuilder.parameter(Date.class, filter.getQueryProperty());
						
						if(root!=null){
							Predicate datePredicate = criteriaBuilder.lessThan(ped,root.get(filter.getProperty()));
							predicate = criteriaBuilder.and(predicate,datePredicate);
							extractedDateFilters.put(filter.getQueryProperty(), (Date) filter.getValue());
						}else if(join!=null){
							Predicate datePredicate = criteriaBuilder.lessThan(ped,join.get(filter.getProperty()));
							predicate = criteriaBuilder.and(predicate,datePredicate);
							extractedDateFilters.put(filter.getQueryProperty(), (Date) filter.getValue());
						}
					} else {
						
						if(root!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.gt(root.get(filter.getProperty()),(Number) filter.getValue()));
						}else if(join!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.gt(join.get(filter.getProperty()),(Number) filter.getValue()));
						}
					}

				} else if ((filter.getOperator() == Filter.Operator.lt)
						&& (filter.getValue() != null)) {
					if (filter.getValue() instanceof Date) { // 特殊处理Date
						ParameterExpression<Date> ped = criteriaBuilder.parameter(Date.class, filter.getQueryProperty());

						if(root!=null){
							Predicate datePredicate = criteriaBuilder.greaterThan(ped, root.get(filter.getProperty()));
							predicate = criteriaBuilder.and(predicate,datePredicate);
							extractedDateFilters.put(filter.getQueryProperty(), (Date) filter.getValue());
						}else if(join!=null){
							Predicate datePredicate = criteriaBuilder.greaterThan(ped, join.get(filter.getProperty()));
							predicate = criteriaBuilder.and(predicate,datePredicate);
							extractedDateFilters.put(filter.getQueryProperty(), (Date) filter.getValue());
						}
					} else {
						if(root!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.lt(root.get(filter.getProperty()),(Number) filter.getValue()));
						}else if(join!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.lt(join.get(filter.getProperty()),(Number) filter.getValue()));
						}
					}
				} else if ((filter.getOperator() == Filter.Operator.ge)
						&& (filter.getValue() != null)) {
					if (filter.getValue() instanceof Date) { // 特殊处理Date
						ParameterExpression<Date> ped = criteriaBuilder.parameter(Date.class, filter.getQueryProperty());
						if(root!=null){
							Predicate datePredicate = criteriaBuilder.lessThanOrEqualTo(ped,root.get(filter.getProperty()));
							predicate = criteriaBuilder.and(predicate,datePredicate);
							extractedDateFilters.put(filter.getQueryProperty(), (Date) filter.getValue());
						}else if(join!=null){
							Predicate datePredicate = criteriaBuilder.lessThanOrEqualTo(ped,join.get(filter.getProperty()));
							predicate = criteriaBuilder.and(predicate,datePredicate);
							extractedDateFilters.put(filter.getQueryProperty(), (Date) filter.getValue());
						}
					} else {
						if(root!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.ge(root.get(filter.getProperty()),(Number) filter.getValue()));
						}else if(join!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.ge(root.get(filter.getProperty()),(Number) filter.getValue()));
						}
					}
				} else if ((filter.getOperator() == Filter.Operator.le)
						&& (filter.getValue() != null)) {
					if (filter.getValue() instanceof Date) { // 特殊处理Date
						ParameterExpression<Date> ped = criteriaBuilder.parameter(Date.class, filter.getQueryProperty());
						
						if(root!=null){
							Predicate datePredicate = criteriaBuilder.greaterThanOrEqualTo(ped,root.get(filter.getProperty()));
							predicate = criteriaBuilder.and(predicate,datePredicate);
							extractedDateFilters.put(filter.getQueryProperty(), (Date) filter.getValue());
						}else if(join!=null){
							Predicate datePredicate = criteriaBuilder.greaterThanOrEqualTo(ped,join.get(filter.getProperty()));
							predicate = criteriaBuilder.and(predicate,datePredicate);
							extractedDateFilters.put(filter.getQueryProperty(), (Date) filter.getValue());
						}
					} else {
						if(root!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.le(root.get(filter.getProperty()),(Number) filter.getValue()));
						}else if(join!=null){
							predicate = criteriaBuilder.and(predicate,criteriaBuilder.le(join.get(filter.getProperty()),(Number) filter.getValue()));
						}
					}
				} else if ((filter.getOperator() == Filter.Operator.like)&& (filter.getValue() != null)&& ((filter.getValue() instanceof String))) {
					
					if(root!=null){
						predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(filter.getProperty()),(String) filter.getValue()));
					}else if(join!=null){
						predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(join.get(filter.getProperty()),(String) filter.getValue()));
					}
					
				} else if ((filter.getOperator() == Filter.Operator.in)
						&& (filter.getValue() != null)) {
					Collection<Object> values = (Collection<Object>) filter.getValue();
					if(root!=null){
						predicate = criteriaBuilder.and(predicate,root.get(filter.getProperty()).in(values.toArray(new Object[] {})));
					}else if(join!=null){
						predicate = criteriaBuilder.and(predicate,join.get(filter.getProperty()).in(values.toArray(new Object[] {})));
					}
				} else if (filter.getOperator() == Filter.Operator.isnull) {
					if(root!=null){
						predicate = criteriaBuilder.and(predicate,root.get(filter.getProperty()).isNull());
					}else if(join!=null){
						predicate = criteriaBuilder.and(predicate,join.get(filter.getProperty()).isNull());
					}
					
				} else if (filter.getOperator() == Filter.Operator.isnotnull) {
					if(root!=null){
						predicate = criteriaBuilder.and(predicate,root.get(filter.getProperty()).isNotNull());
					}else if(join!=null){
						predicate = criteriaBuilder.and(predicate,join.get(filter.getProperty()).isNotNull());
					}
				}
			}
		}
		criteriaQuery.where(predicate);
	}
	
	private void setOrderBy(CriteriaBuilder criteriaBuilder,CriteriaQuery<T> criteriaQuery, Map<String,Root> rootMap,List<OrderByObject> orderByObjList) {
        if ((criteriaBuilder == null) || criteriaQuery == null) {
            return;
        }
        
        List<javax.persistence.criteria.Order> orderByList = new ArrayList<javax.persistence.criteria.Order>();
        if(orderByObjList!=null&&orderByObjList.size()>0){
        	for(int i=0;i<orderByObjList.size();i++){
            	OrderByObject obo=orderByObjList.get(i);
            	if(obo!=null&&!StringUtils.isEmpty(obo.getRootName())&&!StringUtils.isEmpty(obo.getPropertyName())&&obo.getOrderByType()!=null){
            		Root rt=rootMap.get(obo.getRootName());
            		if(rt!=null){
            			if(obo.getOrderByType()==OrderByObject.OrderByType.ASC.getCode()){
            				orderByList.add(criteriaBuilder.asc(rt.get(obo.getPropertyName())));
            			}else if(obo.getOrderByType()==OrderByObject.OrderByType.DESC.getCode()){
            				orderByList.add(criteriaBuilder.desc(rt.get(obo.getPropertyName())));
            			}
            			
            		}
            	}
            }
            criteriaQuery.orderBy(orderByList);
        }
        
    }
	
	

	public List getObjectByJpql(StringBuffer  jpql,Integer currentPage,Integer numberPerPage,String defaultClassAlias,List<ParameterBean> paramList,List<GroupByBean> groupByList,List<OrderByBean> orderByList) throws Exception{
		
		int groupNum =0;
		if(groupByList != null && groupByList.size() >0 ){			
			for(int i=0;i<groupByList.size();i++){				
				GroupByBean groupByBeanTemp = groupByList.get(i);
				if(groupByBeanTemp.getGroupProperty() != null && !"".equals(groupByBeanTemp.getGroupProperty())){
					if(groupNum==0){
						jpql.append(" GROUP BY ");
					}else{
						jpql.append(",");
					}
					if(groupByBeanTemp.getGroupClassAlias()!=null&&!groupByBeanTemp.getGroupClassAlias().equals("")){
						jpql.append(" "+groupByBeanTemp.getGroupClassAlias()+"." + groupByBeanTemp.getGroupProperty());	
					}else{
						jpql.append(" "+defaultClassAlias+"." + groupByBeanTemp.getGroupProperty());	
					}
					groupNum++;
									
				}
				
			}		
			
    	}
		
		int num =0;
		boolean sFlg=false;
		if(orderByList != null && orderByList.size() >0 ){			
			for(int i=0;i<orderByList.size();i++){				
				OrderByBean orderByBeanTemp = orderByList.get(i);
				if(orderByBeanTemp.getSortProperty() != null && !"".equals(orderByBeanTemp.getSortProperty())){
					if(num==0){
						jpql.append(" ORDER BY ");
					}else{
						jpql.append(",");
					}
					if(OrderByBean.SORT_MODE_ASC==orderByBeanTemp.getSortMode()){
						if(orderByBeanTemp.getSortClassAlias()!=null&&!orderByBeanTemp.getSortClassAlias().equals("")) {
							jpql.append(" "+orderByBeanTemp.getSortClassAlias()+"." + orderByBeanTemp.getSortProperty()).append(" ASC ");
						}else {
							jpql.append(" "+defaultClassAlias+"." + orderByBeanTemp.getSortProperty()).append(" ASC ");
						}
							
					}else{
						if(orderByBeanTemp.getSortClassAlias()!=null&&!orderByBeanTemp.getSortClassAlias().equals("")) {
							jpql.append(" "+orderByBeanTemp.getSortClassAlias()+"." + orderByBeanTemp.getSortProperty()).append(" DESC ");
						}else {
							jpql.append(" "+defaultClassAlias+"." + orderByBeanTemp.getSortProperty()).append(" DESC ");
						}
					}
					num++;
									
				}else {
					sFlg=true;
				}
				
			}		
			
    	}else{
    		jpql.append(" ORDER BY "+defaultClassAlias+".createDate DESC ");
    		
		}
		
		Query query=entityManager.createQuery(jpql.toString());
		
		if(paramList!=null&&paramList.size()>0) {
			for (ParameterBean parameterBean : paramList) {
				if(parameterBean.getType()!=null) {
					if(parameterBean.getType() == TemporalType.DATE) {
						query.setParameter(parameterBean.getName(),(Date)parameterBean.getValue(),parameterBean.getType());
					}else if(parameterBean.getType() ==TemporalType.TIME) {
						query.setParameter(parameterBean.getName(),(Calendar)parameterBean.getValue(),parameterBean.getType());
					}else if(parameterBean.getType() ==TemporalType.TIMESTAMP) {
						query.setParameter(parameterBean.getName(),(Date)parameterBean.getValue(),parameterBean.getType());
					}
				}else {
					query.setParameter(parameterBean.getName(),parameterBean.getValue());
				}
			}
			
		}
		
		query.setFirstResult(((currentPage-1)<0?0:(currentPage-1))* numberPerPage);
		query.setMaxResults(numberPerPage);
		
		if(sFlg) {
			query.executeUpdate();
			List result = new ArrayList();
			return result;
		}
		List result = query.getResultList();
		
		return result;
	}

    public Long getObjectCountByJpql(StringBuffer  countJpql,List<ParameterBean> paramList) throws Exception{
		
		
    	
    	Query query=entityManager.createQuery(countJpql.toString());
		
		if(paramList!=null&&paramList.size()>0) {
			for (ParameterBean parameterBean : paramList) {
				if(parameterBean.getType()!=null) {
					if(parameterBean.getType() == TemporalType.DATE) {
						query.setParameter(parameterBean.getName(),(Date)parameterBean.getValue(),parameterBean.getType());
					}else if(parameterBean.getType() ==TemporalType.TIME) {
						query.setParameter(parameterBean.getName(),(Calendar)parameterBean.getValue(),parameterBean.getType());
					}else if(parameterBean.getType() ==TemporalType.TIMESTAMP) {
						query.setParameter(parameterBean.getName(),(Date)parameterBean.getValue(),parameterBean.getType());
					}
				}else {
					query.setParameter(parameterBean.getName(),parameterBean.getValue());
				}
			}
			
		}
		Object obj = query.getSingleResult();
		if(obj instanceof Long) {
			return ((Long)obj).longValue();
		}else if(obj instanceof Integer) {
			return new Long((Integer)obj);
		}
		return ((Long)obj).longValue();
		
	}
    
    public Session getSession() {
    	Session session = entityManager.unwrap(org.hibernate.Session.class);
    	return session;
    }


}
