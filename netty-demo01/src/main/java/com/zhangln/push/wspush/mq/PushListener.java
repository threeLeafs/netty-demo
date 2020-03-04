package com.zhangln.push.wspush.mq;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.zhangln.push.wspush.vo.HttpResVo;
import com.zhangln.push.wspush.vo.HttpWsPushCondition;
import com.zhangln.push.wspush.controller.service.WsPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/9
 * @modified By:
 */

@Slf4j
@Component
public class PushListener {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private WsPushService wsPushService;

    /**
     * 通用异步推送
     * 为了避免重复推送，所以这里的MQ采用工作队列模式
     *
     * @param message
     * @param channel
     */
    @RabbitHandler
    @RabbitListener(queues = "push-common")
    public void commomPush(Message message, Channel channel) {
        byte[] body = message.getBody();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(body);
             ObjectInputStream oii = new ObjectInputStream(bais);) {
            Object o = oii.readObject();
            //再做一次强制类型转化，就可以拿到生产者发送的对象数据了
            HttpWsPushCondition httpWsPushCondition = (HttpWsPushCondition) o;
            log.info("处理一次异步推送：{}", JSONObject.toJSONString(httpWsPushCondition));
            HttpResVo httpResVo = wsPushService.commonPushService(httpWsPushCondition);
            log.info("本次推送结果：{}",JSONObject.toJSONString(httpResVo));
//            广播本次推送结果
            rabbitTemplate.convertAndSend("push-result", "", httpResVo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                // 返回确认状态
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception e) {
                log.error("RabbitMQ 网关日志ACK异常", e);
            }
        }
    }

    /**
     * 监听广播中的推送结果
     *
     * @param message
     * @param channel
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "push-result-1", durable = "false", autoDelete = "true"),
            exchange = @Exchange(value = "push-result", type = ExchangeTypes.FANOUT)
    ))
    public void pushResult(Message message, Channel channel) {
        byte[] body = message.getBody();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(body);
             ObjectInputStream oii = new ObjectInputStream(bais);) {
            Object o = oii.readObject();
            HttpResVo httpResVo = (HttpResVo) o;
            log.info("接收到一次MQ异步推送结果：{}", JSONObject.toJSONString(httpResVo));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                // 返回确认状态
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception e) {
                log.error("RabbitMQ 网关日志ACK异常", e);
            }
        }
    }

}
