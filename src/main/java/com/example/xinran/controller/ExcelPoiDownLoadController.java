package com.example.xinran.controller;

import com.example.xinran.domain.DStudent;
import com.example.xinran.util.ExcelUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaonglu
 * 2020/8/3
 */
@RestController
public class ExcelPoiDownLoadController {


    @RequestMapping("/text/down")
    public void textDown() throws Exception {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        HttpServletRequest request = requestAttributes.getRequest();
        File file  = new File("C:\\Users\\kaonglu\\Desktop\\test\\model.xlsx");
        InputStream inputStream = new FileInputStream(file);
//        InputStream inputStream = null;

        ServletOutputStream outputStream = response.getOutputStream();
        response.reset();
//        response.setContentType("bin");
//        response.setContentType("application/octet-stream");
//        response.setContentType("text/plain; charset=utf-8");
        if (inputStream == null) {
            response.addHeader("File-Exist","false");
            return;
        }
        response.addHeader("File-Exist","true");
        response.addHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

        // 循环取出流中的数据
        byte[] b = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(b)) > 0){
                outputStream.write(b, 0, len);
            }

            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.close();
            inputStream.close();
        }
        return;
    }

    @RequestMapping("/poi/down")
    public void down() throws Exception {

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        HttpServletRequest request = requestAttributes.getRequest();
        String filename = "陪育小学.xls";
        OutputStream out = null;
        try {

            // 1.弹出下载框，并处理中文
            /** 如果是从jsp页面传过来的话，就要进行中文处理，在这里action里面产生的直接可以用
             * String filename = request.getParameter("filename");
             */
            /**
             if (request.getMethod().equalsIgnoreCase("GET")) {
             filename = new String(filename.getBytes("iso8859-1"), "utf-8");
             }
             */

            response.addHeader("content-disposition", "attachment;filename="
                    + java.net.URLEncoder.encode(filename, "utf-8"));

            // 2.下载
            out = response.getOutputStream();

        } catch (Exception e) {
            e.printStackTrace();
        }

        DStudent dStudent = new DStudent();
        dStudent.setSchoolName("陪育小学");
        dStudent.setClasses("一年级2班");
        dStudent.setName("龙大琳");
        dStudent.setFatherPhone("1");
        dStudent.setMotherPhone("2");
        dStudent.setParentPhone("3");

        DStudent dStudent1 = new DStudent();
        dStudent1.setSchoolName("陪育小学");
        dStudent1.setClasses("一年级2班");
        dStudent1.setName("龙大琳2");
        dStudent1.setFatherPhone("11");
        dStudent1.setMotherPhone("21");
        dStudent1.setParentPhone("31");

        List<DStudent> list = new ArrayList<>();
        list.add(dStudent);
        list.add(dStudent1);


        String fileName = "陪育小学";
        report(fileName,list,out);

    }



    public static void report(String fileName,List<DStudent> list,OutputStream out) throws Exception{
//        File f = new File("e:/data/"+fileName+".xlsx");
//        FileOutputStream out = new FileOutputStream(f);
        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            XSSFSheet sheet = wb.createSheet(fileName);
            int rowIndex = 0;
            List<String> titles = new ArrayList<>();
            titles.add("学校");
            titles.add("班级");
            titles.add("学生");
            titles.add("学生父亲联系电话");
            titles.add("学生母亲联系电话");
            titles.add("学生家长联系电话");
            rowIndex = ExcelUtil.writeTitlesToExcel(rowIndex,wb, sheet, titles);
            writeRowsToExcel(wb, sheet, list, rowIndex);
            ExcelUtil.autoSizeColumns(sheet, titles.size() + 1);

            wb.write(out);
        } finally {
            wb.close();

        }
        out.close();
    }


    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<DStudent>  rows, int rowIndex) {
        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setFont(dataFont);
        ExcelUtil.setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new java.awt.Color(0, 0, 0),new DefaultIndexedColorMap()));

        for (DStudent dStudent : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            //学校
            Cell cell0 = dataRow.createCell(0);
            cell0.setCellValue(dStudent.getSchoolName());
            cell0.setCellStyle(dataStyle);
            //班级
            Cell cell1 = dataRow.createCell(1);
            cell1.setCellValue(dStudent.getClasses());
            cell1.setCellStyle(dataStyle);
            //姓名
            Cell cell2 = dataRow.createCell(2);
            cell2.setCellValue(dStudent.getName());
            cell2.setCellStyle(dataStyle);
            //父亲电话
            if(!StringUtils.isEmpty(dStudent.getFatherPhone())){
                Cell cell3 = dataRow.createCell(3);
                cell3.setCellValue(dStudent.getFatherPhone());
                cell3.setCellStyle(dataStyle);
            }

            //母亲电话
            if(!StringUtils.isEmpty(dStudent.getMotherPhone())) {
                Cell cell4 = dataRow.createCell(4);
                cell4.setCellValue(dStudent.getMotherPhone());
                cell4.setCellStyle(dataStyle);
            }
            //家长电话
            if(!StringUtils.isEmpty(dStudent.getParentPhone())) {
                Cell cell5 = dataRow.createCell(5);
                cell5.setCellValue(dStudent.getParentPhone());
                cell5.setCellStyle(dataStyle);
            }
            rowIndex++;
        }
        return rowIndex;
    }



    public static void main(String[] args) throws Exception {
        OutputStream out = new FileOutputStream(new File("C:\\Users\\kaonglu\\Desktop\\test\\haha.xlsx"));
        DStudent dStudent = new DStudent();
        dStudent.setSchoolName("陪育小学");
        dStudent.setClasses("一年级2班");
        dStudent.setName("龙大琳");
        dStudent.setFatherPhone("1");
        dStudent.setMotherPhone("2");
        dStudent.setParentPhone("3");

        DStudent dStudent1 = new DStudent();
        dStudent1.setSchoolName("陪育小学");
        dStudent1.setClasses("一年级2班");
        dStudent1.setName("龙大琳2");
        dStudent1.setFatherPhone("11");
        dStudent1.setMotherPhone("21");
        dStudent1.setParentPhone("31");

        List<DStudent> list = new ArrayList<>();
        list.add(dStudent);
        list.add(dStudent1);


        String fileName = "陪育小学";
        report(fileName,list,out);
    }
}
