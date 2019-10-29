package com.gk.redis.distributelock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: GK
 * @Date: 2019/10/28
 */
public class RedisDistributeLock implements DistributeLock {

    private RedissonClient redissonClient;

    /**
     * 分布式锁的键
     */
    private String lockKey;

    private RLock redLock;

    /**
     * 表示尝试获取分布式锁，并且最大的等待获取锁的时间
     */
    private int acquireTimeout = 500;

    /**
     * 锁的有效时间，即多久释放锁
     */
    private int expireTime = 10 * 1000;

    public RedisDistributeLock(RedissonClient redissonClient, String lockKey) {
        this.redissonClient = redissonClient;
        this.lockKey = lockKey;
    }

    public RedisDistributeLock(RedissonClient redissonClient, String lockKey, int acquireTimeout, int expireTime) {
        this.redissonClient = redissonClient;
        this.lockKey = lockKey;
        this.acquireTimeout = acquireTimeout;
        this.expireTime = expireTime;
    }


    @Override
    public boolean acquire() {
        redLock = redissonClient.getLock(lockKey);
        //以下代码在多线程下会出错
//        boolean flag = false;
//        try {
//            flag = redLock.tryLock(acquireTimeout,expireTime, TimeUnit.MILLISECONDS);
//            if(flag) {
//                System.out.println(Thread.currentThread().getName() + ":" + lockKey + "获取到锁");
//            }else {
//                System.out.println(Thread.currentThread().getName() + "未获取到锁");
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return flag;
        redLock.lock(expireTime,TimeUnit.MILLISECONDS);
        return true;
    }

    @Override
    public boolean release() {
        if(null != redLock) {
            redLock.unlock();
            return true;
        }
        return false;
    }
}
