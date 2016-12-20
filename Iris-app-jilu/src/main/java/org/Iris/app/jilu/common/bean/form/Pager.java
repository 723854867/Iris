package org.Iris.app.jilu.common.bean.form;

import java.util.Collections;
import java.util.List;

/**
 * 分页视图
 * 
 * @author ahab
 */
public class Pager<T> {
	
	@SuppressWarnings("all")
	public static final Pager EMPTY					= new Pager(0, Collections.EMPTY_LIST);

	private long total; 				// 总页数
	private List<T> list;
	
	public Pager(long total, List<T> list) {
		this.total = total;
		this.list = list;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
}
