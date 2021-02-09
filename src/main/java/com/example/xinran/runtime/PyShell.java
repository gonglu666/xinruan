package com.example.xinran.runtime;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by gonglu
 * 2021/1/10
 */
public class PyShell {

    private static final String  shellName = "seeyon.py";

    public static void main(String[] args) throws IOException {
        String path = new File(PyShell.class.getClass().getResource("/").getPath()).getPath();
        StringBuilder sb = new StringBuilder();
        sb.append("cmd /k ");
        sb.append(path.substring(0,1));
        sb.append(": && cd ");
        sb.append(path);
        sb.append(" && python ");
        sb.append(shellName);
        Runtime rt = Runtime.getRuntime();
        rt.exec(sb.toString());
        System.out.println("over");
    }







    private void printPath() throws IOException {
        // 第一种：获取类加载的根路径
        File f = new File(this.getClass().getResource("/").getPath());
        System.out.println(f);

        // 获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  D:\git\daotie\daotie\target\classes\my
        File f2 = new File(this.getClass().getResource("").getPath());
        System.out.println(f2);

        // 第二种：获取项目路径    D:\git\daotie\daotie
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile);


        // 第三种：  file:/D:/git/daotie/daotie/target/classes/
        URL xmlpath = this.getClass().getClassLoader().getResource("");
        System.out.println(xmlpath);


        // 第四种： D:\git\daotie\daotie
        System.out.println(System.getProperty("user.dir"));
        /*
         * 结果： C:\Documents and Settings\Administrator\workspace\projectName
         * 获取当前工程路径
         */

        // 第五种：  获取所有的类路径 包括jar包的路径
        System.out.println(System.getProperty("java.class.path"));
    }
}
