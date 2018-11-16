package com.jeesite.test;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @auther: 高希阳
 * @Date: 2018/11/16 15:23
 * @Description:
 * https://blog.csdn.net/qiaoxin666/article/details/79036161
 */
public class PicTest {

    private static final float PIC_SIZE = 500 * 1024;

    private static final Logger log = Logger.getLogger(PicTest.class);

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Administrator\\Desktop\\test.jpg");
        try {
            //对图片放大3倍
            Thumbnails.of(new FileInputStream(file)).scale(3.0).toFile(new File("C:\\Users\\Administrator\\Desktop\\yyyyy.jpg"));
            //图片进行10倍压缩
            Thumbnails.of(new FileInputStream(file)).scale(0.1f).toFile(new File("C:\\Users\\Administrator\\Desktop\\xxxxx.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 图片大于150KB进行大小压缩
     */
    public void test1(){
        //加载图片源
        File file = new File("D:/pictest/Koala.jpg");
        File file1 = new File("D:/pictest/Koala1.jpg");
        Thumbnails.Builder<File> builder = Thumbnails.of(file);

        if(file.length()> PIC_SIZE){
            float qualify = PIC_SIZE / file.length();
            try {
                builder.scale(qualify).toFile(file1);
            } catch (IOException e) {
                System.out.println("图片压缩失败:" + e);
            }
        }

    }

    /**
     * 图片大于500KB进行宽和高压缩
     */
    public void test2(){
        //加载图片源
        File file = new File("D:/pictest/Koala.jpg");
        File file1 = new File("D:/pictest/Koala.jpg");
        Thumbnails.Builder<File> builder = Thumbnails.of(file);

        if(file.length()> PIC_SIZE){
            try {
                builder.size(500, 500).toFile(file1);
            } catch (IOException e) {
                System.out.println("图片压缩失败:" + e);
            }
        }
    }

    /**
     * 图片不是jpg格式转换成jpg格式
     */
    public void test3(){
        File file = new File("D:/pictest/Koala.png");
        File file1 = new File("D:/pictest/Koala.png");
        Thumbnails.Builder<File> builder = Thumbnails.of(file);

        if(!file.getName().toLowerCase().endsWith("jpg")){
            try {
                builder.scale(1f).outputFormat("jpg").toFile(file1);
            } catch (IOException e) {
                System.out.println("图片压缩失败:" + e);
            }
        }
    }
}
