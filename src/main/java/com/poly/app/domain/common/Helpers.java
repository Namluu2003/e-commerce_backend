package com.poly.app.domain.common;

import java.util.UUID;

public class Helpers {

    public static String genCodeUUID() {
        return UUID.randomUUID().toString();
    }

}
