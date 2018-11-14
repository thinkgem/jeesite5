package com.jeesite.modules.test.web;

import com.jeesite.modules.test.entity.IdCard;
import java.io.*;
import java.util.Properties;

/**
 * @author Administrator
 * @auther 高希阳
 * @Date: 2018/11/14 09:47
 * @Description:输入身份证号，出生年月日、性别、判断其地区
 * https://blog.csdn.net/u014600789/article/details/31739727
 */
public class IdCardParser {

    public static void main(String[] args) {
        //输出目录是项目所在文件夹路径
        //如果打了jar包，输出时jar包所在文件夹路径
        System.out.println(System.getProperty("user.dir"));
        try {
            System.out.println("请输入您要查询的身份证号码：");
            InputStreamReader reader = new InputStreamReader(System.in);
            String str = new BufferedReader(reader).readLine();
            IdCard ic = IdCardParser.parse(str);
            if(ic != null){
                System.out.println(ic);
            }else{
                System.out.println("您输入的有误");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final Properties info;
    static {
        info = new Properties();
        try {
            info.load(new InputStreamReader(new FileInputStream(
                    "web/src/main/resources/config/IdCard2.properties"), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final byte[] factor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
            10, 5, 8, 4, 2, 1 };
    private static final char[] ch = { '1', '0', 'X', '9', '8', '7', '6', '5',
            '4', '3', '2' };

    /**
     * 15位身份证转18位身份证.失败返回null
     *
     * @param $15
     * @return
     */
    private static String $15to18(String $15) {
        if ($15 == null) {
            return null;
        }
        if ($15.length() == 18) {
            return $15;
        }
        if ($15.length() != 15) {
            return null;
        }
        StringBuilder sb = new StringBuilder($15);
        sb.insert(6, "19");// 加入年两位
        int result = 0;
        for (int i = 0; i < sb.length(); i++) {
            result += (Integer.parseInt(sb.charAt(i) + "")) * factor[i];
        }
        return sb.append(ch[result % 11]).toString();// 加入效验码
    }
    /**
     * 验证第18位效验码
     *
     * @param idCardNumber
     * @return
     * 　身份证校验码的计算方法
     * 　　1、将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7－9－10－5－8－4－2－1－6－3－7－9－10－5－8－4－2。
     * 　　2、将这17位数字和系数相乘的结果相加。
     * 　　3、用加出来和除以11，看余数是多少？
     * 　　4、余数只可能有0－1－2－3－4－5－6－7－8－9－10这11个数字。其分别对应的最后一位身份证的号码为1－0－X －9－8－7－6－5－4－3－2。
     * 　　5、通过上面得知如果余数是3，就会在身份证的第18位数字上出现的是9。如果对应的数字是10，身份证的最后一位号码就是罗马数字x。
     * 　　例如：某男性的身份证号码为【53010219200508011x】， 我们看看这个身份证是不是合法的身份证。
     * 　　首先我们得出前17位的乘积和【(5*7)+(3*9)+(0*10)+(1*5)+(0*8)+(2*4)+(1*2)+(9*1)+(2*6)+(0*3)+(0*7)+(5*9)+(0*10)+(8*5)+(0*8)+(1*4)+(1*2)】是189，
     * 然后用189除以11得出的结果是189/11=17----2，也就是说其余数是2。最后通过对应规则就可以知道余数2对应的检验码是X。所以，可以判定这是一个正确的身份证号码。
     * https://zhidao.baidu.com/question/13171070.html
     * http://liufeng063x.iteye.com/blog/656402
     */
    private static boolean check18(String idCardNumber) {
        if (idCardNumber == null || idCardNumber.length() != 18) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < idCardNumber.length() - 1; i++) {
            result += (Integer.parseInt(idCardNumber.charAt(i) + ""))
                    * factor[i];
        }
        int spare=result % 11;
        System.out.println(ch[spare]);
        System.out.println(idCardNumber.charAt(17));
        //a.equalsIgnoreCase(b),忽略字母大小写判断相等
        boolean flag=String.valueOf(ch[spare]).equalsIgnoreCase(String.valueOf(idCardNumber.charAt(17)));

        return flag;
    }
   /**
    * 功能描述：验证出生日期是否合法
    * @author gxy
    * @date 2018/11/14 11:45
    * @return
    */
    private static boolean checkBirthDay(int year, int month, int day) {

        if (year < 1900 || year > 2013) {
            return false;
        }
        if (month > 12 || month < 1) {
            return false;
        }
        if (day < 1) {
            return false;
        }
        byte[] dayOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            dayOfMonth[1] = 29;
        }
        return day <= dayOfMonth[month - 1];
    }


    public static IdCard parse(String idCardNumber) {
        // 检查字符串
        if (!idCardNumber.matches("\\d{15}|\\d{18}|\\d{17}(?i)X")) {
            return null;
        }
        IdCard ic = new IdCard();
        // 15->18
        idCardNumber = $15to18(idCardNumber);
        // 检查效验码
        if (idCardNumber == null || !check18(idCardNumber)) {
            return null;
        }
        // 检查出生年月
        int year = Integer.parseInt(idCardNumber.substring(6, 10));
        int month = Integer.parseInt(idCardNumber.substring(10, 12));
        int day = Integer.parseInt(idCardNumber.substring(12, 14));
        if (!checkBirthDay(year, month, day)) {
            return null;
        }
        ic.setNumber(idCardNumber);
        // 查询身份证归属地
        ic.setAddress(info.getProperty(idCardNumber.substring(0, 6)));
        ic.setBirthday(String.format("%d-%d-%d",year, month, day));
        ic.setSex(1 == (Integer.parseInt(idCardNumber.charAt(16) + "") & 1));
        return ic;
    }
}
