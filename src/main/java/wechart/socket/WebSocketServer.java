package wechart.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wechart.model.WebSocketMessage;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/9
 * @description
 */
@Service
public class WebSocketServer {

    @Autowired
    MyWebSocket myWebSocket;



}
