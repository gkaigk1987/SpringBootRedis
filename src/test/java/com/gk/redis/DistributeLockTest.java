package com.gk.redis;

import com.gk.redis.distributelock.RedisDistributeLock;
import com.gk.redis.util.RedisUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import javax.sound.midi.Soundbank;

/**
 * @Description: TODO
 * @Author: GK
 * @Date: 2019/10/28
 */
public class DistributeLockTest {

//    static  int n = 5;
//
//    public static void secKill() {
//        if(n < 1) {
//            System.out.println(Thread.currentThread().getName() + "无法抢购，抢购已完成");
//            return;
//        }
//        System.out.println(--n);
//    }

    public static void secKill() {
        RedisUtil redisUtil = new RedisUtil("192.168.31.251",6379,"gk_redis",0);
        String secsKill = redisUtil.getKey("secsKill");
        if(null == secsKill || Integer.valueOf(secsKill) < 10) {
            redisUtil.incr("secsKill");
        }else {
            System.out.println(Thread.currentThread().getName() + "无法抢购，抢购已完成");
        }
        redisUtil.shutdownConnection();
    }

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.31.251:6379")
                .setPassword("gk_redis").setDatabase(0);
        for (int i = 0; i < 20; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    RedissonClient redissonClient = null;
                    RedisDistributeLock d_lock = null;
                    try {
                        redissonClient = Redisson.create(config);
                        d_lock = new RedisDistributeLock(redissonClient, "d_lock");
                        d_lock.acquire();
                        secKill();
                        System.out.println(Thread.currentThread().getName() + "正在运行");
                    }finally {
                        if(d_lock != null) {
                            System.out.println(Thread.currentThread().getName() + "release lock");
                            d_lock.release();
                        }
                        if(null != redissonClient) {
                            redissonClient.shutdown();
                        }
                    }
                }
            });
            t.setName("gk_test_" + i);
            t.start();
        }
    }
}
