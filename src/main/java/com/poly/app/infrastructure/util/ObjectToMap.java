package com.poly.app.infrastructure.util;

import java.util.HashMap;
import java.util.Map;

public class ObjectToMap {
    public static Map<String, Object> objectValuesToMap(Object obj) {
        Map<String, Object> map = new HashMap<>();

        // Lấy tất cả các trường của đối tượng
        for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);  // Đảm bảo truy cập vào trường private
                map.put(field.getName(), field.get(obj));  // Thêm tên trường và giá trị vào Map
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }
}
