package com.gk.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.gk.redis.entity.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootRedisApplicationTests {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	
	@Test
	public void test001() {
		String key = "name";
		String value = "高凯";
		stringRedisTemplate.opsForValue().set(key, value);
		
		String redisValue = stringRedisTemplate.opsForValue().get(key);
		
		System.out.println("key=" + key + ",获取的value值为:" + redisValue);
	}
	
	@Test
	public void test002() {
		User user = new User();
		user.setId(1);
		user.setUserName("严倩倩");
		user.setAge(28);
		
		redisTemplate.opsForValue().set("wife", user);
		
		User wife = (User) redisTemplate.opsForValue().get("wife");
		System.out.println(wife.getUserName());
	}

}
