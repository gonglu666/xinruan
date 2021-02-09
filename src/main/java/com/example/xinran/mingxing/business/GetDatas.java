package com.example.xinran.mingxing.business;

import com.example.xinran.domain.DStudent;
import com.example.xinran.mingxing.http.HttpMethod;
import com.example.xinran.mingxing.http.HttpUtils;
import com.example.xinran.mingxing.http.JacksonUtil;
import com.example.xinran.util.ExcelUtil;
import okhttp3.Headers;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;

/**
 * Created by gonglu
 * 2020/11/29
 */
public class GetDatas {

    private static final String basePath = "C:\\Users\\kaonglu\\Desktop\\test";

    private static final String filePath = "C:\\Users\\kaonglu\\Desktop\\test\\敏行数据.xlsx";

    private static Map headers = new HashMap();

    static {
        headers.put("Authorization","Bearer 8Ng639KqDLs6stkE4JmLOrezgRzHIxxnwGvP4XlyXmSs_YpM");
    }





    public static void main(String[] args) throws Exception {
//        OutputStream company = new FileOutputStream(new File("C:\\Users\\kaonglu\\Desktop\\test\\敏行数据-公司.xlsx"));
//        OutputStream department = new FileOutputStream(new File("C:\\Users\\kaonglu\\Desktop\\test\\敏行数据-部门.xlsx"));
//        OutputStream user = new FileOutputStream(new File("C:\\Users\\kaonglu\\Desktop\\test\\敏行数据-人员.xlsx"));
//        OutputStream test = new FileOutputStream(new File("C:\\Users\\kaonglu\\Desktop\\test\\敏行数据-测试.xlsx"));
//        getAllCompanys(company);
//        getAllDepartments(department);
//        getAllUsers(user);

//        File file1 = new File(basePath);
//        if (!file1.exists()) {
//            file1.mkdirs();
//        }
//
//        File file2 = new File(filePath);
//        if (!file2.exists()) {
//            file2.createNewFile();
//        }

//        InputStream inputStream = new FileInputStream(new File(filePath));
        OutputStream outputStream = new FileOutputStream(new File(filePath));

//        Workbook sheets = WorkbookFactory.create(inputStream);
//        XSSFWorkbook sheets = (XSSFWorkbook) WorkbookFactory.create(inputStream);
        XSSFWorkbook sheets = new XSSFWorkbook();
//        getAllCompanys(sheets,"公司数据");
//        getAllDepartments(sheets,"部门数据");
        getAllUsers(sheets,"人员数据");

        sheets.write(outputStream);

        outputStream.close();
        sheets.close();



    }


    public static void getAllCompanys(XSSFWorkbook workbook,String sheetName) throws Exception {
        String getAllCompany = "http://10.152.16.51/api/v1/departments/by_dept_type?limit=2000&dept_type=company&all_dept=true";
        String companys = HttpUtils.sendByJson(Headers.of(headers), getAllCompany, JacksonUtil.toString(null), HttpMethod.GET);
        Map map = JacksonUtil.convertValue(companys, Map.class);
        List<Map> list = (List<Map>) map.get("items");
        report(workbook,sheetName,list);
        System.out.println("company ... data ... success ... ");
    }

    public static void getAllDepartments(XSSFWorkbook workbook,String sheetName) throws Exception {
        String getAllDepartments = "http://10.152.16.51/api/v1/departments/by_dept_type?limit=2000&dept_type=depart&all_dept=true";
        String departments = HttpUtils.sendByJson(Headers.of(headers), getAllDepartments, JacksonUtil.toString(null), HttpMethod.GET);
        Map map = JacksonUtil.convertValue(departments, Map.class);
        List<Map> list = (List<Map>) map.get("items");
        report(workbook,sheetName,list);
        System.out.println("department ... data ... success ... ");
    }

    public static void getAllUsers(XSSFWorkbook workbook,String sheetName) throws Exception {
        String getAllFirstCompany = "http://10.152.16.51/api/v1/departments?limit=200&all_dept=ture&only_dept=ture";
        String firstLevelCompany = HttpUtils.sendByJson(Headers.of(headers), getAllFirstCompany, JacksonUtil.toString(null), HttpMethod.GET);
        Map departmentMap = JacksonUtil.convertValue(firstLevelCompany, Map.class);
        List<Map> userList = new ArrayList<>(3000);

        List<Map> departmentList = (List<Map>) departmentMap.get("items");

//        ref_id -> 1000000001
//        departmentList = new ArrayList<>();
//               Map map1 = new HashMap();
//               map1.put("ref_id","1000000001");
//        departmentList.add(map1);




        Set<String> set = new HashSet<>();
        for (Map map : departmentList) {
            String getUsersByDepartID = "http://10.152.16.51/api/v1/departments/all_users?dept_code=" +
                    map.get("ref_id")+"&include_subdivision=true&include_detail=true";
            String userByDepartId = HttpUtils.sendByJson(Headers.of(headers), getUsersByDepartID, JacksonUtil.toString(null), HttpMethod.GET);
            List<Map> list = JacksonUtil.convertList(userByDepartId, Map.class);
            for (Map m : list) {
                if (set.contains(m.get("login_name"))) {
                    continue;
                }
                set.add((String) m.get("login_name"));
                userList.add(m);
            }
        }
        report(workbook,sheetName,userList);
        System.out.println("user ... data ... success ... ");
    }

    public static void report(XSSFWorkbook workbook,String fileName, List<Map> list) throws Exception {
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
