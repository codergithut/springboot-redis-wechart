package wechart.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/22
 * @description
 */
@RestController
@RequestMapping(value = "/log4j")
public class Log4jController {

    private static final Logger logger = LoggerFactory.getLogger(Log4jController.class);

    @RequestMapping(value = "")
    @ResponseBody
    public void log4JTest() throws InterruptedException {

        boolean flag = true;

        while(flag) {
            Date date = new Date();
            if(date.getTime()%3==0) {
                logger.error("this is error i make " + date.getTime());
            }
            if(date.getTime()%3==1) {
                logger.warn("this is warn i make " + date.getTime());
            }
            if(date.getTime()%3==2) {
                logger.info("this is info i make" +date.getTime());
            }

            Thread.sleep(70000l);
        }

    }




}
