package com.earthworm.rabbitmq.consumer;

import com.earthworm.rabbitmq.event.IMsgEventListener;
import com.earthworm.rabbitmq.event.MsgEventSourceManager;
import com.earthworm.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/4 11:22
 * @version: v1.0.0
 * @Type: ConsumerHandler.java
 * @Desc: 消费处理者类
 */
@Component
public class ConsumerHandler {
    // slf4j日志相关
    private static Logger logger = LoggerFactory.getLogger(Consumer.class);

    // 自动注入RabbitMQUtils
    @Autowired
    private RabbitMqUtils mqUtils;

    /**
     * 消费者消费消息的方法
     *
     * @param queueName ：队列名
     * @return 消息实例
     * @throws IOException         :异常
     * @throws TimeoutException：异常
     */
    public void consumerMessage(String queueName, IMsgEventListener... listeners) throws IOException, TimeoutException {

        // 判断listeners是否为null,如果为null,直接return，结束方法。
        if(listeners == null){
            logger.info("listeners为空......");
            return;
        }

        // 实例化事件源
        MsgEventSourceManager manager = new MsgEventSourceManager();
        // 遍历可变参数数组
        for (IMsgEventListener listener :listeners) {
            manager.addMsgEventListener(listener);
        }

        //开启手动确认消息消费
        boolean autoAck = false;

        try {
            // 获取信道
            Channel channel = mqUtils.getChannel();

            //同一时刻服务器只会发一条消息给消费者
            channel.basicQos(1);
            logger.info("开启手动消息确认机制.....................");

            //autoAck参数:false为手动应答，true为自动应答
            channel.basicConsume(queueName, autoAck, "myConsumerTag", new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {

                    String messageStr = new String(body, "utf-8");


                    // 定义此类相关
                    logger.info("-------- 触发事件 --------------");
                    boolean flag = manager.onTrigger(messageStr);

                    if (channel == null) {
                        // do 异常。记录日志
                    }
                    if (flag) {
                        try {
                            channel.basicAck(envelope.getDeliveryTag(), false);
                            logger.info("消息已经手动确认被消费了..............................");

                        } catch (Exception e) {
                            logger.error("错误日志：", e);
                            channel.basicNack(envelope.getDeliveryTag(), false, true);
                            logger.info("消息处理失败，请重新处理... ");
                        }
                    } else {
                        // will do 相关的异常处理
                        logger.info("flag == false :触发了channel.basicNack方法.......消息重新返回给消息队列。");
                        channel.basicNack(envelope.getDeliveryTag(), false, true);
                    }

//                    // 用完channel之后去关闭掉channel
//                    try {
//                        mqUtils.closeChannel(channel);
//                    } catch (TimeoutException e) {
//                        e.printStackTrace();
//                        logger.error("关闭channel出错，详情为：", e);
//                    }

                }
            });

        } catch (IOException e) {
            logger.error("错误日志信息：", e);
            throw e;
        }


    }


}

