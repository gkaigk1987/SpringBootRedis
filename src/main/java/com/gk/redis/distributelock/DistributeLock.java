package com.gk.redis.distributelock;

public interface DistributeLock {
    /**
     * 获取锁
     */
    public boolean acquire();

    /**
     * 释放锁
     * @return
     */
    public boolean release();
}
