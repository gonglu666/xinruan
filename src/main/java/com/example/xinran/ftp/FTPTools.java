package com.example.xinran.ftp;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过FTP上传文件
 *
 * @Author lvhaibao
 * @Date 2018/2/11 21:43
 */
public class FTPTools {

    //用于打印日志

    //设置私有不能实例化
    private FTPTools() {

    }

    /**
     * 上传
     *
     * @param hostname ip或域名地址
     * @param port  端口
     * @param username 用户名
     * @param password 密码
     * @param workingPath 服务器的工作目
     * @param inputStream 要上传文件的输入流
     * @param saveName    设置上传之后的文件名
     * @return
     */
    public static boolean upload(String hostname, int port, String username, String password, List workingPath, InputStream inputStream, String saveName) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        //1 测试连接
        if (connect(ftpClient, hostname, port, username, password)) {
            try {
//                boolean b1 = ftpClient.makeDirectory("20210118");
                if(checkAndMakePath(ftpClient,workingPath)){
                    String join = String.join(File.separator, workingPath)+File.separator;
//                    ftpClient.changeWorkingDirectory(join);
                    if (storeFile(ftpClient, saveName, inputStream)) {
                        flag = true;
                        disconnect(ftpClient);
                    }
                }
            } catch (IOException | FtpProtocolException e) {
                e.printStackTrace();
                disconnect(ftpClient);
            }
        }
        return flag;
    }

    /**
     * 断开连接
     *
     * @param ftpClient
     * @throws Exception
     */
    public static void disconnect(FTPClient ftpClient) {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试是否能连接
     *
     * @param ftpClient
     * @param hostname  ip或域名地址
     * @param port      端口
     * @param username  用户名
     * @param password  密码
     * @return 返回真则能连接
     */
    public static boolean connect(FTPClient ftpClient, String hostname, int port, String username, String password) {
        boolean flag = false;
        try {
            //ftp初始化的一些参数
            ftpClient.connect(hostname, port);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            if (ftpClient.login(username, password)) {
                flag = true;
            } else {
                try {
                    disconnect(ftpClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 上传文件
     *
     * @param ftpClient
     * @param saveName        全路径。如/home/public/a.txt
     * @param fileInputStream 要上传的文件流
     * @return
     */
    public static boolean storeFile(FTPClient ftpClient, String saveName, InputStream fileInputStream) {
        boolean flag = false;
        try {
            if (ftpClient.storeFile(saveName, fileInputStream)) {
                flag = true;
                disconnect(ftpClient);
            }
        } catch (IOException e) {
            disconnect(ftpClient);
            e.printStackTrace();
        }
        return flag;
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("C:\\Users\\kaonglu\\Desktop\\test\\123456789.txt");
        List<String> path = new ArrayList();
        path.add("govdoc");
        path.add("upload");
        path.add("2021");
        path.add("01");
        path.add("18");

        System.out.println("------------------------------");


        FTPTools.upload("10.1.67.176",21,"ftpuser","ftpuser",path,inputStream,"ftp.txt");
        System.out.println("success");



    }



    public static boolean checkAndMakePath(FTPClient client,List<String> list) throws IOException, FtpProtocolException {
        if (!list.isEmpty()) {
            String path = "";
            try {
                for (String s : list) {
                    path += File.separator + s;
                    if (!client.changeWorkingDirectory(path)) {
                        client.makeDirectory(path);
                    }
                }
                client.changeWorkingDirectory(path);
                return true;
            } catch (IOException e) {
                System.out.println("创建路径失败"+e);
                return false;
            }
        }
        return true;
    }
}

