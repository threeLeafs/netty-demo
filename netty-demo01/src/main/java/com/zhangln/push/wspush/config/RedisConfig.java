package com.zhangln.push.wspush.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author wangpingfei
 * @description ddd
 * @date Create in 2020/03/04
 * @modified By:
 */
@Configuration
public class RedisConfig {
    @Primary
    @Bean
    public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 使用其他序列化替代jdk序列化
        RedisSerializer<Object> redisSerializer = getRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(redisSerializer);
        template.setValueSerializer(redisSerializer);
        template.setHashKeySerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    private RedisSerializer<Object> getRedisSerializer() {
        // jackson序列化
//      Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//      ObjectMapper om = new ObjectMapper();
//      om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
//      om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//      jackson2JsonRedisSerializer.setObjectMapper(om);
        // fastjson序列化
        return new GenericFastJsonRedisSerializer();
    }

}
