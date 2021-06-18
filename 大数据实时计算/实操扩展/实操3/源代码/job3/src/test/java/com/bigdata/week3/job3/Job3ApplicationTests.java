package com.bigdata.week3.job3;

import com.alibaba.fastjson.JSON;
import com.bigdata.week3.job3.Kafka.Producer;
import com.bigdata.week3.job3.Model.BuyInfo;
import com.bigdata.week3.job3.Service.BuyInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class Job3ApplicationTests {

	@Autowired
	BuyInfoService buyInfoService;

	@Test
	void contextLoads() {
		List<BuyInfo> buyInfoList=buyInfoService.getBuyInfoList();
		for(BuyInfo buyInfo:buyInfoList){
			Producer.produceToKafka(JSON.toJSONString(buyInfo));
		}
	}

}
