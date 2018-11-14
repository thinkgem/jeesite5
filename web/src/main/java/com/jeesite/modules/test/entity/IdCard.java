package com.jeesite.modules.test.entity;

/**
 * @auther: 高希阳
 * @Date: 2018/11/14 09:50
 * @Description:身份证实体类
 * https://blog.csdn.net/u014600789/article/details/31739727
 */
public class IdCard {
    private String number;
    private String address;
    private String birthday;
    private boolean sex;//true男.false女

    public IdCard(){

    }

    public IdCard(String number, String address, String birthday, boolean sex) {
        this.number = number;
        this.address = address;
        this.birthday = birthday;
        this.sex = sex;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return String.format("[号码:%s,地址:%s,出生:%s,性别:%s]",number,address,birthday,sex ? '男' : '女');
    }
}
