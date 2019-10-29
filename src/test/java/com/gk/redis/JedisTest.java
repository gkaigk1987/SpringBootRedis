package com.gk.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: TODO
 * @Author: GK
 * @Date: 2019/10/29
 * 该实现有问题，线程数越多越不准确
 */
public class JedisTest {
    public static void main(String[] args) {
        String redisKey = "redisTest";
        ExecutorService executorService = Executors.newFixedThreadPool(1000);
        try {
            Jedis jedis = new Jedis("192.168.31.251", 6379);
            jedis.auth("gk_redis");
            jedis.set(redisKey, "0");
            jedis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 1000; i++) {
            executorService.execute(() -> {
                Jedis jedis1 = new Jedis("192.168.31.251", 6379);
                jedis1.auth("gk_redis");
                try {
                    jedis1.watch(redisKey);
                    String redisValue = jedis1.get(redisKey);
                    int valInteger = Integer.valueOf(redisValue);
                    String userInfo = UUID.randomUUID().toString();
                    if (valInteger < 100) {
                        Transaction transaction = jedis1.multi();
                        transaction.incr(redisKey);
                        List list = transaction.exec();
                        if (list != null) {
                            System.out.println("用户：" + userInfo + "，秒杀成功！当前成功人数：" + (valInteger + 1));
                        } else {
                            System.out.println("用户：" + userInfo + "，秒杀失败");
                        }
                    } else {
                        System.out.println("已经有20人秒杀成功，秒杀结束");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    jedis1.close();
                }
            });
        }
        executorService.shutdown();
    }
}
