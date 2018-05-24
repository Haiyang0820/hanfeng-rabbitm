package com.earthworm.rabbitmq.event;

import java.util.EventListener;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/15 9:23
 * @version: v1.0.0
 * @Type:
 * @Desc: 定义新的事件监听接口，该接口继承自EventListener；该接口包含对onEvent事件的处理程序：
 *          定义监听接口，负责监听MsgEventState事件
 */
public interface IMsgEventListener extends EventListener {

    /**
     * 具体doSomething ，
     * 可通过msgEventState中的msgStr来判断是否做什么事情
     */
    public boolean onEvent(MsgEventState msgEventState);
    // doEvent

}
