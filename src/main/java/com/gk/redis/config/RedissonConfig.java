package com.gk.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: TODO
 * @Author: GK
 * @Date: 2019/10/29
 */
@Configuration
public class RedissonConfig {

    @Value("${redis.config.host}")
    private String host;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(host)
                .setPassword(password)
                .setDatabase(0);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
