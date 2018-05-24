package com.earthworm.rabbitmq.event;

import com.earthworm.rabbitmq.MessageDetail;

import java.util.EventObject;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/15 9:17
 * @version: v1.0.0
 * @Type:   MsgEventState
 * @Desc: event object：事件状态对象，定义事件对象，必须继承EventObject
 *          用于listener的相应的方法之中，作为参数，一般存在与listerner的方法之中
 *          伴随着事件的发生，相应的状态通常都封装在事件状态对象中，该对象必须继承自java.util.EventObject。
 *          事件状态对象作为单参传递给应响应该事件的监听者方法中。发出某种特定事件的事件源的标识是：
 *          遵从规定的设计格式为事件监听者定义注册方法，并接受对指定事件监听者接口实例的引用。

            具体的对监听的事件类，当它监听到event object产生的时候，它就调用相应的方法，进行处理。
 */
public class MsgEventState extends EventObject {

    // 消息字符串属性
    private String messageStr;

    public MsgEventState(Object source, String messageStr) {
        super(source);
        this.messageStr = messageStr;
    }

    // getter方法
    public String getMessageStr() {
        return messageStr;
    }


    // setter方法
    public void setMessageStr(String messageStr) {
        this.messageStr = messageStr;
    }
}
