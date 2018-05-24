package com.earthworm.rabbitmq.producer;

import com.earthworm.rabbitmq.MessageDetail;
import com.earthworm.rabbitmq.utils.FastJsonUtils;
import com.earthworm.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/5 13:47
 * @version: v1.0.0
 * @Type: Publisher.java
 * @Desc: 消息发布者
 */
@Component
public class PublisherHandler {

    // slf4j日志相关
    private static Logger logger = LoggerFactory.getLogger(PublisherHandler.class);
    // 自动装配RabbitMqUtils工具类
    @Autowired
    private RabbitMqUtils mqUtils;

    /**
     * 消息发布方法
     *
     * @param message    ： 带有消息内容的消息实体对象
     * @param routingKey ：路由键
     * @throws IOException             ：IO异常
     * @throws TimeoutException：链接超时异常
     */
    public void publishMessage(MessageDetail message, String routingKey) throws IOException, TimeoutException {
        publishMessage(message, "", routingKey);
    }

    /**
     * 消息发布的方法
     *
     * @param message：         带有消息内容的消息实体对象
     * @param exchange         ：交换机名称
     * @param routingKey：路由键名称
     * @throws TimeoutException：IO异常
     * @throws IOException：链接超时异常
     */
    public void publishMessage(MessageDetail message, String exchange, String routingKey) throws TimeoutException, IOException {

        Channel channel = mqUtils.getChannel();

        if (message != null && StringUtils.isNotBlank(routingKey)) {
            String msg = FastJsonUtils.toJSONString(message);
            AMQP.BasicProperties pros = new AMQP.BasicProperties()
                    .builder()
                    .contentEncoding("utf-8") // 指定编码
                    .contentType("text/plain") // 指定contentType
                    .deliveryMode(2) // 2代表为消息持久化
                    .priority(1) // 指定权重
                    .build();

            logger.info("开始发送消息....:{}", message.getContent());
            // 利用channel信道发送消息
            channel.basicPublish(exchange, routingKey, pros, msg.getBytes("utf-8"));
            logger.info("消息：{} 发布成功", msg);
        }

        if (StringUtils.isBlank(exchange)) {
            publishMessage(message, routingKey);
            logger.info("exchange为空..........，使用默认的交换机进行信息发送......");
        }

        // 发布完消息之后，需要进行关闭channel
        mqUtils.closeChannel(channel);

    }

}
