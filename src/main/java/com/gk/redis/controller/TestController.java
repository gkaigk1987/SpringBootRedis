package com.gk.redis.controller;

import com.alibaba.fastjson.JSONObject;
import com.gk.redis.service.impl.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

	@Autowired
	private RedisService redisService;

	@GetMapping("/hello")
	public String hello() {
		return "Hello Docker";
	}

	/**
	 * 秒杀入口
	 * @return
	 */
	@PostMapping(value = "/secKill")
	@ResponseBody
	public JSONObject testSecKill() {
		JSONObject json = new JSONObject();
		String secKill = redisService.secKill();
		if("1".equals(secKill)) {
			json.put("succ","true");
			json.put("msg","购买成功");
			log.info("购买成功");
		}else if("0".equals(secKill)) {
			json.put("succ","true");
			json.put("msg","商品已卖完");
			log.info("商品已售罄");
		}else {
			json.put("succ","false");
			json.put("msg","未获取锁");
			log.info("未获取锁");
		}
		return json;
	}
	
}
