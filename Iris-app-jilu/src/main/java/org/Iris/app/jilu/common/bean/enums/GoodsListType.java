package org.Iris.app.jilu.common.bean.enums;

public enum GoodsListType {

	GOODSNAME(0),
	MERCHANT(1);
	
	private int mark;
	
	private GoodsListType(int mark){
		this.mark = mark;
	}
	
	public int mark() {
		return mark;
	}
	
	public static final GoodsListType match(int type) {
		for (GoodsListType listType : GoodsListType.values()) {
			if (listType.mark != type)
				continue;
			return listType;
		}
		return GoodsListType.GOODSNAME;
	}
}
