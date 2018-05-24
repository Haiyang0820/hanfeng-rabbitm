package com.earthworm.rabbitmq.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/4/28 9:41
 * @version: v1.0.0
 * @Type: PropertiesValueUtil.java
 * @Desc: Rabbitmq属性相关配置属性注入工具类
 *   // 加上消息重发次数属性
 */

@Component
public class PropertiesValueUtil {
    // 通过application.properties文件注入交换机类型
    @Value("${rabbitmq.host}")
    private String host;

    // 通过application.properties文件注入交换机名称
    @Value("${rabbitmq.port}")
    private int port;

    // 通过application.properties文件注入消息持久化状态
    @Value("${rabbitmq.virtualHost}")
    private String virtualHost;

    // 通过application.properties文件注入交换机是否自动删除属性
    @Value("${rabbitmq.username}")
    private String username;

    // 通过application.properties文件注入队列名称
    @Value("${rabbitmq.password}")
    private String password;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

