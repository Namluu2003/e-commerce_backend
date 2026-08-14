package com.poly.app.domain.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ObjectResponse {
    private boolean isSuccess = false;
    private String mess;
    private Object data;

    public <T> ObjectResponse(T obj) {
        processReponseObject(obj);
    }

    public void processReponseObject(Object obj) {
        if (obj != null) {
            this.isSuccess = true;
            this.data = obj;
        }
    }
}