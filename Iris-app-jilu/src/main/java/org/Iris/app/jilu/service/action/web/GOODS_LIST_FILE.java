package org.Iris.app.jilu.service.action.web;

import org.Iris.app.jilu.service.action.MerchantWebAction;
import org.Iris.app.jilu.web.session.MerchantWebSession;
/**
 * 产品列表
 * @author 樊水东
 * 2017年1月13日
 */
public class GOODS_LIST_FILE extends MerchantWebAction{
	String[] strTitles=	{	
			"条形码（必须）",
			"中文名称（必须）",
			"规格（必须）",
			"产品唯一ID（SKU）",
			"英文名称",
			"分类",
			"中文品牌",
			"英文品牌",
			"计量单位",
			"重量",
			"别名",
			"单价",
		};
	String strDes = "注:有“必须”标记的列是必须有内容的；其它列可以根据需要选择输入；添加数据就按这个模板往下加即从“第三行”开始；如果是新增产品，唯一ID（SKU）留空";

	@Override
	protected String execute0(MerchantWebSession session) {
		return merchantWebService.getGoodsListFile(session,strTitles, strDes, session.getMemMerchant());
	}
	
}
