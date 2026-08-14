package com.poly.app.infrastructure.constant;

import com.poly.app.infrastructure.util.PropertiesReader;

import lombok.Getter;

@Getter
public enum Message {

    SUCCESS("Success");


    // viết các lỗi ở đâ
//    API_ERROR(PropertiesReader.getProperty(PropertyKeys.API_ERROR)),
//    PRODUCT_DETAIL_NOT_EXIST(PropertiesReader.getProperty(PropertyKeys.PRODUCT_DETAIL_NOT_EXIST));
    private static System PropertiesReader;
    private String message;

    Message(String message) {
        this.message = message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
