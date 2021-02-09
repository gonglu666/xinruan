package com.example.xinran.test;

import com.example.xinran.mingxing.http.HttpMethod;
import com.example.xinran.mingxing.http.HttpUtils;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.example.xinran.mingxing.http.HttpUtils.getOKHttpClient;

/**
 * Created by gonglu
 * 2021/2/7
 */
public class HttpTest {

    public static void main(String[] args) {
        OkHttpClient client = getOKHttpClient();
        Map head = new HashMap();
        head.put("token", "ad131e0b-0b59-4565-8081-cb468ad6a568");

        Request request = HttpUtils.createRequest(Headers.of(head), "http://www.loubiqu.com/55_55551/7865129.html", null, HttpMethod.GET);
        Response response = null;
        try {
            response = client.newCall(request).execute();
            String html = response.body().string();
            Document doc = Jsoup.parseBodyFragment(html);
            Element content = doc.getElementById("content");

            System.out.println(content.html().replaceAll("<br>","").replaceAll("&nbsp;",""));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
