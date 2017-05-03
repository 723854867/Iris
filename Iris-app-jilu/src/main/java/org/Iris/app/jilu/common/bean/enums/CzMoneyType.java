package org.Iris.app.jilu.common.bean.enums;
/**
 * 充值金额类型
 * @author 樊水东
 * 2017年5月3日
 */
public enum CzMoneyType {

	item0(0,60),
	item1(1,180),
	item2(2,500),
	item3(3,980),
	item4(4,1980),
	item5(5,4880);
	
	private int type;
	private int money;
	
	private CzMoneyType(int type,int money){
		this.type = type;
		this.money = money;
	}
	
	public int type(){
		return this.type;
	}
	
	public int money(){
		return this.money;
	}
	
	public static int getMoney(int type){
		for(CzMoneyType czMoneyType : CzMoneyType.values())
			if(czMoneyType.type == type)
				return czMoneyType.money;
		return 0;
	}
}
