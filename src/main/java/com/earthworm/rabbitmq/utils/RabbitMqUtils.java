package com.earthworm.rabbitmq.utils;

import com.earthworm.rabbitmq.producer.PublisherHandler;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/3 18:43
 * @version: v1.0.0
 * @Type: RabbitMqUtils.java工具类
 * @Desc: 提供相应的ConnectionFactory的创建、Connection的创建和获取、Channel的获取和创建、队列的声明、交换机的声明和绑定
 */
@Component
public class RabbitMqUtils {
    private static Logger logger = LoggerFactory.getLogger(PublisherHandler.class);
    private Connection connection = null;

    @Autowired
    private PropertiesValueUtil propertiesValue;

    // 创建链接
    public Connection createConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(propertiesValue.getUsername());
        factory.setPassword(propertiesValue.getPassword());
        factory.setVirtualHost(propertiesValue.getVirtualHost());
        factory.setHost(propertiesValue.getHost());
        factory.setPort(propertiesValue.getPort());
        factory.setAutomaticRecoveryEnabled(true);
// connection that will recover automatically
        connection = factory.newConnection();
        logger.info("创建链接connection：{}", connection);
        return connection;
    }

    /**
     *
     * @return 获取Connection
     * @throws IOException
     * @throws TimeoutException
     */
    public Connection getConnection() throws IOException, TimeoutException {
        // 判断是否存在链接，没有链接，就创建链接，有链接，直接用
        if (connection == null) {
            synchronized (this) {
                connection = createConnection();
            }
        }
        return connection;
    }

    /**
     * 关闭链接
     *
     * @param connection
     * @throws IOException
     */
    public void closeConnection(Connection connection) throws IOException {
        if (connection != null) {
            connection.close();
        }
    }

    // 创建信道
    public Channel createChannel() throws IOException, TimeoutException {
        return getConnection().createChannel();
    }

    public Channel getChannel() throws IOException, TimeoutException {
        return createChannel();
    }

    // 关闭信道
    public void closeChannel(Channel channel) throws IOException, TimeoutException {
        if (channel != null && channel.isOpen()) {
            channel.close();
            logger.info("channel信道关闭");
        }
    }


    public boolean isConnection(){
        boolean isConnection = false;
        try {
            Connection connection = getConnection();
            if (connection == null){
                getConnection();
            }
        } catch (IOException |TimeoutException e) {
            logger.error("判断是否链接出异常，详情为：",e);
        }

        return isConnection;

    }

    /**
     * 声明队列
     * @param queue ：队列名
     * @throws IOException
     * @throws TimeoutException
     */
    public void queueDeclare(String queue) throws IOException, TimeoutException {
        Channel channel = getChannel();
        if (StringUtils.isNotBlank(queue)){
            channel.queueDeclare(queue,true,false,false,null);
            logger.info("队列 {}：声明成功",queue);
        }else {
            logger.info("队列名称不能为空........");
        }

        // 用完之后，关闭channel
        closeChannel(channel);

    }

    public void directExchangeDeclare(String exchange) throws IOException, TimeoutException {
        Channel channel = getChannel();
        if (StringUtils.isNotBlank(exchange)){
            channel.exchangeDeclare(exchange,BuiltinExchangeType.DIRECT,true);
            logger.info("交换机:{}声明成功",exchange);
        }else {
            logger.info("交换机为默认的交换机：Exchange: (AMQP default) name: {}",exchange);
        }

        // 用完之后，关闭channel
        closeChannel(channel);

    }

    public void binding(String queue,String exchange,String routingKey) throws IOException, TimeoutException {
        Channel channel = getChannel();
        directExchangeDeclare(exchange);
        queueDeclare(queue);
        if(StringUtils.isNotBlank(queue) &&StringUtils.isNotBlank(exchange)&&StringUtils.isNotBlank(routingKey) ){
            channel.queueBind(queue,exchange,routingKey);
            logger.info("交换机绑定成功.........");
        }

        // 用完之后，关闭channel
        closeChannel(channel);
    }


}
