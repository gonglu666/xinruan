package com.example.xinran.zhiyuan.business;

import com.example.xinran.mingxing.http.HttpMethod;
import com.example.xinran.mingxing.http.HttpUtils;
import com.example.xinran.mingxing.http.JacksonUtil;
import com.example.xinran.util.ExcelUtil;
import okhttp3.Headers;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by gonglu
 * 2020/12/2
 */
public class GetDatas {

    private static final String basePath = "C:\\Users\\kaonglu\\Desktop\\test";

    private static final String filePath = "C:\\Users\\kaonglu\\Desktop\\test\\致远数据.xlsx";

    private static Map headers = new HashMap<>();

    private static String [] companyCodes = {"1000000001","1000001000","1000001001","1000001002","1000001003","1000001004","1000001005","1000001006","1000001007","1000001011","1000001017","1000001022","1000001012","1000001013","1000001019",
            "1000001023","1000001024","1000001021","1000001008","1000001009","1000001014","1000001015","1000001018","1000001020","1000001233","1000001228","1000001254","1000001277","1000001314","1000001488",
            "1000001284","1000001289","1000001432","1000001434","1000001401","1000001384","1000001487","1000001454","1000001493","1000001494","1000001495","1000001496","1000001497","1000001499","1000001486",
            "1000001500","1000001501","1000001503","1000001269","1000001433","1000001491","1000001505","1000001147","1000001489","1000001099","1000001148","1000001456","1000001457","1000001490","1000001149",
            "1000001100","1000001026","1000001402","1000001455","1000001262","1000001150","1000001237","1000001389","1000001102","1000001537","1000001027","1000001538","1000001151","1000001101","1000001243",
            "1000001400","1000001264","1000001316","1000001536","1000001153","1000001266","1000001103","1000001531","1000001290","1000001154","1000001051","1000001106","1000001317","1000001268","1000001105",
            "1000001534","1000001155","1000001048","1000001291","1000001272","1000001161","1000001263","1000001540","1000001108","1000001049","1000001403","1000001410","1000001541","1000001157","1000001230",
            "1000001327","1000001107","1000001050","1000001231","1000001406","1000001542","1000001270","1000001318","1000001156","1000001286","1000001109","1000001367","1000001418","1000001088","1000001232",
            "1000001159","1000001114","1000001544","1000001325","1000001227","1000001404","1000001044","1000001543","1000001120","1000001235","1000001121","1000001407","1000001234","1000001045","1000001547",
            "1000001545","1000001236","1000001426","1000001028","1000001116","1000001238","1000001110","1000001246","1000001417","1000001029","1000001111","1000001388","1000001405","1000001030","1000001239",
            "1000001031","1000001385","1000001413","1000001112","1000001409","1000001032","1000001135","1000001131","1000001033","1000001416","1000001132","1000001034","1000001358","1000001408","1000001035",
            "1000001125","1000001337","1000001036","1000001423","1000001129","1000001330","1000001324","1000001134","1000001415","1000001037","1000001038","1000001320","1000001040","1000001331","1000001366",
            "1000001039","1000001319","1000001041","1000001052","1000001390","1000001391","1000001386","1000001057","1000001392","1000001055","1000001387","1000001397","1000001393","1000001515","1000001010",
            "1000001273","1000001322","1000001335","1000001069","1000001326","1000001016","1000001217","1000001514","1000001310","1000001458","1000001526","1000001274","1000001469","1000001275","1000001046",
            "1000001546","1000001257","1000001478","1000001533","1000001229","1000001329","1000001321","1000001382","1000001332","1000001276","1000001312","1000001508","1000001368","1000001315","1000001309",
            "1000001082","1000001313","1000001323","1000001513","1000001043","1000001394","1000001328","1000001333","1000001431","1000001506","1000001071","1000001047","1000001247","1000001473","1000001396",
            "1000001188"};

    static {
        headers.put("token","76999626-19ef-40a7-9a66-4b1a1602f5d1");
    }

    public static void main(String[] args) throws Exception {

        OutputStream outputStream = new FileOutputStream(new File(filePath));

//        Workbook sheets = WorkbookFactory.create(inputStream);
//        XSSFWorkbook sheets = (XSSFWorkbook) WorkbookFactory.create(inputStream);
        XSSFWorkbook sheets = new XSSFWorkbook();
        getAllCompany(sheets,"公司数据");
//        getAllDepartment(sheets,"部门数据");
//        getAllUser(sheets,"人员数据");

        sheets.write(outputStream);

        outputStream.close();
        sheets.close();

    }

    private static void getAllCompany(XSSFWorkbook workbook,String sheetName) throws Exception {

        String getCompanyByPareentCode = "http://10.152.16.66/seeyon/rest/orgAccount/code/";
        List<Map> list = new ArrayList<>();
        for (String companyCode : companyCodes) {
            String company = HttpUtils.sendByJson(Headers.of(headers), getCompanyByPareentCode+companyCode, JacksonUtil.toString(null), HttpMethod.GET);
            list.add(JacksonUtil.convertValue(company, Map.class));
        }
        report(workbook,sheetName,list);
        System.out.println("company ... data ... success ... ");
    }

    private static void getAllDepartment(XSSFWorkbook workbook,String sheetName) {
        String getDepartByUnitID = "http://10.152.16.66/seeyon/rest/orgDepartments/";
        String deparement = HttpUtils.sendByJson(Headers.of(headers), getDepartByUnitID+"-2762169630460063928", JacksonUtil.toString(null), HttpMethod.GET);
        List<Map> list = JacksonUtil.convertList(deparement, Map.class);
        System.out.println(list.size());
    }

    private static void getAllUser(XSSFWorkbook workbook,String sheetName) {
        String getUsersByUnitID = "http://10.152.16.66/seeyon/rest/orgMembers/";
        String user = HttpUtils.sendByJson(Headers.of(headers), getUsersByUnitID+"-2762169630460063928", JacksonUtil.toString(null), HttpMethod.GET);
        List<Map> list = JacksonUtil.convertList(user, Map.class);
        System.out.println(list.size());
    }





    public static void report(XSSFWorkbook workbook, String fileName, List<Map> list) throws Exception {
        XSSFSheet sheet = workbook.getSheet(fileName);
        if (sheet == null) {
            sheet = workbook.createSheet(fileName);
        }
        int rowIndex = 0;
        List<String> titles = new ArrayList<>();
        Iterator iterator = list.get(0).keySet().iterator();
        while (iterator.hasNext()) {
            titles.add((String) iterator.next());
        }
        rowIndex = ExcelUtil.writeTitlesToExcel(rowIndex,workbook, sheet, titles);
        writeRowsToExcel(workbook, sheet, list, rowIndex);
        ExcelUtil.autoSizeColumns(sheet, titles.size() + 1);
    }

    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<Map> rows, int rowIndex) {
        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setFont(dataFont);
        ExcelUtil.setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new java.awt.Color(0, 0, 0),new DefaultIndexedColorMap()));
        Set set = rows.get(0).keySet();
        for (Map map : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            Iterator iterator = set.iterator();
            int colunm = 0;
            while (iterator.hasNext()) {
                Cell cell = dataRow.createCell(colunm);
                cell.setCellValue(String.valueOf(map.get(iterator.next())));
                cell.setCellStyle(dataStyle);
                colunm++;
            }
            rowIndex++;
        }
        return rowIndex;
    }
}

