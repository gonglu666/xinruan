package com.example.xinran.service;

import com.microsoft.schemas.office.office.STInsetMode;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by gonglu
 * 2020/12/13
 */
public class TXT {

    public static void main(String[] args) {
        test();



    }



    private static void test() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\kaonglu\\Desktop\\test\\member.txt")),
                    "UTF-8"));
            String lineTxt = null;
            Set<String> set = new LinkedHashSet<>();
            while ((lineTxt = br.readLine()) != null) {
                lineTxt = lineTxt.trim();
                if (!StringUtils.isEmpty(lineTxt) && lineTxt.indexOf("@") != -1) {
                    set.add(lineTxt.substring(0,lineTxt.indexOf("@")));
                }
            }
            br.close();
            Iterator<String> iterator = set.iterator();
            StringBuilder sb = new StringBuilder();
            while(iterator.hasNext()) {
                sb.append("'");
                sb.append(iterator.next());
                sb.append("',");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }

    private static void getAllCompany() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:\\Users\\kaonglu\\Desktop\\test\\cps\\33.txt")),
                    "UTF-8"));
            String lineTxt = null;
            int i = 1;
            int sum = 0;
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            while ((lineTxt = br.readLine()) != null) {
                if (i ==5 || (i>5 && (i-5) % 8 == 0)) {
                    sb.append("\"");
                    sb.append(lineTxt);
                    sb.append("\",");
                    sum++;
                    if (sum % 15 ==0) {
                        sb.append("\n");
                    }
                }
                i++;
            }
            sb.append("}");
            System.out.println("公司总数："+sum);
            System.out.println(sb);
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    }
}
