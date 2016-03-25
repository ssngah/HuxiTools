package com.huxi.huxiutils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuan on 15/11/23.
 */
public class Validation {

    public static boolean isIdCardNoValid(String idCardNo) {
        return idCardNo.length() >= 15 && idCardNo.length() <= 18;
    }

    public static boolean isEmailValid(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        Pattern p = Pattern.compile("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");
        Matcher m = p.matcher(email);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isPhoneValid(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        }
        if (TextUtils.equals(phone, "13800138000")) {
            return false;
        }
        Pattern p = Pattern.compile("^[1][3-8]\\d{9}$");
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            return false;
        }
        return true;
    }

    /**
     * @return If the password matches regular expression :^[A-Za-z0-9]{6,16}$
     */
    public static boolean isPasswordValid(String password) {
        return isPasswordValid(password, "^[A-Za-z0-9]{6,16}$");
    }

    /**
     * @param regex The password regular expression you are using now.
     * @return If the password matches regular expression you pass in.
     */
    public static boolean isPasswordValid(String password, String regex) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * @param maxLength text所允许的最大字符长度。每个中文字符长度为2，其他字符为1。如最多只允许6个中文，则允许的最大长度为6*2个
     */
    public static boolean isTextLenthLegal(String text, int maxLength) {
        if (TextUtils.isEmpty(text)) {
            return false;
        } else if (length(text) <= maxLength) {
            return true;
        } else {
            return false;
        }
    }

    private static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
