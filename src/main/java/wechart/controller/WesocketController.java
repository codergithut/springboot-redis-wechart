package wechart.controller;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/12
 * @description
 */
@Controller
@RequestMapping(value = "/socket")
public class WesocketController {

    //注册添加用户信息
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView test() throws BadHanyuPinyinOutputFormatCombination {
       return new ModelAndView("client");
    }

}
