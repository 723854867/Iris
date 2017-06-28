package org.Iris.app.jilu.service.action.backstage;

import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.web.session.IrisSession;

/**
 * 后台产品列表
 * @author 蔡威威
 * 2017年6月19日
 */
public class GOODS_LIST_FILE extends BackstageAction {
	String[] strTitles=	{	"条形码（必须）",
							"中文名称（必须）",
							"规格（必须）",
							"英文名称",
							"分类",
							"中文品牌",
							"英文品牌",
							"计量单位",
							"重量",
							"别名",
							"sku",
							"单价",
						};
	String strDes = "注:有“必须”标记的列是必须有内容的；其它列可以根据需要选择输入；添加数据就按这个模板往下加即从“第三行”开始";
	@Override
	protected String execute0(IrisSession session){
		
	    return backstageService.getGoodsListFile(session, strTitles, strDes);
	}
}
