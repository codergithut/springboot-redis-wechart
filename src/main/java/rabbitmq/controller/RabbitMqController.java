package rabbitmq.controller;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import rabbitmq.service.receive.RecMessageListener;
import rabbitmq.service.receive.ReceivedMessage;
import rabbitmq.service.send.SendMessage;
import rabbitmq.service.send.SendMessageService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:Administrator@gtmap.cn">Administrator</a>
 * @version 1.0, 2017/8/10
 * @description
 */
@RestController
public class RabbitMqController {

    @Autowired
    SendMessage messageService;

    @Autowired
    ReceivedMessage receivedMessage;


    @RequestMapping(value = "/rabbitmq")
    @ResponseBody
    public void testRabbitMq() {
        RecMessageListener recMessageListener = new RecMessageListener();

        Queue queue = new Queue("10000001");

        Queue[] queues = new Queue[] {queue};

        messageService.sendExchangeMsg("exchange", "10000001", "hahah");

        receivedMessage.messageContainer(queues, recMessageListener).start();

    }

}
