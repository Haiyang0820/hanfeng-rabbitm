package com.earthworm.rabbitmq.producer;

import com.earthworm.rabbitmq.MessageDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/22 14:26
 * @version: v1.0.0
 * @Type:
 * @Desc: PublisherHandler 测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestPublisherHandler {
    // slf4j日志相关
    private Logger logger = LoggerFactory.getLogger(TestPublisherHandler.class);

    @Autowired
    private PublisherHandler publisher;

    /**
     * 因为使用Direct模式Exchange，所以相当于给指定队列发送消息
     * 往一个队列中发送一条消息
     */
    @Test
    public void testMessageToQueue() {
        logger.info("测试：发送一条消息到一个队列中.......");

        // 封装消息实体对象
        MessageDetail messageDetail = new MessageDetail("192.168.1.1", "host1", "I am StrMsg...");

        publishMsg(messageDetail);

    }

    /**
     * 因为使用Direct模式Exchange，所以相当于给指定队列发送消息
     * 往一个队列中发送多条消息
     */
    @Test
    public void testMessagesToQueue() {
        logger.info("测试：发送多条消息到一个队列中......");

        MessageDetail messageDetail = null;
        for (int i = 1; i < 11; i++) {
            messageDetail = new MessageDetail("192.168.1." + i, "host:" + i, "I am Msg i==.." + i);
            publishMsg(messageDetail);
        }
    }

    private void publishMsg(MessageDetail messageDetail) {
        try {
            publisher.publishMessage(messageDetail, "qyzh.test.EQ1", "rkey1");
            logger.info("消息发送成功");
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
            logger.error("出错了，详情为：", e);
        }
    }


    /**
     * 因为使用Direct模式Exchange，所以相当于给指定队列发送消息
     * 发送一条消息到两个队列
     */
    @Test
    public void testMessageToQueues() {
        // 后期用pub/sub方式来做
        logger.info("测试：发送一条消息到两个队列.....");
        // 封装消息实体对象
        MessageDetail messageDetail = new MessageDetail("192.168.1.3", "host3", "I am StrMsg 3...");
        try {
            publisher.publishMessage(messageDetail, "qyzh.test.EQ1", "rkey1");
            publisher.publishMessage(messageDetail, "qyzh.test.EQ2", "rkey2");
            logger.info("消息发送成功");
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
            logger.error("出错了，详情为：", e);
        }

    }


    /**
     * 因为使用Direct模式Exchange，所以相当于给指定队列发送消息
     * 发送多条消息到两个队列
     */
    @Test
    public void testMessagesToQueues() {
        logger.info("测试：发送多条消息到两个队列.......");
        String exchange1 = "qyzh.test.EQ1";
        String exchange2 = "qyzh.test.EQ2";

        String rkey1 = "rkey1";
        String rkey2 = "rkey2";

        MessageDetail messageDetail = null;
        for (int i = 1; i < 11; i++) {
            messageDetail = new MessageDetail("192.168.1." + i, "host:" + i, "I am Msg i==.." + i);
            try {
                publisher.publishMessage(messageDetail, exchange1, rkey1);
                publisher.publishMessage(messageDetail, exchange2, rkey2);
            } catch (TimeoutException | IOException e) {
                e.printStackTrace();
                logger.error("testMessagesToQueues出错了，详情：", e);
            }
        }

    }
}
