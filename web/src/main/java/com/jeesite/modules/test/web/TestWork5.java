package com.jeesite.modules.test.web;

import java.util.Calendar;
import java.util.Scanner;

/**
 *
 根据用户输入的身份证号码，从字符串中提取用户的出生年月日，输出用户的生日信息。
 要求：
 身份证号码必须为18位或16位。
 年龄范围在1900到当前日期之间。
 月份必须在1~12之间
 对应的日应该与当前月相符，如4月最多只有30天。注意判断闰年的2月有29天。
 * @author 万星明
 * @version 1.0
 * @time
 * https://blog.csdn.net/qq_19533277/article/details/83039676
 */
public class TestWork5 {

    static Scanner sc = new Scanner(System.in);
    static Calendar c = Calendar.getInstance();

    public static void main(String[] args) {
        System.out.println("请输入身份证：");
        String IDcard = sc.next();
        IDcard(IDcard);
    }

    public static void IDcard(String IDcard) {
        switch(IDcard.length()) {
            case 18:
            case 16:
                String year = IDcard.substring(6, 10);
                if(Integer.decode(year) <1900 && Integer.decode(year)>c.get(Calendar.YEAR)) {
                    System.out.println("年龄不合法");
                }
                String month = IDcard.substring(10,12);

                if(Integer.decode(month)<1 && Integer.decode(month)>12) {
                    System.out.println("身份证不合法");
                }
                String day = IDcard.substring(12,14);

                c.set(Integer.decode(year) ,Integer.parseInt(month), 0);
                if(Integer.decode(day)>c.get(Calendar.DAY_OF_MONTH)) {
                    System.out.println("身份证不合法");
                }
                System.out.println("尊贵的用户，您的生日为："+year+"年"+month+"月"+day+"日");
                break;
            default:
                System.out.println("身份证不合法");
                break;
        }
    }
}
