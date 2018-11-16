package com.jeesite.modules;

import com.jeesite.modules.test.entity.TestGxyData;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * @auther: 高希阳
 * @Date: 2018/10/26 17:21
 * @Description:
 * https://blog.csdn.net/simonchi/article/details/55098709
 */
@RestController
public class HelloController {

    private static final Logger log = Logger.getLogger(HelloController.class);

    /**
     * 处理AJAX请求跨域的问题
     * @author Levin
     * @time 2017-07-13
     */
    @RequestMapping(value="/test",method= RequestMethod.GET)
    public String sayHello(){
        return "hello";
    }

    /**
     * 功能描述：https://www.ktanx.com/blog/p/3478
     * @author gxy
     * @date 2018/11/12 14:25
     * @param 
     * @return 
     */
    @RequestMapping("say/{name}")
    public TestGxyData say(@PathVariable String name) {
        TestGxyData message = new TestGxyData();
        message.setName(name);
        message.setCreatDate(new Date());
        return message;
    }

    /**
     * 功能描述：outputQuality,降低图片质量，以outputstream输出流的方式response给浏览器去展示
     * @author gxy
     * @date 2018/11/16 15:55
     * @param
     * @return
     * scale 图片尺寸
     * rotate 图片旋转
     * outputQuality 图片质量
     */
    @RequestMapping(value = "/send")
    public void sendMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("===============HTTP请求入口==============");
        Thumbnails.of(new File("D:\\Documents\\Pictures\\DASDAD.png")).scale(0.5f).rotate(90).outputQuality(0.1)
            .toOutputStream(response.getOutputStream());
    }
}
