package com.earthworm.rabbitmq;

import java.util.UUID;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/3 15:15
 * @version: v1.0.0
 * @Type:
 * @Desc: MessageDetail实体：
 * 其中包含了要发送的消息内容content，id,ip,deliverHost等小关信息
 */

public class MessageDetail {
    // 消息信息的唯一标识符id
    private String id;
    // 消息发送的ip
    private String ip;
    // 消息发送的主机
    private String deliverHost;
    // 发送消息时生成的时间戳
    private long timestamp;

    // 消息体的具体内容
    private Object content;


    /**
     * 默认的构造器方法
     */
    public MessageDetail() {
    }

    /**
     * 包含指定参数的构造函数
     *
     * @param ip               ：包含ip地址
     * @param deliverHost：主机地址
     * @param content：要发送的内容
     */

    public MessageDetail(String ip, String deliverHost, Object content) {
        this.id = UUID.randomUUID().toString();
        this.ip = ip;
        this.deliverHost = deliverHost;
        this.timestamp = System.currentTimeMillis();
        this.content = content;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public Object getContent() {
        return content;
    }


    public void setContent(Object content) {
        this.content = content;
    }

    public String getDeliverHost() {
        return deliverHost;
    }

    public void setDeliverHost(String deliverHost) {
        this.deliverHost = deliverHost;
    }

    @Override
    public String toString() {
        return "MessageDetail{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", deliverHost='" + deliverHost + '\'' +
                ", timestamp=" + timestamp +
                ", content=" + content +
                '}';
    }
}
