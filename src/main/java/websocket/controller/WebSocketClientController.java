package websocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/9
 * @description
 */
@Controller
@RequestMapping(value = "/talk")
public class WebSocketClientController {

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView get() {
        return new ModelAndView("client");
    }

}
