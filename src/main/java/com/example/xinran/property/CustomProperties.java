package com.example.xinran.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by gonglu
 * 2020/12/23
 */
@Component
@ConfigurationProperties("default.xinran")
@PropertySource("classpath:xinran.properties")
public class CustomProperties {

    private String pi;

    private String nai;

    public String getPi() {
        return pi;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public String getNai() {
        return nai;
    }

    public void setNai(String nai) {
        this.nai = nai;
    }
}
