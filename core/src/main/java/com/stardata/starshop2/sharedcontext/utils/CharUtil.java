package com.stardata.starshop2.sharedcontext.utils;

import java.util.Random;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/21 18:27
 */
public class CharUtil {
    /**
     * 获取随机字符串
     *
     * @param num 随机字符串的长度
     * @return String 随机字符串
     */
    public static String getRandomString(Integer num) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789@#$%^&*()!-+=~;:<>/,.?'";
        Random random = new Random();
        Random randomCase = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            int upperCase = randomCase.nextInt(2);
            char c = base.charAt(number);
            if (upperCase > 0) {
                sb.append((c+"").toUpperCase());
            }
            else {
                sb.append(c);
            }

        }
        return sb.toString();
    }

    /**
     * 获取随机字符串
     * @param num 随机数字串长度
     * @return String 随机数字串
     */
    public static String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
