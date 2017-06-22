package org.Iris.app.jilu.service.action.backstage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Iris.app.jilu.common.AppConfig;
import org.Iris.app.jilu.service.action.BackstageAction;
import org.Iris.app.jilu.storage.domain.CfgGoods;
import org.Iris.app.jilu.web.JiLuParams;
import org.Iris.app.jilu.web.session.IrisSession;
import org.Iris.core.service.bean.Result;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * 后台产品列表
 * @author 蔡威威
 * 2017年6月19日
 */
public class GOODS_LIST_FILE extends BackstageAction {

	@Override
	protected String execute0(IrisSession session){
		
		//String zhName = "";//session.getKVParamOptional(JiLuParams.ZH_NAME);
		//String alias = "";//session.getKVParamOptional(JiLuParams.ALIAS);
		//String goodsCode = "";//session.getKVParamOptional(JiLuParams.GOODS_CODE);
		//String ret = backstageService.getGoodsListFileString(zhName,alias,goodsCode);
		List<CfgGoods> list = backstageService.getGoodsList();

	    HttpServletResponse response = session.getResponse();
	    
		String filePath= session.getRequest().getSession().getServletContext().getRealPath("/WEB-INF/download/产品模板.xlsx");
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
			Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
			// 循环写入行数据
			int i = 0;
			for (CfgGoods cfgGoods :list) {
				Row row = (Row) sheet1.createRow(i);

				int j = 0;
				Cell cell;
				//条形码（必须）	中文名称（必须）	规格（必须）	英文名称	分类	中文品牌	计量单位	重量	别名	sku	barcode	单价
				
				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getGoodsCode());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getZhName());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getGoodsFormat());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getUsName());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getClassification());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getZhBrand());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getUnit());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getWeight());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getAlias());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getSku());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getBarcode());

				cell = row.createCell(j++);
				cell.setCellValue(cfgGoods.getUnitPrice());
				
				i++;
			}
			
			FileOutputStream stream = new FileOutputStream(filePath);
			wb.write(stream);
			stream.close();
			wb.close();
			
//			FileWriter fw = new FileWriter(f);
//	    	PrintWriter pw = new PrintWriter(fw);
//	    	pw.print(ret);
//	    	fw.close();
//	    	pw.close();
	    	
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
}
