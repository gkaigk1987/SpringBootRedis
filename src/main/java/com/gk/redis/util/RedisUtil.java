package com.gk.redis.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;

/**
 * @Description: TODO
 * @Author: GK
 * @Date: 2019/10/28
 */
public class RedisUtil {

    private String host;

    private int port;

    private String password;

    private int database;

    private RedisClient redisClient;

    public RedisUtil() {
        this.host = "localhost";
        this.port = 6379;
        this.password = null;
        this.database = 0;
    }

    public RedisUtil(String host, int port, String password, int database) {
        this.host = host;
        this.port = port;
        this.password = password;
        this.database = database;
    }

    private RedisClient getClient() {
        RedisURI redisURI = null;
        if(null != password && password.length() > 0) {
            redisURI = RedisURI.Builder.redis(host,port).withPassword(password).withDatabase(database).build();
        }else {
            redisURI = RedisURI.Builder.redis(host,port).withDatabase(database).build();
        }
        redisClient = RedisClient.create(redisURI);
		return redisClient;
    }

    public String getKey(String key) {
        if(null == redisClient) {
            redisClient = getClient();
        }
        redisClient.setDefaultTimeout(Duration.ofSeconds(20));
        StatefulRedisConnection<String,String> connect = redisClient.connect();
        RedisCommands<String,String> commands = connect.sync();	//同步命令
        String value = commands.get(key);
        connect.close();
        return value;
    }

    public void incr(String key) {
        if(null == redisClient) {
            redisClient = getClient();
        }
        redisClient.setDefaultTimeout(Duration.ofSeconds(20));
        StatefulRedisConnection<String,String> connect = redisClient.connect();
        RedisCommands<String,String> commands = connect.sync();	//同步命令
        commands.incr(key);
        connect.close();
    }

    public void shutdownConnection() {
        if(null != redisClient) {
            redisClient.shutdown();
        }
    }
}
