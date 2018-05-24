package com.earthworm.rabbitmq.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: yangh@i-earthworm.com
 * @Date: 2018/5/22 13:54
 * @version: v1.0.0
 * @Type:
 * @Desc: 消费者3
 */
@Component
public class MsgListenerImpl3 implements IMsgEventListener {
    // slf4j日志相关
    private Logger logger = LoggerFactory.getLogger(MsgListenerImpl3.class);

    @Override
    public boolean onEvent(MsgEventState msgEventState) {

        String messageStr = msgEventState.getMessageStr();
        if (msgEventState == null || messageStr == null) {
            logger.info("没有消息可被消费........");
            return false;
        }

        logger.info("MsgListenerImpl3中收到的 messageStr:{}", messageStr);
        logger.info("---------------------------------------------------------");
        
        return true;
    }
}
