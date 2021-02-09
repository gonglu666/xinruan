package com.example.xinran.service;

import java.io.*;
import java.util.*;

import com.example.xinran.util.ContentTransferUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.util.StringUtils;

public class Excel {


    public static void main(String[] args) {
        try {

            //获取指定列的值
//            readSpecifyColumns(new File("C:\\Users\\kaonglu\\Desktop\\test\\人员岗位.xls"));

            //获取指定行的值
//            readSpecifyRows(new File("C:\\Users\\kaonglu\\Desktop\\test\\人员岗位.xls"));

            //读取行列的值
//            readRowsAndColums(new File("C:\\Users\\kaonglu\\Desktop\\test\\人员岗位.xls"));

            //将获取到的值写入到TXT或者xls中
            copy_excel(new File("C:\\Users\\kaonglu\\Desktop\\test\\人员岗位需要调整内容.xls"));

            //对比两个文件的不同
//            compareTwoFiles(new File("C:\\Users\\kaonglu\\Desktop\\test\\compare\\mx.xls"),
//                    new File("C:\\Users\\kaonglu\\Desktop\\test\\compare\\es.xls"),0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  	读取指定列
     * @param file
     * @throws Exception
     */
    public static void readSpecifyColumns(File file)throws Exception{
        ArrayList<String> columnList = new ArrayList<String>();
        ArrayList<String> valueList = new ArrayList<String>();
        Workbook readwb = null;
        InputStream io = new FileInputStream(file.getAbsoluteFile());
        readwb = Workbook.getWorkbook(io);
        Sheet readsheet = readwb.getSheet(0);
        int rsColumns = readsheet.getColumns();  //获取表格列数
        int rsRows = readsheet.getRows();  //获取表格行数
        for (int i = 0; i < rsRows; i++) {
            Cell cell_name1 = readsheet.getCell(0, i);  //第一列的值
            columnList.add(cell_name1.getContents());
            Cell cell_name2 = readsheet.getCell(2, i);  //第三列的值，此处需要手动更改，获取不同列的值
            valueList.add(cell_name2.getContents());
        }
        System.out.println(columnList);
        System.out.println(valueList);


    }

    /**
     *   	读取指定行
     * @param file
     * @throws Exception
     */
    public static void readSpecifyRows(File file)throws Exception{
        ArrayList<String> columnList = new ArrayList<String>();
        Workbook readwb = null;
        InputStream io = new FileInputStream(file.getAbsoluteFile());
        readwb = Workbook.getWorkbook(io);
        Sheet readsheet = readwb.getSheet(0);
        int rsColumns = readsheet.getColumns();  //获取表格列数
        int rsRows = readsheet.getRows();  //获取表格行数

        //i指定行，j指定列
        for(int i=0;i<rsRows;i++){
            for (int j = 0; j < rsColumns; j++) {
                Cell cell_name = readsheet.getCell(j, i);  //在这里指定行，此处需要手动更改，获取不同行的值
                columnList.add(cell_name.getContents());
            }
            columnList.add("\n");
        }

        System.out.println(columnList);
    }


    private static void readRowsAndColums(File file) throws BiffException, IOException {
        //1:创建workbook
        Workbook workbook=Workbook.getWorkbook(file);
        //2:获取第一个工作表sheet
        Sheet sheet=workbook.getSheet(0);
        //3:获取数据
        System.out.println("行："+sheet.getRows());
        System.out.println("列："+sheet.getColumns());
        for(int i=0;i<sheet.getRows();i++){
            for(int j=0;j<sheet.getColumns();j++){
                Cell cell=sheet.getCell(j,i);
                System.out.print(cell.getContents()+" ");
            }
            System.out.println();
        }

        //最后一步：关闭资源
        workbook.close();
    }

    /**
     * 	将获取到的值写入到TXT或者xls中
     * @param file
     * @throws Exception
     */
    public static void copy_excel(File file) throws Exception {
        FileWriter fWriter = null;
        PrintWriter out = null;
        String fliename = file.getName().replace(".xls", "");
//        fWriter = new FileWriter(file.getParent()+ "/agetwo.xls");//输出格式为.xls
        fWriter = new FileWriter(file.getParent() + "/" + fliename + ".sql");//输出格式为.txt
        out = new PrintWriter(fWriter);
        InputStream is = new FileInputStream(file.getAbsoluteFile());
        Workbook wb = null;
        wb = Workbook.getWorkbook(is);
        int sheet_size = wb.getNumberOfSheets();
        Sheet sheet = wb.getSheet(0);
        int rows = sheet.getRows();
        int columns = sheet.getColumns();
        for(int i =0;i<rows;i++){
//            for (int j = 0; j < columns; j++) {
//                String cellinfo = sheet.getCell(j, i).getContents();//读取的是第二列数据，没有标题，标题起始位置在for循环中定义
//                out.print(cellinfo+"\t");
//            }
            String cellinfo0 = sheet.getCell(0,i).getContents();
            String cellinfo1 = sheet.getCell(1,i).getContents();
            String cellinfo2 = sheet.getCell(2,i).getContents();
            if (StringUtils.isEmpty(cellinfo0)) {
//                cellinfo9 = "9999";
                continue;
            }
            int us = cellinfo1.indexOf("(兼)");
            int ch = cellinfo1.indexOf("（兼）");

            if (us != -1){
                cellinfo1 = cellinfo1.substring(0,us);
                cellinfo2 = ContentTransferUtil.escape(cellinfo1);
            } else if (ch != -1) {
                cellinfo1 = cellinfo1.substring(0,ch);
                cellinfo2 = ContentTransferUtil.escape(cellinfo1);
            } else {
                continue;
            }


//            String format = "update org_member set sort_id = " + cellinfo9 + " where code = '" +  cellinfo2 +
//                    "' and is_deleted = false and is_enable = true;";


//            String format_mx = "update user_infos as ui join users as us on ui.user_id = us.id join accounts as ac on ac.id = us.account_id set position=\""+cellinfo2+
//                    "\" where  ac.login_name = \""+cellinfo1+"\";";

            String format_mx = "update org_post set name=\""+cellinfo1 +
                    "\", code = \"" + cellinfo2 + "\" where id = "+cellinfo0+";";
            out.println(format_mx);
        }

        out.close();//关闭流
        fWriter.close();
        out.flush();//刷新缓存
        System.out.println("输出完成！");
    }


    /**
     * 比对两个excel差异的数据
     * @param file1
     * @param file2
     */
    private static void compareTwoFiles(File file1,File file2,int index) throws IOException, BiffException {
        //file1 中重复数据
        Map<String,Integer> map1 = new HashMap<>();
        //file1 中不重复数据集合
        Set<String> set1 = new HashSet<>();
        //file2 中重复数据
        Map<String,Integer> map2 = new HashMap<>();
        //file2 中不重复数据集合
        Set<String> set2 = new HashSet<>();

        //统一对比第二列
        InputStream inputStream1 = new FileInputStream(file1.getAbsoluteFile());
        Workbook wb1 = Workbook.getWorkbook(inputStream1);;
        //对比哪个sheet
        Sheet sheet1 = wb1.getSheet(0);
        int rows1 = sheet1.getRows();

        InputStream inputStream2 = new FileInputStream(file2.getAbsoluteFile());
        Workbook wb2 = Workbook.getWorkbook(inputStream2);;
        //对比哪个sheet
        Sheet sheet2 = wb2.getSheet(0);
        int rows2 = sheet2.getRows();

        String cellinfo1 = "";
        for (int i=1 ; i<rows1; i++) {
            if (index == 0) {
                cellinfo1 = sheet1.getCell(1,i).getContents().trim();
            } else if (index == 1) {
                cellinfo1 = sheet1.getCell(13,i).getContents().trim()+"-"+sheet1.getCell(7,i).getContents().trim();
            } else if (index == 2){
                cellinfo1 = sheet1.getCell(1,i).getContents().trim()+"-"+sheet1.getCell(3,i).getContents().trim()+"-"+sheet1.getCell(11,i).getContents().trim()+"-"+sheet1.getCell(17,i).getContents().trim();
            } else {
                cellinfo1 = sheet1.getCell(1,i).getContents().trim()+"-"+sheet1.getCell(3,i).getContents().trim()+"-"+sheet1.getCell(11,i).getContents().trim()+"-"+sheet1.getCell(17,i).getContents().trim()+"-"+sheet1.getCell(24,i).getContents().trim();
//                cellinfo1 = sheet1.getCell(1,i).getContents().trim();
//                cellinfo1 = sheet1.getCell(1,i).getContents().trim()+"-"+sheet1.getCell(3,i).getContents().trim()+"-"+sheet1.getCell(11,i).getContents().trim()+"-"+sheet1.getCell(17,i).getContents().trim();
            }
            if (set1.contains(cellinfo1)) {
                if (map1.containsKey(cellinfo1)) {
                    map1.put(cellinfo1,map1.get(cellinfo1)+1);
                } else {
                    map1.put(cellinfo1,2);
                }
            } else {
                set1.add(cellinfo1);
            }
            cellinfo1 = "";
        }

        String cellinfo2 = "";
        for (int i=1 ; i<rows2; i++) {
            if (index == 0) {
                cellinfo2 = sheet2.getCell(0,i).getContents().trim();
            } else if (index == 1) {
                cellinfo2 = sheet2.getCell(0,i).getContents().trim()+"-"+sheet2.getCell(1,i).getContents().trim();
            } else if (index == 2){
                cellinfo2 = sheet2.getCell(0,i).getContents().trim()+"-"+sheet2.getCell(1,i).getContents().trim()+"-"+sheet2.getCell(5,i).getContents().trim()+"-"+sheet2.getCell(4,i).getContents().trim();
            } else {
                cellinfo2 = sheet2.getCell(2,i).getContents().trim()+"-"+sheet2.getCell(0,i).getContents().trim()+"-"+sheet2.getCell(9,i).getContents().trim()+"-"+sheet2.getCell(10,i).getContents().trim()+"-"+sheet2.getCell(6,i).getContents().trim().substring(0,sheet2.getCell(6,i).getContents().trim().indexOf("("));
//                cellinfo2 = sheet2.getCell(2,i).getContents().trim();
//                cellinfo2 = sheet2.getCell(2,i).getContents().trim()+"-"+sheet2.getCell(0,i).getContents().trim()+"-"+sheet2.getCell(9,i).getContents().trim()+"-"+sheet2.getCell(10,i).getContents().trim();
            }
            if (set2.contains(cellinfo2)) {
                if (map2.containsKey(cellinfo2)) {
                    map2.put(cellinfo2,map2.get(cellinfo2)+1);
                } else {
                    map2.put(cellinfo2,2);
                }
            } else {
                set2.add(cellinfo2);
            }
            cellinfo2 = "";
        }


        Set<String> set1_more = new HashSet();
        Iterator<String> iterator = set1.iterator();
        while (iterator.hasNext()) {

            String next = iterator.next();
            if (set2.contains(next)) {
                set2.remove(next);
            } else {
                set1_more.add(next);
            }


        }

        FileWriter fWriter = null;
        PrintWriter out = null;
        String fliename = "user_mx_zy";
//        fWriter = new FileWriter(file.getParent()+ "/agetwo.xls");//输出格式为.xls
        fWriter = new FileWriter(file1.getParent() + "/" + fliename + ".txt");//输出格式为.txt
        out = new PrintWriter(fWriter);

        if (map1.size() != 0) {
            out.println("移动端有重复数，重复数据为:");
            for (Map.Entry entry : map1.entrySet()) {
                out.println(entry.getKey()+" : "+entry.getValue()+";");
            }
            out.println();
        }
//        if (map2.size() != 0) {
//            out.println("ES有重复数，重复数据为:");
//            for (Map.Entry entry : map2.entrySet()) {
//                out.println(entry.getKey()+" : "+entry.getValue()+";");
//            }
//            out.println();
//        }

        if (!set1_more.isEmpty()) {
            out.println("移动端数据不一致，多出的数据为:");
            Iterator<String> iterator1 = set1_more.iterator();
            while (iterator1.hasNext()) {
                out.println(iterator1.next()+" ;");
            }
            out.println();
        }

        if (!set2.isEmpty()) {

            out.println("ES数据不一致，多出的数据为:");
            Iterator<String> iterator1 = set2.iterator();
            while (iterator1.hasNext()) {
//                String next = iterator1.next();
//                next = next.substring(0,next.indexOf("-"));
//                out.println(next);
                out.println(iterator1.next()+" ;");
            }
            out.println();
        }


        out.close();//关闭流
        fWriter.close();
        out.flush();//刷新缓存
        System.out.println("输出完成！");


    }

}