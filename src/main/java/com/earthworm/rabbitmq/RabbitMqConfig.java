package com.earthworm.rabbitmq;

import com.earthworm.rabbitmq.utils.FastJsonUtils;
import com.earthworm.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Connection;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/3 18:14
 * @version: v1.0.0
 * @Type: RabbitMqConfig.java配置类
 * @Desc: RabbitMQ相关的配置类，包括初始化链接，初始化队列、交换机和绑定等相关操作
 */
@Configuration
public class RabbitMqConfig {
    private static Logger logger = LoggerFactory.getLogger(RabbitMqConfig.class);

    @Autowired
    private RabbitMqUtils mqUtils;

    @Bean
    public Connection createConnection(RabbitMqUtils utils,
                                       @Value("${rabbitmq.exchange.bind.direct}") String defaultQueues) {

        //
        utils = mqUtils;
        Connection conn = null;
        try {
//            factory.setUri("amqp://userName:password@hostName:portNumber/virtualHost");
            conn = utils.getConnection();
            logger.info("获取Connection链接成功，connection为：{}", conn);
            initQueueAndExchangeBinding(defaultQueues);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            logger.error("获取Connection链接失败，详情：{}", e);
        }

        return conn;
    }

    private void initQueueAndExchangeBinding(String defaultQueues) throws IOException, TimeoutException {

        Map<String, String> queueMap = FastJsonUtils.convertJsonStrToMap(defaultQueues);
        if (queueMap != null) {
            for (Map.Entry<String, String> queueEntity : queueMap.entrySet()) {
                // will do......
                String exchange = queueEntity.getKey();
                String routingKey = queueEntity.getValue();
                if ("".equals(exchange) && StringUtils.isNotBlank(routingKey)) {
                    mqUtils.queueDeclare(routingKey);
                } else if (StringUtils.isNotBlank(exchange) && StringUtils.isNotBlank(routingKey)) {
                    mqUtils.binding(exchange, exchange, routingKey);
                } else if (StringUtils.isBlank(routingKey)) {
                    logger.info("路由键不能为空............................");
                }
            }
        } else {
            logger.info("配置文件中没有配置任何交换机和队列....................");
        }
    }
}
