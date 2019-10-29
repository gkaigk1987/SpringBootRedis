package com.gk.redis.service.impl;

import com.gk.redis.service.IRedisService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: GK
 * @Date: 2019/10/29
 */
@Service
public class RedisService implements IRedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public String secKill() {
        final String lockKey = "secKill_key";
        RLock lock = redissonClient.getLock(lockKey);

//        try {
//            TODO:第一个参数10s表示尝试获取分布式锁，并且最大的等待获取锁的时间为10s
//            TODO:第二个参数10s表示上锁之后，10s内操作完毕将自动释放锁
//            boolean b = lock.tryLock(10, 10, TimeUnit.SECONDS);
//            if(b) {
//                String secKill = stringRedisTemplate.opsForValue().get("secKill");
//                if(null == secKill || Integer.valueOf(secKill) < 100) {
//                    stringRedisTemplate.opsForValue().increment("secKill");
//                    return "1";
//                }
//                return "0";
//            }
//            return "2";
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return "2";
//        } finally {
//            if(null != lock) {
//                lock.unlock();
//            }
//        }

        try {
            lock.lock(10,TimeUnit.SECONDS);
            String secKill = stringRedisTemplate.opsForValue().get("secKill");
            if(null == secKill || Integer.valueOf(secKill) < 100) {
                stringRedisTemplate.opsForValue().increment("secKill");
                return "1";
            }
            return "0";
        }finally {
            if(null != lock) {
                lock.unlock();
            }
        }

    }
}
