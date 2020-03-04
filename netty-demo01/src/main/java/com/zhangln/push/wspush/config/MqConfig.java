package com.zhangln.push.wspush.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sherry
 * @description
 * @date Create in 2019/12/9
 * @modified By:
 */
@Configuration
public class MqConfig {

    /**
     * 通用推送队列
     * @return
     */
    @Bean
    public Queue queue(){
        return new Queue("push-common",false,false,true);
    }

}
