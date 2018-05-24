package com.earthworm.rabbitmq.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/15 9:27
 * @version: v1.0.0
 * @Type:
 * @Desc: 通过MsgEventSourceManager.java创造一个事件源类，它用一个List<IMsgEventListener> listeners
 * 对象来存储所有的事件监听器对象，存储方式是通过addDoorListener(..)这样的方法。
 * notifyListeners(..)是触发事件的方法，用来通知系统：事件发生了，你调用相应的处理函数吧。
 * 事件源对象，在这里你可以把它想象成一个控制开门关门的遥控器，
 */
public class MsgEventSourceManager {

    //所有消息监听者
    private List<IMsgEventListener> listeners;

    /**
     * 添加事件
     *
     * @param listener MsgEventListener
     */
    public void addMsgEventListener(IMsgEventListener listener) {
        if (listeners == null) {
            listeners = new CopyOnWriteArrayList<IMsgEventListener>();
        }
        listeners.add(listener);
    }

    /**
     * 移除事件
     *
     * @param listener MsgEventListener
     */
    public void removeMsgEventListener(IMsgEventListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }

    /**
     * 触发逻辑事件
     */
    public boolean onTrigger(String messageStr) {

        if (listeners == null)
            return false;

        MsgEventState event = new MsgEventState(this, messageStr);
        boolean flag = notifyListeners(event);
        return true;

    }


    /**
     * 通知所有的msgListener
     */
    private boolean notifyListeners(MsgEventState event) {
        for (IMsgEventListener listener : listeners) {
            // 如果事件未执行成功，那么就返回false状态
            if (!listener.onEvent(event)) {
                return false;
            }
        }
        // 事件处理完成之后，返回true;
        return true;
    }


}
