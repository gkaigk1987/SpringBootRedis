package com.gk.redis.service.impl;

import com.gk.redis.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: GK
 * @Date: 2019/10/29
 */
@Slf4j
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
        int flag = 0;
        try {
            lock.lock(10,TimeUnit.SECONDS);
//            long start = System.currentTimeMillis();
            String secKill = stringRedisTemplate.opsForValue().get("secKill");
            if(null == secKill || Integer.valueOf(secKill) < 100) {
                stringRedisTemplate.opsForValue().increment("secKill");
//                long end = System.currentTimeMillis();
//                log.info("抢到用时：{}",(end - start));
                flag = 1;
//                return "1";
            }
//            long end = System.currentTimeMillis();
//            log.info("抢购失败用时：{}",(end - start));
//            return "0";
        }finally {
            if(null != lock) {
                lock.unlock();
            }
        }
        if(flag == 1) {
            stringRedisTemplate.opsForValue().increment("flag");
        }
        return "1";
    }

    @Override
    public void secKill2() {
        ListOperations<String, String> op = stringRedisTemplate.opsForList();
        if(op.size("secKill2") < 100) {
            op.rightPush("secKill2", UUID.randomUUID().toString());
        }
    }

}
