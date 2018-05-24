package com.earthworm.rabbitmq.consumer;

import com.earthworm.rabbitmq.event.MsgListenerImpl1;
import com.earthworm.rabbitmq.event.MsgListenerImpl2;
import com.earthworm.rabbitmq.event.MsgListenerImpl3;
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
 * @Date: 2018/5/22 14:06
 * @version: v1.0.0
 * @Type:
 * @Desc: TestConsumerHandler测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestConsumerHandler {

    // slf4j日志相关
    private Logger logger = LoggerFactory.getLogger(TestConsumerHandler.class);

    @Autowired
    private ConsumerHandler consumer;

    @Autowired
    private MsgListenerImpl1 listener1;

    @Autowired
    private MsgListenerImpl2 listener2;

    @Autowired
    private MsgListenerImpl3 listener3;


    /**
     * 一个队列，一个消息监听者
     */

    @Test
    public void test1Queue1Listener() {

        logger.info("一个队列，一个消息监听者 。。。。测试 ！");
        try {
            consumer.consumerMessage("qyzh.test.EQ1",listener1);
            // 等待3秒
            Thread.sleep(3000);
        } catch (IOException |TimeoutException |InterruptedException e) {
            e.printStackTrace();
            logger.error("出错了，详情为：",e);
        }
        logger.info("测试完成");

    }

    /**
     * 一个队列，多个消息监听者
     */
    @Test
    public void test1Queue2Listeners() {
        logger.info("一个队列，多个消息监听者.......");
        try {
            consumer.consumerMessage("qyzh.test.EQ1",listener1,listener2,listener3);
            logger.info("................................................");

            // 等待40秒
            Thread.sleep(40000);
        } catch (IOException |TimeoutException |InterruptedException e) {
            e.printStackTrace();
            logger.error("出错了，详情为：",e);
        }
        logger.info("测试完成");

    }


    /**
     * 两个队列，一个消息监听者
     */
    @Test
    public void test2Queues1Listener() {

        logger.info("两个队列，一个消息监听者 ！");
        try {
            consumer.consumerMessage("qyzh.test.EQ1",listener1);
            logger.info("------------------------------------------------");
            consumer.consumerMessage("qyzh.test.EQ2",listener1);
            // 等待3秒
            Thread.sleep(3000);
        } catch (IOException |TimeoutException |InterruptedException e) {
            e.printStackTrace();
            logger.error("出错了，详情为：",e);
        }
        logger.info("测试完成");

    }
}
