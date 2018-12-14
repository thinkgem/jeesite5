package com.jeesite.modules.gxy.web;

import com.jeesite.common.web.BaseController;
import org.apache.log4j.Logger;
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

    private static final Logger log = Logger.getLogger(GxyController.class);

    @RequestMapping(value = "index")
    public String form() {
        return "gxy/gxyindex";
    }

    @RequestMapping(value = "webgl")
    public String webgl() {
        return "gxy/webgl";
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
