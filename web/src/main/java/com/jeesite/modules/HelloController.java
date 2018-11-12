package com.jeesite.modules;

import com.jeesite.modules.test.entity.TestGxyData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @auther: 高希阳
 * @Date: 2018/10/26 17:21
 * @Description:
 */
@RestController
public class HelloController {

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
}
