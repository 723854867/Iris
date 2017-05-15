package org.Iris.util.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	public static void main(String[] args) {
	}

	public static void exportExcel(String filePath,List<String[]> list) throws Exception {
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook();
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook();
		} else {
			System.out.println("您输入的excel格式不正确");
			return;
		}
		// 创建sheet对象
		Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
		// 循环写入行数据
		for (int i = 0; i < list.size(); i++) {
			Row row = (Row) sheet1.createRow(i);
			// 循环写入列数据
			for (int j = 0; j < 2; j++) {
				Cell cell = row.createCell(j);
				if(j==1){
					cell.setCellValue(Integer.valueOf(list.get(i)[j]));
				}else{
					cell.setCellValue(list.get(i)[j]);
				}
					
			}
		}
		FileOutputStream stream = new FileOutputStream(filePath);
		wb.write(stream);
		stream.close();
		wb.close();
	}

	@SuppressWarnings({ "resource", "deprecation" })
	public static  List<String[]> importExcel(String filePath) throws Exception {
		List<String[]> list = new ArrayList<String[]>();
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
		InputStream stream = new FileInputStream(filePath);
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
			return list;
		}
		// 3.得到Excel工作表对象
		Sheet sheet = wb.getSheetAt(0);
		// for (Row row : sheet1) {
		// for (Cell cell : row) {
		// System.out.print(cell.getStringCellValue()+ " ");
		// }
		// System.out.println();
		// }

		// 总行数
		int trLength = sheet.getLastRowNum();
		// 4.得到Excel工作表的行
		Row row = sheet.getRow(0);
		// 总列数
		int tdLength = row.getLastCellNum();
		for (int i = 0; i < trLength+1; i++) {
			String[] strings = new String[tdLength];
			row = sheet.getRow(i);
			for (int j = 0; j < tdLength; j++) {
				Cell cell = row.getCell(j);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				strings[j]=cell.getStringCellValue().trim().replaceAll("\u00A0","");
			}
			list.add(strings);
		}
		stream.close();
		wb.close();
		return list;
	}
	
	@SuppressWarnings("deprecation")
	public static  List<String[]> importExcelByStream(InputStream stream,String fileType) throws Exception {
		List<String[]> list = new ArrayList<String[]>();
		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
			return list;
		}
		// 3.得到Excel工作表对象
		Sheet sheet = wb.getSheetAt(0);
		// for (Row row : sheet1) {
		// for (Cell cell : row) {
		// System.out.print(cell.getStringCellValue()+ " ");
		// }
		// System.out.println();
		// }

		// 总行数
		int trLength = sheet.getLastRowNum();
		// 4.得到Excel工作表的行
		Row row = sheet.getRow(0);
		// 总列数
		int tdLength = row.getLastCellNum();
		for (int i = 0; i < trLength+1; i++) {
			String[] strings = new String[tdLength];
			row = sheet.getRow(i);
			for (int j = 0; j < tdLength; j++) {
				Cell cell = row.getCell(j);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				strings[j]=cell.getStringCellValue().trim().replaceAll("\u00A0","");
			}
			list.add(strings);
		}
		stream.close();
		wb.close();
		return list;

	}
}
