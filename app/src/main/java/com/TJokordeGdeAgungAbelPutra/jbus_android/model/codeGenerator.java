package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

import java.util.Random;

public class codeGenerator {
    public static String getCode(){
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for(int i = 0; i < 6; i++){
            int digit = random.nextInt(10);
            randomString.append(digit);
        }
        String code = randomString.toString();
        return code;
    }
}
