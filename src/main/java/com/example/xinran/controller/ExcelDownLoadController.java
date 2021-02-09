package com.example.xinran.controller;

import com.example.xinran.common.GlResponse;
import com.example.xinran.config.AsyncConfig;
import com.example.xinran.domain.Arcserve;
import jxl.Workbook;
import jxl.format.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaonglu
 * 2020/8/3
 */

@RestController
public class ExcelDownLoadController {

    @Autowired
    private AsyncConfig asyncConfig;

    @RequestMapping("/down")
    public GlResponse down(){
         ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
         final HttpServletResponse response = requestAttributes.getResponse();




                String filename = "地址列表.xls";
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




                try {

                    // 写到服务器上
                    String path = "F:/filetest/"  + filename;

                    // 写到服务器上（这种测试过，在本地可以，放到linux服务器就不行）
                    //String path =  this.getClass().getClassLoader().getResource("").getPath()+"/"+filename;

                    // 创建写工作簿对象
                    WritableWorkbook workbook = Workbook.createWorkbook(out);
                    // 工作表
                    WritableSheet sheet = workbook.createSheet("地址列表", 0);
                    // 设置字体;
                    WritableFont font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);

                    WritableCellFormat cellFormat = new WritableCellFormat(font);
                    // 设置背景颜色;
                    cellFormat.setBackground(Colour.WHITE);
                    // 设置边框;
                    cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
                    // 设置文字居中对齐方式;
                    cellFormat.setAlignment(Alignment.CENTRE);
                    // 设置垂直居中;
                    cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
                    // 分别给1,5,6列设置不同的宽度;
                    sheet.setColumnView(0, 15);
                    sheet.setColumnView(4, 60);
                    sheet.setColumnView(5, 35);
                    // 给sheet电子版中所有的列设置默认的列的宽度;
                    sheet.getSettings().setDefaultColumnWidth(20);
                    // 给sheet电子版中所有的行设置默认的高度，高度的单位是1/20个像素点,但设置这个貌似就不能自动换行了
                    // sheet.getSettings().setDefaultRowHeight(30 * 20);
                    // 设置自动换行;
                    cellFormat.setWrap(true);


                    sheet.mergeCells(0, 0, 5, 0); //合并单元格mergeCells(a,b,c,d) a 单元格的列号,b 单元格的行号,c 从单元格[a,b]起，向左合并到c列,d 从单元格[a,b]起，向下合并到d行
                    String cellContent1 = "个人就诊信息详情表";
                    Label content = new Label(0, 0, cellContent1, cellFormat); //单元格内容
                    sheet.addCell(content);
                    sheet.setRowView(0, 600);  //设置行高

                    // 单元格
                    Label label0 = new Label(0, 1, "ID", cellFormat);
                    Label label1 = new Label(1, 1, "省", cellFormat);
                    Label label2 = new Label(2, 1, "市", cellFormat);
                    Label label3 = new Label(3, 1, "区", cellFormat);
                    Label label4 = new Label(4, 1, "详细地址", cellFormat);
                    Label label5 = new Label(5, 1, "创建时间", cellFormat);

                    sheet.addCell(label0);
                    sheet.addCell(label1);
                    sheet.addCell(label2);
                    sheet.addCell(label3);
                    sheet.addCell(label4);
                    sheet.addCell(label5);



                    // 给第二行设置背景、字体颜色、对齐方式等等;
                    WritableFont font2 = new WritableFont(WritableFont.ARIAL, 14, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
                    WritableCellFormat cellFormat2 = new WritableCellFormat(font2);
                    // 设置文字居中对齐方式;
                    cellFormat2.setAlignment(Alignment.CENTRE);
                    // 设置垂直居中;
                    cellFormat2.setVerticalAlignment(VerticalAlignment.CENTRE);
                    cellFormat2.setBackground(Colour.WHITE);
                    cellFormat2.setBorder(Border.ALL, BorderLineStyle.THIN);
                    cellFormat2.setWrap(true);

                    // 记录行数
                    int n = 2;

                    // 查找所有地址
                    List<Arcserve> list = new ArrayList<Arcserve>();
                    list.add(new Arcserve("gonglu",31,"15330013218",30000,"beijing","bg"));
                    list.add(new Arcserve("wsh",31,"15330013218",30000,"beijing","bg"));
                    list.add(new Arcserve("gw",31,"15330013218",30000,"beijing","bg"));
                    if (list != null && list.size() > 0) {

                        // 遍历
                        for (Arcserve a : list) {



                            Label lt0 = new Label(0, n, a.getName() + "", cellFormat2);
                            Label lt1 = new Label(1, n, String.valueOf(a.getAge()), cellFormat2);
                            Label lt2 = new Label(2, n, a.getPhone(), cellFormat2);
                            Label lt3 = new Label(3, n, String.valueOf(a.getSale()), cellFormat2);
                            Label lt4 = new Label(4, n, a.getAddr(), cellFormat2);
                            Label lt5 = new Label(5, n, a.getDream(), cellFormat2);

                            sheet.addCell(lt0);
                            sheet.addCell(lt1);
                            sheet.addCell(lt2);
                            sheet.addCell(lt3);
                            sheet.addCell(lt4);
                            sheet.addCell(lt5);

                            n++;
                        }
                    }

                    //开始执行写入操作
                    workbook.write();
                    //关闭流
                    workbook.close();

                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return GlResponse.getResponse("success","call success",new String("kkkk"));
     }









        // 第六步，下载excel




}
