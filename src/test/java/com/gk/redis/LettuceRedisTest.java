package com.gk.redis;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;

public class LettuceRedisTest {
	
	/**
	 * 同步方式
	 */
	@Test
	public void test001() {
		//第一种连接方式	gk_redis是密码
		RedisClient client = RedisClient.create("redis://gk_redis@192.168.31.251:6379/0");
		//第二种连接方式
//		RedisClient client = RedisClient.create(RedisURI.create("redis://gk_redis@192.168.10.235:6379/0"));
		//第三种连接方式
//		RedisURI redisURI = RedisURI.Builder.redis("192.168.10.235",6379).withPassword("gk_redis").withDatabase(0).build();
//		RedisClient client = RedisClient.create(redisURI);
		//第四种连接方式
//		RedisURI redisURI = new RedisURI("192.168.10.235",6379,Duration.ofSeconds(20));
//		redisURI.setPassword("gk_redis");
//		RedisClient client = RedisClient.create(redisURI);
				
		client.setDefaultTimeout(Duration.ofSeconds(20));	//设置超时时间为20秒
		StatefulRedisConnection<String,String> connect = client.connect();
		RedisCommands<String,String> commands = connect.sync();	//同步命令
		commands.set("key2", "value2");
		commands.incr("test");
		String key1_val = commands.get("key2");
		String value1 = commands.get("key1");
		System.out.println(key1_val);
		System.out.println(value1);
		connect.close();
		client.shutdown();
	}
	
	/**
	 * 异步方式
	 */
	@Test
	public void test002() {
		RedisClient client = RedisClient.create("redis://gk_redis@192.168.10.235:6379/0");
		StatefulRedisConnection<String,String> connect = client.connect();
		RedisAsyncCommands<String,String> async = connect.async();
		RedisFuture<String> redisFuture = async.get("key1");
		try {
			// 设置了阻塞时间
			String value = redisFuture.get(20, TimeUnit.SECONDS);
			System.out.println(value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		connect.close();
		client.shutdown();
		//异步方式
//		redisFuture.thenAccept(new Consumer<String>() {
//			@Override
//			public void accept(String value) {
//				System.out.println(value);
//			}
//		});
//		connect.close();
//		client.shutdown();
	}

}
