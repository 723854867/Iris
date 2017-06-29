package org.Iris.app.jilu.service.realm;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.common.Beans;
import org.Iris.app.jilu.common.bean.form.CfgGoodsForm;
import org.Iris.app.jilu.common.bean.form.CfgGoodsListForm;
import org.Iris.app.jilu.common.bean.form.GoodsPagerForm;
import org.Iris.app.jilu.common.bean.form.Pager;
import org.Iris.app.jilu.common.model.AccountType;
import org.Iris.app.jilu.common.model.Env;
import org.Iris.app.jilu.service.realm.jms.JmsService;
import org.Iris.app.jilu.service.realm.merchant.Merchant;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.storage.domain.MemCustomer;
import org.Iris.app.jilu.storage.domain.MemMerchant;
import org.Iris.app.jilu.storage.domain.SysPage;
import org.Iris.app.jilu.storage.redis.CommonKeyGenerator;
import org.Iris.app.jilu.storage.redis.WebKeyGenerator;
import org.Iris.app.jilu.web.JiLuCode;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.exception.IllegalConstException;
import org.Iris.core.service.bean.Result;
import org.Iris.core.service.locale.ICode;
import org.Iris.util.common.KeyUtil;
import org.Iris.util.lang.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class MerchantWebService implements Beans{
	
	@Resource
	private JmsService jmsService;

	/**
	 * 生成验证码: 验证码和 账号对应
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public String generateCaptcha(AccountType type, String account) {
		String captchaKey = WebKeyGenerator.accountCaptchaKey(type, account);
		String captchaCountKey = WebKeyGenerator.accountCaptchaCountKey(type, account);
		
		// 生成验证码并且缓存验证码
		String captcha = KeyUtil.randomCaptcha(AppConfig.getCaptchaDigit());
		long flag = luaOperate.recordCaptcha(captchaKey, captchaCountKey, captcha, 
				AppConfig.getCaptchaLifeTime(), AppConfig.getCaptchaCountMaximum(), AppConfig.getCaptchaCountLifeTime());
		if (-1 == flag) 
			return Result.jsonError(JiLuCode.CAPTCHA_GET_CD);
		if (-2 == flag)
			return Result.jsonError(JiLuCode.CAPTCHA_COUNT_LIMIT);
		Env env = AppConfig.getEnv();
		switch (env) {
		case LOCAL:											// 测试环境下直接返回验证码
		case TEST:
			//jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess(captcha);				
		case ONLINE:										// 线上环境需要发送短信
			jmsService.sendCaptchaMessage(type, account, captcha);
			return Result.jsonSuccess();					
		default:
			return Result.jsonError(ICode.Code.SYSTEM_ERROR);
		}
	}
	
	/**
	 * 登陆
	 * 
	 * @param type
	 * @param account
	 * @param captch
	 * @return
	 * @throws Exception 
	 */
	public String login(AccountType type, String account, String captch,HttpServletRequest request){ 
		switch (type) {
		case MOBILE:
		case EMAIL:
			if (!verifyCaptch(type, account, captch))
				return Result.jsonError(JiLuCode.CAPTCHA_ERROR);
			break;
		default:
			throw IllegalConstException.errorException(JiLuParams.TYPE);
		}
		
		Merchant merchant = merchantService.getMerchantByAccount(type, account);
		if(merchant == null)
			return Result.jsonError(JiLuCode.ACOUNT_IS_NOT_EXIST);
		HttpSession session = request.getSession();
		session.setAttribute("merchant",merchant.getMemMerchant());
		return Result.jsonSuccess(merchant.getMemMerchant());
		
	}
	/**
	 * 检查验证码
	 * 
	 * @param type
	 * @param account
	 * @return
	 */
	public boolean verifyCaptch(AccountType type, String account, String captch) {
		return luaOperate.delIfEquals(WebKeyGenerator.accountCaptchaKey(type, account), captch);
	}
	
	/**
	 * 产品列表
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String getGoodsList(int page, int pageSize,String zhName,String alias,String goodsCode,MemMerchant memMerchant) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		map.put("zhName", zhName);
		map.put("alias", alias);
		map.put("goodsCode", goodsCode);
		map.put("source", memMerchant.getMerchantId());
		long count = cfgGoodsMapper.getCount(map);
		if(count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<CfgGoods> list = cfgGoodsMapper.getGoodsList(map);
		for(CfgGoods cfgGoods :list)
			cfgGoods.setUpdateTime(DateUtils.getUTCDate((long)cfgGoods.getUpdated()*1000));
		return Result.jsonSuccess(new Pager<CfgGoods>(count, list));
	}
	
	/**
	 * 插入商品
	 * 
	 * @param memGoods
	 */
	public String insertGoods(CfgGoods memGoods) {
		cfgGoodsMapper.insert(memGoods);
		redisOperate.hmset(memGoods.redisKey(), memGoods);
		return Result.jsonSuccess(memGoods);
	}

	/**
	 * 查看商品信息
	 * 
	 * @param goodsId
	 * @return
	 */
	public String getGoodsInfo(long goodsId) {
		CfgGoods goods = getGoodsById(goodsId);
		if (goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		return Result.jsonSuccess(goods);
	}
	
	/**
	 * 更新商品
	 * 
	 * @param memGoods
	 */
	public String updateGoods(long goodsId, String zhName, String usName, String goodsFormat, String classification,
			String zhBrand, String usBrand, String unit, float weight, String alias, String barcode, String sku,
			float unitPrice) {
		CfgGoods memGoods = getGoodsById(goodsId);
		if (memGoods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		if (zhName != null)
			memGoods.setZhName(zhName);
		if (usName != null)
			memGoods.setUsName(usName);
		if (goodsFormat != null)
			memGoods.setGoodsFormat(goodsFormat);
		if (classification != null)
			memGoods.setClassification(classification);
		if (zhBrand != null)
			memGoods.setZhBrand(zhBrand);
		if (usBrand != null)
			memGoods.setUsBrand(usBrand);
		if (unit != null)
			memGoods.setUnit(unit);
		if (weight != 0)
			memGoods.setWeight(weight);
		if (alias != null)
			memGoods.setAlias(alias);
		if (barcode != null)
			memGoods.setBarcode(barcode);
		if (sku != null)
			memGoods.setSku(sku);
		if (unitPrice != 0)
			memGoods.setUnitPrice(unitPrice);
		int time = DateUtils.currentTime();
		memGoods.setUpdated(time);
		cfgGoodsMapper.update(memGoods);
		redisOperate.hmset(memGoods.redisKey(), memGoods);
		return Result.jsonSuccess(new GoodsPagerForm(memGoods));
	}
	
	/**
	 * 删除商品
	 * 
	 * @param goodsId
	 * @return
	 */
	public String removeGoods(long goodsId) {
		CfgGoods goods = getGoodsById(goodsId);
		if (goods == null)
			throw IllegalConstException.errorException(JiLuParams.GOODS_ID);
		cfgGoodsMapper.delete(goods);
		redisOperate.del(goods.redisKey());
		return Result.jsonSuccess();
	}
	
	/**
	 * 获取商品
	 * @param goodsId
	 * @return
	 */
	public CfgGoods getGoodsById(long goodsId) {
		String key = CommonKeyGenerator.getMemGoodsKey(goodsId);
		CfgGoods goods = redisOperate.hgetAll(key, new CfgGoods(goodsId));
		if (goods != null)
			return goods;
		goods = cfgGoodsMapper.getGoodsById(goodsId);
		if (null != goods)
			redisOperate.hmset(key, goods);
		return goods;
	}
	
	
	public String getGoodsListFile(IrisSession session, String[] strTitles, String strDes, MemMerchant memMerchant){
		
		Map<String, Object> map = new HashMap<>();
		map.put("start", 0);
		map.put("pageSize", 65535);
		map.put("zhName", "");
		map.put("alias", "");
		map.put("goodsCode", "");
		map.put("source", memMerchant.getMerchantId());
		long count = cfgGoodsMapper.getCount(map);
		if(count == 0)
			return null;
		List<CfgGoods> list = cfgGoodsMapper.getGoodsList(map);

		HttpServletResponse response = session.getResponse();
	    
		String filePath= session.getRequest().getSession().getServletContext().getRealPath("/WEB-INF/download/产品列表.xlsx");
		File f = new File(filePath);
	    if (!f.exists()) {
	    	try {
				response.sendError(404, "File not found!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      return null;
	    }
	    
	    
		//if(ret.length() == 0)
	    if (list == null)
		{
			filePath = session.getRequest().getSession().getServletContext().getRealPath("/WEB-INF/download/产品模板.xlsx");
			f = new File(filePath);
			if (!f.exists()) {
				try {
					response.sendError(404, "File not found!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		
		//	正常输出
		try {
			
			Workbook wb = new XSSFWorkbook();	//	xlsx
			// 创建sheet对象
			Sheet sheet1 = (Sheet) wb.createSheet("Sheet1");
			// 循环写入行数据
			Row row;
			Cell cell;
			
			//	第一行标题
			row = (Row) sheet1.createRow(0);
			for (int i = 0; i < strTitles.length; i++){
				cell = row.createCell(i);
				cell.setCellValue(strTitles[i]);
			}
			
			//	第二行备注
			row = (Row) sheet1.createRow(1);
			cell = row.createCell(0);
			cell.setCellValue(strDes);
			
			//	第三行开始是数据
			int i = 2;
			for (CfgGoods cfgGoods :list) {
				row = (Row) sheet1.createRow(i);				

				int j = 0;
				//条形码（必须）	中文名称（必须）	规格（必须）	产品唯一ID	英文名称	分类	中文品牌	英文品牌	计量单位	重量	别名	单价
				
				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getGoodsCode());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getZhName());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getGoodsFormat());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getGoodsId() );
				
				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getUsName());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getClassification());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getZhBrand());
				
				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getUsBrand());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getUnit());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getWeight());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getAlias());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getUnitPrice());
				
				i++;
			}
			
			FileOutputStream stream = new FileOutputStream(filePath);
			wb.write(stream);
			stream.close();
			wb.close();
			
			BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		    byte[] buf = new byte[1024];
		    int len = 0;
		    
		    response.setContentType(session.getRequest().getSession().getServletContext().getMimeType(f.getName()));
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(f.getName(), "UTF-8"));
			
		    OutputStream out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			
			//out.flush();
    		//out.close();
			br.close();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	    return null;
	}
	
	
	
	
	/**
	 * 获取菜单列表 sam
	 * 
	 * @return
	 */
	public String getMenuList() {
		return Result.jsonSuccess(webMenuMapper.getSysMenuList());
	}
	
	/**
	 * 获取当前页面父页面路径 sam
	 * 
	 * @return
	 */
	public String GetParentPagePath(String curPagePath) {
		String parentPagePath = "";
		SysPage page = webMenuMapper.getPageByPagePath(curPagePath);
		
		if (page !=null && page.getParentpageid() > 0) {
			page = webMenuMapper.getPageByPageId(page.getParentpageid());
			parentPagePath = page.getUrl();
		} else {
			parentPagePath = page.getUrl();
		}

		return Result.jsonSuccess(parentPagePath);
	}

	public String getMerchantCustomersByNameOrPhone(int page, int pageSize, String name, String phone,MemMerchant memMerchant) {
		Map<String, Object> map = new HashMap<>();
		map.put("start", (page-1)*pageSize);
		map.put("pageSize", pageSize);
		map.put("name", name);
		map.put("mobile", phone);
		map.put("merchantId", memMerchant.getMerchantId());
		long count = memCustomerMapper.getMerchantCustomersCount(map);
		if(count == 0)
			return Result.jsonSuccess(Pager.EMPTY);
		List<MemCustomer> list = memCustomerMapper.getMerchantCustomersByMap(map);
		return Result.jsonSuccess(new Pager<MemCustomer>(count, list));
	}
}
