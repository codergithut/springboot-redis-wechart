package wechart.service.impl;

import org.springframework.stereotype.Service;
import wechart.config.CommonValue;
import wechart.model.User;
import wechart.service.IHsahRedisService;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/4
 * @description
 */
@Service(value = "userServiceImpl")
public class UserServiceImpl extends IHsahRedisService<User> implements CommonValue{

    private static final String REDIS_KEY = USRINFO;

    @Override
    protected String getRedisKey() {
        return this.REDIS_KEY;
    }
}
