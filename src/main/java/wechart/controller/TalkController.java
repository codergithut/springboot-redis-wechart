package wechart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tianjian on 2017/8/5.
 */
@RestController
@RequestMapping(value = "/talk")
public class TalkController {

    //根据key查询
//    @RequestMapping(value = "/get", method = RequestMethod.GET)
//    @ResponseBody
//    public Object get() {
//        RedisModel m = new RedisModel();
//        m.setRedisKey("zhangsanKey02");
//        return service.get(m.getRedisKey());
//    }
}
