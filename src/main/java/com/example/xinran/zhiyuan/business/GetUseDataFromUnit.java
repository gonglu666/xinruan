package com.example.xinran.zhiyuan.business;

import com.example.xinran.mingxing.http.HttpMethod;
import com.example.xinran.mingxing.http.HttpUtils;
import com.example.xinran.mingxing.http.JacksonUtil;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import okhttp3.Headers;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by gonglu
 * 2021/2/7
 */
public class GetUseDataFromUnit {

    static Map<String,String> codeId = null;
    static Map<String,String> codeType = null;
    static Map<String,String> codePath = null;
    static Map<String,String> pathCode = null;

    private static final String ucDepart = "C:\\Users\\kaonglu\\Desktop\\test\\uc_department.xls";
    private static final String orgUnits = "C:\\Users\\kaonglu\\Desktop\\test\\org_unit.xls";

    private static final String getUserFDepart = "http://localhost/seeyon/rest/orgMembers/department/";
    private static final String deleteDepart = "http://localhost/seeyon/rest/orgDepartment/code/";
    private static final String deleteCompany = "http://localhost/seeyon/rest/orgAccount/";
    private static final String deleteUser = "http://localhost/seeyon/rest/orgMember/";
    private static final String getDutyFUnit = "http://localhost/seeyon/rest/orgLevels/all/";
    private static final String getPosiFUnit = "http://localhost/seeyon/rest/orgPosts/";
    private static final String deletePosition = "http://localhost/seeyon/rest/orgPost/";
    private static final String deleteDuty = "http://localhost/seeyon/rest/orgLevel/";

    private static Map headers = new HashMap<>();

    static {
        headers.put("token","4ab0a3a2-a06f-4ff4-962e-49f946f4e8e9");
    }

    public static void main(String[] args) throws IOException, BiffException {
        long start = System.currentTimeMillis();
        Set initSet = getAllUsingDepartments(new File(ucDepart),2,0,true);
        Map allDepartments = getAllDepartments(new File(orgUnits), initSet);
        handleDatas(allDepartments);
        long end = System.currentTimeMillis();
        System.out.println("此次清除人员共花费了："+(end-start)/1000);

    }

    private static Set getAllUsingDepartments(File file,int row,int column,boolean add) throws IOException, BiffException {
        Set<String> set = new HashSet();

        InputStream inputStream = new FileInputStream(file.getAbsoluteFile());
        Workbook wb = Workbook.getWorkbook(inputStream);;
        Sheet sheet = wb.getSheet(0);
        int rows = sheet.getRows();

        for (int i = row; i<rows; i++) {
            set.add(sheet.getCell(column,i).getContents().trim());
            if (add) {
                set.add(sheet.getCell(2,i).getContents().trim());
            }
        }
        System.out.println("需要保留的初始部门和公司总数为："+set.size());
        return set;
    }

    /**
     * 从所有数据中保留初始数据，然后按照层级删除数据
     * @param file
     * @param initSet
     * @return
     * @throws IOException
     * @throws BiffException
     */
    private static Map getAllDepartments(File file,Set<String> initSet) throws IOException, BiffException {
        Set<String> needRemainDeparts = new HashSet();
        Set<String> needRemainUnits = new HashSet();
        Set<String> initDeparts = new HashSet<>();
        Set<String> intiUnits = new HashSet<>();
        codeId = new HashMap();
        codeType = new HashMap();
        codePath = new HashMap();
        pathCode = new HashMap();
        Map<String,Map> result = new HashMap<>();
        InputStream inputStream = new FileInputStream(file.getAbsoluteFile());
        Workbook wb = Workbook.getWorkbook(inputStream);
        Sheet sheet = wb.getSheet(0);
        int rows = sheet.getRows();
        for (int i = 0; i<rows; i++) {
            if (StringUtils.isEmpty(sheet.getCell(3,i).getContents().trim())) {
                continue;
            }
            if (codeId.containsKey(sheet.getCell(3,i).getContents().trim())) {
                System.out.println("该部门编码重复，重复的编码为："+sheet.getCell(3,i).getContents().trim()+", id为："+sheet.getCell(0,i).getContents().trim());
            }
            codeId.put(sheet.getCell(3,i).getContents().trim(),sheet.getCell(0,i).getContents().trim());
            codeType.put(sheet.getCell(3,i).getContents().trim(),sheet.getCell(5,i).getContents().trim());
            codePath.put(sheet.getCell(3,i).getContents().trim(),sheet.getCell(7,i).getContents().trim());
            pathCode.put(sheet.getCell(7,i).getContents().trim(),sheet.getCell(3,i).getContents().trim());
        }

        System.out.println("数据库中查询总部门数量为："+codeId.size());
        Iterator<String> iterator = initSet.iterator();
        while(iterator.hasNext()) {
            String next = iterator.next();
            if (!codeType.containsKey(next)) {
                System.out.println("初始化总数据不存在数据库中："+next);
                continue;
            }
            if (codeType.get(next).equals("Account")) {
                intiUnits.add(next);
            } else if(codeType.get(next).equals("Department")) {
                initDeparts.add(next);
            }
        }
        System.out.println("需要保留的初始部门个数："+initDeparts.size());
        System.out.println("需要保留的初始公司个数："+intiUnits.size());

        Iterator<String> depart = initDeparts.iterator();
        while(depart.hasNext()) {
            String next = depart.next();
            while( !codeType.get(next).equals("Account")) {
                needRemainDeparts.add(next);
                String path = codePath.get(next);
                next = pathCode.get(path.substring(0,path.length()-4));
            }

        }
        System.out.println("需要保留的总部门个数为（包括父部门）："+needRemainDeparts.size());



        //显示需要关联的部门
//        needRemainDeparts.removeAll(initDeparts);
//        if (needRemainDeparts.size()>0) {
//            System.out.println("需要关联的父级部门如下：");
//            needRemainDeparts.forEach(
//                    s -> System.out.println(s)
//            );
//        }


        Iterator<String> units = intiUnits.iterator();
        while(units.hasNext()) {
            String next = units.next();
            while( !codePath.get(next).equals("0000")) {
                needRemainUnits.add(next);
                String path = codePath.get(next);
                next = pathCode.get(path.substring(0,path.length()-4));
            }

        }
        System.out.println("需要保留的总公司个数为（包括父公司）："+needRemainUnits.size());

        //显示需要关联的公司
//        needRemainUnits.removeAll(intiUnits);
//        if (needRemainUnits.size()>0) {
//            System.out.println("需要关联的父级公司如下：");
//            needRemainUnits.forEach(
//                    s -> System.out.println(s)
//            );
//        }

        //从总数据中移除需要保留的数据
        needRemainDeparts.forEach(x -> {codePath.remove(x);codeId.remove(x);});
        needRemainUnits.forEach(x -> {codePath.remove(x);codeId.remove(x);});

        System.out.println("剩余需要删除的的公司+部门数据："+codePath.size());

        result.put("path",codePath);
        result.put("type",codeType);

        return result;
    }

    private static void handleDatas(Map<String,Map<String,String>> result) {
        Map<Integer,HashMap> unitLevel = new HashMap();
        Map<Integer,HashMap> departLevel = new HashMap();

        Map<String,String> codePath = result.get("path");
        Map<String,String> codeType = result.get("type");
        Set<Integer> unit = new TreeSet<Integer>(Comparator.reverseOrder());
        Set<Integer> depart = new TreeSet<Integer>(Comparator.reverseOrder());

        Iterator iterator = codePath.keySet().iterator();
        while (iterator.hasNext()) {
            String next = (String) iterator.next();
            //区分公司，并分级
            if (codeType.get(next).equals("Account")) {
                if (codePath.get(next).equals("0000")) {
                    continue;
                }
                int i = codePath.get(next).length() / 4;
                if (unitLevel.get(i) == null) {
                    unitLevel.put(i,new HashMap<String,String>());
                }
                unitLevel.get(i).put(next,codePath.get(next));
            } else if (codeType.get(next).equals("Department")) {
                int i = codePath.get(next).length() / 4;
                if (departLevel.get(i) == null) {
                    departLevel.put(i,new HashMap<String,String>());
                }
                departLevel.get(i).put(next,codePath.get(next));
            }
        }

        System.out.println("公司分级个数："+unitLevel.size());
        System.out.println("部门分级个数："+departLevel.size());

        unit.addAll(unitLevel.keySet());
        depart.addAll(departLevel.keySet());
    }



}
