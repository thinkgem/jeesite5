package com.jeesite.modules.gxy.web;

import com.jeesite.common.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @auther: 高希阳
 * @Date: 2018/10/25 15:31
 * @Description:
 */
@Controller
@RequestMapping(value = "${frontPath}/gxy")
public class GxyController extends BaseController {

    @RequestMapping(value = "index")
    public String form() {
        return "gxy/gxyindex";
    }

    @RequestMapping(value = "hello")
    public String hello() {
        return "hello";
    }

    public static void main(String[] args) {
        int i=5,k;

//        k=(++i)+(++i)+(i++);//6+7+7
        System.out.println(++i);//6
        System.out.println(++i);//7
        System.out.println(i++);//7
        System.out.println(i);//8
    }


}
